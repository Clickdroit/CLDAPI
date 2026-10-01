# CLDAPI — UHC Minecraft API

> A modular Java API for building and hosting competitive UHC game modes on Spigot 1.8.8.

## Overview

CLDAPI provides the core game infrastructure needed to build UHC modes without coupling every feature to a single plugin.

**Main areas:** game lifecycle, teams, scenarios, configuration GUIs, world border, combat-log protection, world pre-generation and external game modules.

## Architecture

```text
                         ┌─────────────────────┐
                         │      CLDAPI         │
                         └──────────┬──────────┘
                                    │
             ┌──────────────────────┼──────────────────────┐
             ▼                      ▼                      ▼
       Game lifecycle          Module registry        Configuration
             │                      │                      │
             ▼                      ▼                      ▼
       Players / teams        External game modes        GUIs
             │
             ▼
       Events / listeners
```

The project is organised around separate game, command, configuration, listener, module and world-management responsibilities.

## Features

- Game lifecycle and player management
- Solo and team modes
- Configurable scenarios
- In-game administration GUI
- Automatic world-border management
- Combat-log protection
- World pre-generation utilities
- External game-module integration

## Requirements

- Java 8+
- Spigot/Paper 1.8.8
- Gradle 8.x for development/build tooling

## Build

```bash
./gradlew build
./gradlew shadowJar
```

PowerShell:

```powershell
.\gradlew.bat --no-daemon test shadowJar
```

A successful build does not replace testing on a development server.

## Commands

| Command | Purpose | Permission |
|---|---|---|
| `/host` | Game administration | `uhc.host` |
| `/scenario` | Scenario configuration | `uhc.host` |
| `/whitelist` | Whitelist management | `uhc.host` |
| `/rules` | Display rules | `uhc.player` |
| `/helpop` | Request staff help | `uhc.player` |
| `/tc` | Team coordinates | `uhc.player` |

## External modules

External plugins can register their own game modes through `GameModuleRegistry`.

Typical integration:

1. Declare CLDAPI as a dependency.
2. Implement `GameModule`.
3. Register the module through `API.getAPI().getModuleRegistry()`.
4. Handle the lifecycle callbacks required by the game mode.

See the source tree and existing implementations for the complete API contract.

## Project structure

```text
src/main/java/fr/clickdroit/api/
├── commands/
├── common/
├── config/
├── game/
├── listener/
├── module/
├── utils/
└── worlds/
```

## Status

This repository is primarily a technical/portfolio project and is not presented as a drop-in production dependency.

## License

Proprietary — all rights reserved © Clickdroit.
