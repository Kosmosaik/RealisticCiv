# Development Tools

## `package_source.py`

Creates the clean project ZIP intended for GPT/developer handoffs.

From the project root on Windows:

```powershell
python .\tools\package_source.py
```

Output:

```text
dist/RealisticCiv-source-v<version>.zip
```

The archive includes the source project, documentation, Gradle wrapper/build configuration, GitHub workflow files, and development tools. It excludes local/generated content such as `.gradle/`, `build/`, `run/`, `.idea/`, worlds, logs, previous ZIPs, and common credential/secret files.

You normally do **not** need to manually prepare a source ZIP anymore; run this script and upload the generated file.
