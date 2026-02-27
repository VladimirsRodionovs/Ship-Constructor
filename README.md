# ShipConstructor

Local engineering tool for ship module design and chassis pre-calculation.

The app provides a JavaFX module designer with JSON-backed fields, engineering catalogs (A-F stack), structural graph preview, and DB persistence to `ShipModules`.

## What it does

- edits module cards (general, energy, fuel, lines, storage, JSON blocks);
- validates and previews payload before save;
- saves/updates module definitions in MySQL (`ShipModules`);
- auto-selects next free `ModuleID`;
- runs chassis graph build/load planning/axial envelope pre-solver for diagnostics;
- supports local JSON save/load for module drafts.

## Entry point

- `org.example.shipconstructor.ui.ModuleDesignerLauncher`

## Tech stack

- Java 17
- Maven
- JavaFX 21
- Jackson
- MySQL Connector/J

## Repository structure

- `src/main/java/org/example/shipconstructor/ui/` - JavaFX UI and form workflows.
- `src/main/java/org/example/shipconstructor/db/` - env loader + JDBC repository.
- `src/main/java/org/example/shipconstructor/chassis/` - engineering/chassis model and solvers.
- `docs/ENGINEERING_REFERENCE.md` - engineering taxonomy (materials/process/quality).
- `docs/CHASSIS_CALCULATION_MODEL.md` - calculation model and load stages.
- `data/reference/chassis_coefficients.sample.json` - sample coefficient library.

## Local DB config

The app reads credentials from `.env` (preferred) or `project.env` in project root.

Minimal `.env` example:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=EXOLOG
DB_USER=YOUR_DB_USER
DB_PASSWORD=YOUR_DB_PASSWORD
```

Alternative full URL:

```env
DB_URL=jdbc:mysql://127.0.0.1:3306/EXOLOG?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=YOUR_DB_USER
DB_PASSWORD=YOUR_DB_PASSWORD
```

## Build

```bash
cd /home/vladimirs/ShipConstructor/ShipConstrucor
mvn -q -DskipTests compile
```

## Run

```bash
mvn -q javafx:run
```

## Key capabilities in UI

- New / Duplicate / Clear workflows;
- Validate JSON fields;
- Save Local / Load Local;
- Save to DB (insert/update by `ModuleID`);
- Chassis graph preview and solver diagnostics.

## Engineering model

Chassis output is influenced by layered stack:

`A BaseMaterial -> B StructureType -> C ManufacturingProcess -> D AssemblyProcess -> E QualityProfile -> F EnvironmentProfile`

See details and templates in:
- `docs/ENGINEERING_REFERENCE.md`
- `docs/CHASSIS_CALCULATION_MODEL.md`

## Notes

- This is a tool project, not a runtime game server component.
- Keep real credentials only in local env files, never in tracked docs/code.
