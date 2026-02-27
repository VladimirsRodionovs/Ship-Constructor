# ShipConstructor

Local engineering tool for ship module design and chassis pre-calculation.

The app provides a JavaFX module designer, validates structured payload fields, stores module definitions in MySQL (`ShipModules`), and supports local JSON drafts.

## Highlights
- Module-card editing workflow with validation.
- Insert/update persistence with `ModuleID` support.
- Chassis graph and pre-solver diagnostics.
- Engineering coefficient-driven calculation model.

## Tech Stack
- Java 17
- Maven
- JavaFX 21
- Jackson
- MySQL Connector/J

## Quick Start
1. Configure DB credentials in `.env` (or `project.env`).
2. Build:
   - `mvn -q -DskipTests compile`
3. Run:
   - `mvn -q javafx:run`

## Documentation
- Internal design: `ARCHITECTURE.md`
- Engineering taxonomy: `docs/ENGINEERING_REFERENCE.md`
- Chassis model: `docs/CHASSIS_CALCULATION_MODEL.md`

## Contact
vladimirs.rodionovs@gmail.com
