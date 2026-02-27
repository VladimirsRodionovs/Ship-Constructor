# ARCHITECTURE — ShipConstructor

## Purpose
`ShipConstrucor` is a local engineering tool for ship-module design and chassis pre-calculation. It is a JavaFX desktop app with MySQL persistence and local JSON draft support.

## Tech stack
- Java 17
- Maven
- JavaFX 21
- Jackson
- MySQL Connector/J

## High-level flow
1. User edits module card in UI.
2. UI validates fields and JSON blocks.
3. App builds module domain model.
4. Data is saved to `ShipModules` (insert/update, next free `ModuleID`).
5. Chassis graph/pre-solver can run for diagnostics.
6. Draft can be saved/loaded as local JSON.

## Layers
### 1) UI layer
- Package: `org.example.shipconstructor.ui`
- Entry point: `ModuleDesignerLauncher`
- Responsibility: form workflow, New/Duplicate/Clear actions, validation, service calls.

### 2) Application/service layer
- Package: `org.example.shipconstructor.service`
- Responsibility: orchestration between UI, domain, DB, and local IO.

### 3) Domain model
- Package: `org.example.shipconstructor.domain`
- Responsibility: ship-module business entities.

### 4) Chassis subsystem
- Package: `org.example.shipconstructor.chassis`
- Subpackages:
  - `domain` — calculation entities;
  - `graph` — structural graph construction;
  - `service` — solver/diagnostic services;
  - `ui` — chassis-related UI;
  - `demo` — demo scenarios.
- Responsibility: load planning, axial envelope checks, and diagnostic metrics.

### 5) Persistence layer
- Package: `org.example.shipconstructor.db`
- Responsibility:
  - read env config (`.env` / `project.env`);
  - JDBC MySQL access;
  - read/write `ShipModules` records.

## Configuration
- DB config priority: `.env` first, `project.env` fallback.
- Supports `DB_HOST/PORT/NAME` split settings or full `DB_URL`.

## Contracts and integrations
- Main table: `ShipModules` (MySQL `EXOLOG`).
- Engineering docs:
  - `docs/ENGINEERING_REFERENCE.md`
  - `docs/CHASSIS_CALCULATION_MODEL.md`
- Coefficients sample:
  - `data/reference/chassis_coefficients.sample.json`

## Extension points
- New module attributes: extend `domain` + `ui` + DB mapping.
- New chassis stages: extend `chassis/service` + `chassis/domain`.
- New exchange formats: extend JSON IO in service layer.

## Risks
- UI and application logic may become tightly coupled as form grows.
- JSON preview and DB schema can drift without coordinated changes.
- Coefficient model changes require solver recalibration.

## Quick navigation
- Entry point: `src/main/java/org/example/shipconstructor/ui/ModuleDesignerLauncher.java`
- UI: `src/main/java/org/example/shipconstructor/ui/`
- Chassis: `src/main/java/org/example/shipconstructor/chassis/`
- DB: `src/main/java/org/example/shipconstructor/db/`
- Domain: `src/main/java/org/example/shipconstructor/domain/`
