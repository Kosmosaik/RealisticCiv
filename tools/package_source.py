#!/usr/bin/env python3
"""Create a clean source ZIP for RealisticCiv development handoffs.

Run from anywhere:
    python tools/package_source.py

Optional:
    python tools/package_source.py --output C:\\Temp\\RealisticCiv-source.zip

The script deliberately excludes generated caches, build output, local Minecraft
runtime/server files, IDE state, test worlds, logs, previous ZIPs, and common
secret/credential files. It keeps source, Gradle wrapper/build files, docs,
GitHub workflow files, and project tooling.
"""

from __future__ import annotations

import argparse
import fnmatch
import re
import sys
import zipfile
from pathlib import Path

PROJECT_NAME = "RealisticCiv"

EXCLUDED_DIR_NAMES = {
    ".git",
    ".gradle",
    ".idea",
    ".vscode",
    ".settings",
    ".cache",
    "build",
    "out",
    "classes",
    "bin",
    "run",
    "dist",
    "node_modules",
    "__pycache__",
}

EXCLUDED_FILE_PATTERNS = (
    "*.class",
    "*.log",
    "*.hprof",
    "*.jfr",
    "*.iml",
    "*.ipr",
    "*.iws",
    "*.pyc",
    "*.pyo",
    "*.tmp",
    "*.temp",
    "*.bak",
    "*.swp",
    "*.swo",
    "*.zip",
    ".DS_Store",
    "Thumbs.db",
)

# Prevent accidental handoff of common secrets if such files are introduced later.
SENSITIVE_FILE_PATTERNS = (
    ".env",
    ".env.*",
    "*.pem",
    "*.key",
    "*.p12",
    "*.pfx",
    "*.jks",
    "*.keystore",
    "*credentials*",
    "*token*",
    "*secret*",
)

# The Gradle wrapper JAR is source-project infrastructure and must be included.
ALLOWED_JARS = {
    "gradle/wrapper/gradle-wrapper.jar",
}

REQUIRED_PATHS = (
    "build.gradle",
    "gradle.properties",
    "settings.gradle",
    "gradlew",
    "gradlew.bat",
    "gradle/wrapper/gradle-wrapper.properties",
    "gradle/wrapper/gradle-wrapper.jar",
    "src",
)


def project_root() -> Path:
    return Path(__file__).resolve().parent.parent


def read_version(root: Path) -> str:
    props = root / "gradle.properties"
    try:
        text = props.read_text(encoding="utf-8")
    except OSError:
        return "unknown"
    match = re.search(r"(?m)^version\s*=\s*([^\s#]+)", text)
    return match.group(1) if match else "unknown"


def is_sensitive(name: str) -> bool:
    lower = name.lower()
    return any(fnmatch.fnmatch(lower, pattern.lower()) for pattern in SENSITIVE_FILE_PATTERNS)


def should_exclude(relative: Path) -> tuple[bool, str]:
    parts = relative.parts
    if any(part in EXCLUDED_DIR_NAMES for part in parts[:-1]):
        return True, "generated/local directory"

    name = relative.name
    posix = relative.as_posix()

    if is_sensitive(name):
        return True, "possible secret/credential"

    if name.endswith(".jar") and posix not in ALLOWED_JARS:
        return True, "non-wrapper JAR"

    if any(fnmatch.fnmatch(name, pattern) for pattern in EXCLUDED_FILE_PATTERNS):
        return True, "generated/archive file"

    return False, ""


def validate_project(root: Path) -> None:
    missing = [p for p in REQUIRED_PATHS if not (root / p).exists()]
    if missing:
        print("ERROR: This does not look like the expected RealisticCiv project.", file=sys.stderr)
        print("Missing:", file=sys.stderr)
        for path in missing:
            print(f"  - {path}", file=sys.stderr)
        raise SystemExit(2)


def collect_files(root: Path) -> tuple[list[Path], dict[str, int]]:
    included: list[Path] = []
    excluded_counts: dict[str, int] = {}

    for path in root.rglob("*"):
        if not path.is_file():
            continue
        relative = path.relative_to(root)
        excluded, reason = should_exclude(relative)
        if excluded:
            excluded_counts[reason] = excluded_counts.get(reason, 0) + 1
            continue
        included.append(path)

    included.sort(key=lambda p: p.relative_to(root).as_posix().lower())
    return included, excluded_counts


def main() -> int:
    parser = argparse.ArgumentParser(description="Package a clean RealisticCiv source ZIP.")
    parser.add_argument(
        "--output",
        type=Path,
        help="Output ZIP path. Default: dist/RealisticCiv-source-v<version>.zip",
    )
    args = parser.parse_args()

    root = project_root()
    validate_project(root)
    version = read_version(root)

    output = args.output
    if output is None:
        output = root / "dist" / f"{PROJECT_NAME}-source-v{version}.zip"
    elif not output.is_absolute():
        output = (Path.cwd() / output).resolve()

    output.parent.mkdir(parents=True, exist_ok=True)
    if output.exists():
        output.unlink()

    files, excluded_counts = collect_files(root)
    total_bytes = sum(path.stat().st_size for path in files)

    with zipfile.ZipFile(output, "w", compression=zipfile.ZIP_DEFLATED, compresslevel=9) as archive:
        for path in files:
            relative = path.relative_to(root)
            archive_name = (Path(PROJECT_NAME) / relative).as_posix()
            archive.write(path, archive_name)

    zip_size = output.stat().st_size
    print(f"Created: {output}")
    print(f"Included files: {len(files)}")
    print(f"Source size: {total_bytes / 1024 / 1024:.2f} MiB")
    print(f"ZIP size: {zip_size / 1024 / 1024:.2f} MiB")
    if excluded_counts:
        print("Excluded:")
        for reason, count in sorted(excluded_counts.items()):
            print(f"  {count:4d}  {reason}")
    print("\nThis ZIP intentionally excludes run/, build/, .gradle/, .idea/, test worlds, logs, and previous archives.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
