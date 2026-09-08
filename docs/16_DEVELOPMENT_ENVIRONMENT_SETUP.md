# Development Environment and Bootstrap

## Purpose

This document records the intended Windows development setup and the procedure for creating the first Fabric project.

Toolchain versions change over time. **Verify the current official Fabric/Minecraft requirements immediately before creating or upgrading the project.** Once a milestone begins, pin the chosen versions rather than continuously chasing new Minecraft releases.

---

# Platform Choice

Current project direction:

```text
Minecraft Java Edition
Fabric Loader
Fabric API
Java
Gradle Wrapper / Fabric Loom
IntelliJ IDEA
Git
```

Fabric was chosen because the project needs:

- custom code and entities,
- networking,
- server-authoritative gameplay,
- custom UI,
- registries/data loading,
- Mixins when vanilla behavior lacks an appropriate API hook,
- a development environment suitable for a long-running total conversion.

---

# Provisional Bootstrap Baseline

At the latest design discussion, the intended baseline was:

```text
Minecraft Java: 26.2
Java/JDK: 25
Fabric: current compatible Loader + Fabric API
Fabric Loom: current compatible release
Gradle: use the project wrapper
IDE: IntelliJ IDEA
```

**Important:** these values are a recorded project discussion, not a permanent compatibility guarantee. Verify the current Fabric documentation before generating the actual project.

Once the first real source project is created, the exact versions in its Gradle files become authoritative for that development milestone.

---

# Windows Tools to Install

## 1. JDK

Install the JDK version required by the chosen Minecraft/Fabric version.

Preferred distribution:

```text
Eclipse Temurin
```

After installation, verify in PowerShell:

```powershell
java -version
javac -version
```

Both should resolve to the intended JDK.

---

## 2. IntelliJ IDEA

Install a current IntelliJ IDEA version compatible with the Fabric development documentation.

Recommended optional plugin:

```text
Minecraft Development
```

Use Fabric's official project generator/templates for the actual baseline rather than trusting an IDE plugin template to be current.

---

## 3. Git for Windows

Install Git and verify:

```powershell
git --version
```

Use version control from the first code milestone.

---

# Recommended Project Location

Use a simple local path outside OneDrive/synchronized folders.

Example:

```text
C:\Projects\PrimitiveCivilization
```

Avoid unusual characters and unnecessarily deep paths.

---

# Generate the Fabric Project

Use Fabric's official template/project generator at implementation time.

Recommended options:

```text
Language: Java
Fabric API: enabled
Data Generation: enabled
```

Working identifiers may initially be:

```text
Display name: Primitive Civilization
Mod ID: primitivecivilization
Package: com.primitivecivilization
```

These identifiers are still provisional until the first source project is committed.

Data generation should be enabled because the project is expected to contain large quantities of data-driven content such as:

- tags,
- recipes/process definitions,
- loot rules,
- models/blockstates where suitable,
- advancements or equivalent data,
- generated validation-support data.

The project should not require a globally installed Gradle. Use the included Gradle wrapper.

---

# Import in IntelliJ

Extract the generated project directly into the project root, for example:

```text
C:\Projects\PrimitiveCivilization\
    build.gradle / build.gradle.kts
    gradle.properties
    settings.gradle / settings.gradle.kts
    gradlew
    gradlew.bat
    src\
```

Open that root folder in IntelliJ and allow the Gradle import/sync to finish.

Confirm the Gradle JVM is set to the required JDK version.

---

# Baseline Commands

Use the project wrapper from PowerShell in the project root.

## Run development client

```powershell
.\gradlew.bat runClient
```

## Run development dedicated server

```powershell
.\gradlew.bat runServer
```

## Build

```powershell
.\gradlew.bat build
```

The exact generated run tasks can vary by template/Loom version; confirm them in the imported project if necessary.

---

# Multiplayer Verification From Day One

Before any gameplay milestone is considered stable:

1. run the integrated client environment,
2. run the dedicated-server environment,
3. verify the mod loads without client-only class crashes,
4. test authoritative gameplay requests against the server path.

This requirement exists even during the primitive prototype because later settlers, knowledge, settlement permissions, reservations, and work orders all depend on correct server/client separation.

---

# Initial Repository Setup

After the untouched template successfully builds and launches:

```powershell
git init
git add .
git commit -m "chore: bootstrap Fabric project"
```

The exact Git branching strategy may evolve, but the baseline project should be committed before gameplay changes begin.

Recommended first implementation sequence:

```text
bootstrap
↓
verify client
↓
verify dedicated server
↓
add docs into repository
↓
establish module/package skeleton
↓
data validation foundation
↓
Phase 1: vanilla bootstrap locks
```

---

# Suggested Initial Source Structure

Do not create empty abstractions merely to match this diagram, but the architecture should trend toward clear modules such as:

```text
src/main/java/.../
    core/
    progression/
    materials/
    workactions/
    tools/
    worldresources/
    settlers/
    settlement/
    knowledge/
    questbook/
    networking/

src/main/resources/
    assets/...
    data/...
```

A coarse package/resource skeleton is now present in the repository to make the intended boundaries visible. Do not keep adding speculative nested packages ahead of implementation; refine or remove these placeholders as actual responsibilities become concrete.

---

# What to Send to a New GPT/Developer

After generating the untouched Fabric project:

1. include this documentation set in the project,
2. ZIP the complete project,
3. provide the complete ZIP rather than snippets,
4. instruct the assistant to read the project recursively before editing.

Recommended instruction:

> Read the entire project ZIP recursively. Treat it as the source of truth. Read README, CURRENT_STATE_AND_HANDOFF, DEVELOPMENT_WORKFLOW, TECHNICAL_ARCHITECTURE, and INITIAL_VERTICAL_SLICE before coding. Audit the current implementation first, then implement the next roadmap milestone without introducing singleplayer-only or throwaway architecture.

---

# Upgrade Policy

Do not automatically update Minecraft/Fabric every time a new version releases.

Upgrade only when there is a clear reason and after checking:

- Fabric Loader/API compatibility,
- Loom/Gradle requirements,
- Java version requirements,
- mappings/name changes,
- Mixins/API breakage,
- saved data compatibility,
- third-party compatibility if the project later integrates with other mods.

For a long-running total conversion, development stability is usually more valuable than immediately targeting every new Minecraft release.

---

## Verified Project Bootstrap State

The current source project was generated and tested with:

```text
Minecraft: 26.2
Java target: 25
Fabric Loader: 0.19.5
Fabric API: 0.159.0+26.2
Fabric Loom: 1.17-SNAPSHOT
Mod ID: realisticciv
Java package: com.realisticciv
```

The project owner has manually verified:

```text
runClient                         ✓
runServer                         ✓
dev client joins dev server       ✓
```

### Local Development Authentication

The Loom development client does not necessarily carry a normal authenticated launcher session. For convenient local dedicated-server testing, the local-only file:

```text
run/server.properties
```

may use:

```properties
online-mode=false
```

`run/` is ignored and excluded from handoff/source ZIPs, so this does not change the released mod or dictate production/public-server configuration.

Friend/public/release-like testing should use a normal Fabric installation and normal authenticated Minecraft clients against an `online-mode=true` server.
