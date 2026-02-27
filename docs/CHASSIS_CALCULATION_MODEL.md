# Chassis Calculation Model (Draft v0)

## Goal

Formal model for ship chassis/frame calculation:
- physically constrained
- scalable across tech levels
- supports "ideal limit -> degraded real limit" logic
- compatible with the engineering reference layers (`A-F`)

This document defines:
1. user inputs
2. intermediate values
3. coefficient groups (A-F)
4. hard physical ceilings/floors
5. calculation stages

## 1. User Inputs (minimum)

### Geometry / scale
- `sizeType` (required): base cube size class
- `sizeTotal` (required): total count of occupied cubes
- `sizeDimensions` (required): `{x,y,z}` cube dimensions (bounding dimensions in cubes)

### Material / engineering stack
- `baseMaterialId` (A)
- `structureTypeId` (B)
- `structureProfileCode` (optional, e.g. `STR_ISOGRID_SHELL`) for direct structure behavior selection; when present it should take precedence over coarse `structureTypeId` aliases in pre-solver heuristics
- `manufacturingProcessId` (C)
- `assemblyProcessId` (D)
- `qualityProfileId` (E)
- `environmentProfileId` (F)

### Mission / load design (legacy simple mode)
- `targetAccelerationLongitudinalG`
- `targetAccelerationLateralG`
- `targetAccelerationVerticalG`
- `missionPriority` (`mass_efficiency`, `balanced`, `survivability`, `serviceability`)
- `safetyPriority` (`economy`, `balanced`, `high_margin`)

### Structural design basis / load cases (preferred mode)
- `structuralDesignBasis`
  - mission/design class (e.g. `space tug`, `space-only cruiser`, `landing-capable`)
  - capability flags (`spaceOnly`, `atmosphericCapable`, `landingCapable`)
  - thrust arrangement assumptions (e.g. fore-mounted traction allowed)
  - target service life cycles
- `capabilityClassSet` (inside `structuralDesignBasis`)
  - operational suitability classes used to auto-generate/augment structural load cases
  - v1 (used by chassis):
    - `planetLandingCapabilityClass`
    - `groundSupportSurfaceClass`
    - `maneuverAgilityClass`
    - `structuralDamageToleranceClass`
  - v1 (stored, mostly casing-side for now):
    - `atmosphereOperationClass`
    - `pressureDifferentialClass`
    - `thermalEnvironmentClass`
    - `corrosionChemicalExposureClass`
    - `toxicityBiohazardExposureClass`
    - `radiationExposureClass`
- `loadCaseSet[]`
  - multiple named cases (acceleration vectors, duration, occurrence weight)
  - `certificationCritical` flag
  - `primaryLoadMode` (compression / tension / bending / mixed; diagnostic for now)
- `thrustLayout`
  - one or more thrust centers/nodes (position, direction, max thrust)
- `massDistributionModel`
  - center of mass states (`dry`, `loaded`, `fueled`)
  - major mass nodes (reactor, tanks, cargo blocks, etc.)

### Optional future inputs
- `casingMaterial` / `armorMaterial`
- `expectedServiceLifeHours`
- `thermalExposureClass`
- `pressurizedHull` flag
- `damageToleranceRequirement`

## 2. Intermediate Values (derived automatically)

### Geometry (from size model)
- `cubeEdgeM`
- `dimXcubes`, `dimYcubes`, `dimZcubes`
- `lengthM`, `widthM`, `heightM`
- `boundingVolumeM3 = length * width * height`
- `occupiedVolumeM3 = sizeTotal * cubeEdgeM^3`
- `packingFillRatio = occupiedVolumeM3 / boundingVolumeM3`

### Shape / scaling indicators
- `slendernessLengthToWidth`
- `slendernessLengthToHeight`
- `surfaceAreaApproxM2`
- `characteristicSpanM`
- `scaleClass` (small/medium/large/mega for internal rules)

### Load envelope (requested / derived)
- `effectiveAccelerationEnvelope` (from `loadCaseSet` critical cases, or direct acceleration input)
- `maxDesignAccelerationG`
- `equivalentLoadSeverityIndex` (aggregated from 3 axes + mission profile)
- `dynamicLoadAmplificationFactor` (mission/use dependent)
- future:
  - `compressionVsTensionBias`
  - `thrustLineOffsetMomentIndex`
  - `torsionDemandIndex`

### Material baseline envelope (A)
- `materialDensity`
- `materialStrengthEnvelope` (tension/compression/shear)
- `materialStiffnessEnvelope`
- `materialFatigueEnvelope`
- `materialTemperatureEnvelope`

### Structure modifiers (B)
- `effectiveDensityFactor`
- `specificStrengthFactor`
- `specificStiffnessFactor`
- `bucklingResistanceFactor`
- `damageToleranceFactor`
- `inspectionComplexityFactor`

### Process/assembly/QA/environment modifiers (C/D/E/F)
- `manufacturingQualityFactor`
- `manufacturingDefectPenalty`
- `assemblyJointEfficiency`
- `assemblyStressConcentrationPenalty`
- `qualityConfidenceFactor`
- `qualityAllowedMarginReductionFactor`
- `environmentDegradationFactor`
- `environmentFatigueAccelerationFactor`

## 3. Coefficient Groups (A-F participation)

### A. BaseMaterial (physical baseline)
Contributes to:
- density
- strength envelope
- stiffness envelope
- fatigue baseline
- thermal/corrosion/radiation resistance

Role in model:
- defines *ideal physics-constrained baseline* material behavior

### B. StructureType (geometry architecture)
Contributes to:
- effective density (mass efficiency)
- buckling behavior
- stiffness distribution
- damage tolerance
- joint count tendency / inspection complexity

Role in model:
- modifies how well material properties are realized structurally

### C. ManufacturingProcess (part quality realization)
Contributes to:
- microstructure control
- defect rate
- repeatability
- residual stress control
- large-part capability

Role in model:
- reduces gap between ideal material+structure and real manufactured part

### D. AssemblyProcess (joint realization)
Contributes to:
- joint efficiency
- stress concentration penalty
- joint mass penalty
- alignment precision
- repairability/disassembly

Role in model:
- critical real-world limiter for full chassis performance

### E. QualityProfile (uncertainty control)
Contributes to:
- confidence in actual structure quality
- detectability of defects
- allowable reduction of conservative margin
- lifecycle prediction confidence

Role in model:
- governs how close design may operate to calculated real limit

### F. EnvironmentProfile (mission degradation)
Contributes to:
- material degradation
- fatigue acceleration
- inspection difficulty
- repair difficulty

Role in model:
- shifts usable limits downward for actual service conditions

## 4. Hard Physical Ceilings / Floors (must not be violated)

### Hard ceilings
1. `RealLimit <= IdealLimit`
2. `PracticalEfficiency <= PracticalEfficiencyCap`
3. `PracticalEfficiencyCap <= TheoreticalEfficiencyCap`
4. `JointEfficiency <= 1.0`
5. No coefficient may create "free strength" beyond the ideal material+structure limit

### Hard floors
1. `safetyFactor >= safetyFloor`
2. `frameMassFraction >= frameMassFractionFloor`
3. `bucklingReserve >= bucklingReserveFloor`
4. `inspectionConfidence >= minimumAllowedConfidence` for mission-critical classes

### Physical impossibility checks
- `sizeTotal > x*y*z` -> invalid
- non-positive dimensions / cube size -> invalid
- negative masses / capacities -> invalid
- accelerations beyond model upper support range -> reject or flag "out of calibrated range"

## 5. Calculation Stages (v0 sequence)

### Stage 0: Validate inputs
- required IDs present
- geometry is physically consistent
- accelerations within supported range
- at least one load source exists (`accelerationEnvelope` or non-empty `loadCaseSet`)

### Stage 1: Derive geometry metrics
Compute:
- actual dimensions in meters
- volumes
- slenderness and scale indicators
- packing ratio

### Stage 1.5: Build effective load envelope
Use:
- `loadCaseSet` if present (prefer `certificationCritical` cases)
- capability-derived synthetic load cases (landing / agility / damage tolerance / ground support)
- otherwise direct acceleration envelope

Outputs:
- `effectiveLongitudinalG`
- `effectiveLateralG`
- `effectiveVerticalG`
- trace record of selected load cases
- trace record of generated capability load cases (`generatedCapabilityLoadCaseCount`)

### Stage 1.6: Build draft `StructuralGraph` (pre-solver bridge)
Purpose:
- create a coarse spatial representation of the chassis before full structural solving
- preserve geometry, engine placement, landing points, and mass anchor locations
- produce a future-ready target for applying distributed/node loads

Outputs (new graph layer):
- `StructuralGraph.nodes[]` (frame corners, station centers, engine mounts, thrust points, mass anchors, landing points)
- `StructuralGraph.members[]` (longitudinal rails, rings, braces, spine, engine supports, landing supports)
- `StructuralGraph.panels[]` (top/bottom/side panels, bulkheads)

Inputs used:
- `sizeType`, `sizeDimensions` -> envelope size + station spacing
- `enginePlacementProfileClass` and/or `thrustLayout` -> engine/thrust node placement
- `massDistributionModel` -> mass anchor placement
- `capabilityClassSet.planetLandingCapabilityClass` -> landing load point generation

Notes:
- this is intentionally not an FEA/solver stage yet
- graph is a coarse design scaffold for future internal force and deformation calculations

### Stage 1.7: Build structural load application list (pre-solver)
Purpose:
- translate `loadCaseSet` + capability-generated cases into graph-attachable loads
- keep load provenance and future solver metadata

Outputs:
- `StructuralGraphLoadSet.loads[]`
  - whole-graph inertial loads
  - thrust application loads on thrust nodes / thrust node sets
  - landing reactions on landing load points
  - ground support uneven reactions
  - certification reserve/differential-control loads (as applicable)

Notes:
- current output is a planning/application list, not a solved response
- exact load distribution across members/panels will be a later solver stage

### Stage 1.8: Run coarse member axial-envelope pre-solver (diagnostic)
Purpose:
- estimate which structural members are likely to become critical before full FEA
- identify dominant load paths and local overload hotspots
- support early chassis architecture decisions (`engine placement`, landing support layout, mass anchoring)

Current output:
- per-member diagnostic envelope (`tension/compression/combined abs index`)
- governing load case name/mode per member
- top-loaded member list for review/debugging

Important:
- this is not a true force-balance solver yet
- it is a role-aware load-path heuristic to guide architecture and calibration

## 5.1 Primary / Secondary Structural Graph Elements (current v1 rule set)

### Members (current classification)
- `PRIMARY_LOAD_PATH`
  - `LONGITUDINAL_RAIL`
  - `TRANSVERSE_RING`
  - `CENTER_SPINE`
- `SECONDARY_STIFFENING`
  - `DIAGONAL_BRACE`
- `LOCAL_LOAD_TRANSFER`
  - `ENGINE_SUPPORT`
  - `LANDING_SUPPORT`
  - `MASS_SUPPORT`

Rationale:
- primary members carry global hull/frame continuity and major axial load paths
- secondary members improve stiffness, buckling resistance, and load redistribution
- local transfer members inject concentrated loads (engines/landing/mass nodes) into the primary frame

### Panels (current classification)
- `PRIMARY_SHEAR_CLOSURE`
  - `BULKHEAD`
  - `GENERIC_SHEAR_PANEL`
- `SECONDARY_CLOSURE`
  - `TOP_PANEL`
  - `BOTTOM_PANEL`
  - `PORT_PANEL`
  - `STARBOARD_PANEL`
- `LOCAL_PARTITION`
  - `DECK_PANEL` (current placeholder classification)

Note:
- panel roles are placeholders for future shell/shear participation modeling
- some ship classes may later promote outer panels to primary shear structure

### Stage 2: Build ideal structural limit (physics-side)
Use:
- A (material)
- B (structure)
- geometry
- effective load envelope

Outputs:
- `idealMaxSupportedMassKg`
- `idealRecommendedFrameMassKg`
- `idealBucklingReserveIndex`
- `idealStiffnessReserveIndex`

Notes:
- This is a best-case bound assuming near-perfect realization and no extra degradation from C/D/E/F.
- Current model still compresses load-case physics into an equivalent acceleration envelope.
- Current model may auto-generate capability-driven load cases before compressing them into an equivalent envelope.
- Future versions should resolve axial compression/tension, bending, and torsion per load case using `thrustLayout + massDistributionModel`.

### Stage 3: Apply manufacturing and assembly realization penalties
Use:
- C (manufacturing)
- D (assembly)
- geometry/scale penalties

Outputs:
- `realizationFactor`
- `realMaxSupportedMassBeforeQAEnvKg`
- `jointPenaltyMassKg`
- `stressConcentrationPenalty`

### Stage 4: Apply QA confidence and operational conservatism
Use:
- E (quality profile)
- mission/safety priority

Outputs:
- `qualityConfidenceAdjustedLimitKg`
- `appliedSafetyFactor`
- `operationalMarginKg`

### Stage 5: Apply environment degradation / mission service penalties
Use:
- F (environment)
- expected load severity

Outputs:
- `finalOperationalMaxSupportedMassKg`
- `fatigueAdjustedLimitKg`
- `serviceLifeRiskIndex`

### Stage 6: Derive budgets for ship design
Outputs:
- `recommendedFrameMassKg`
- `maxCasingArmorMassKg`
- `maxModulesMassKg`
- `massReserveKg`
- warnings / constraints

## 6. Output Groups (for ship constructor integration)

### Primary results
- `finalOperationalMaxSupportedMassKg`
- `recommendedFrameMassKg`
- `recommendedTotalMassTargetKg`
- `frameMassFraction`
- `appliedSafetyFactor`

### Engineering diagnostics
- `idealMaxSupportedMassKg`
- `realizationFactor`
- `qualityConfidenceFactor`
- `environmentDegradationFactor`
- `bucklingReserveIndex`
- `jointEfficiencyComposite`

### Warnings
Examples:
- size/shape inefficient for chosen structure type
- assembly process is weak for selected materials
- environment heavily degrades chosen matrix/material
- requested acceleration too aggressive for quality level

## 7. Formula Strategy (v0 implementation approach)

Do not implement a single opaque formula.
Implement staged functions:
1. geometry metrics
2. ideal limit estimate
3. realization penalties
4. safety/QA adjustment
5. environment adjustment
6. budget derivation

This keeps the model explainable and debuggable.

## 8. Calibration Notes (future)

The model must be calibrated, not guessed blindly:
- start with internally consistent synthetic reference points
- compare classes (steel frame vs titanium frame vs CFRP shell, etc.)
- enforce monotonic behaviors:
  - more acceleration -> lower supported mass
  - larger scale -> stronger buckling penalties if structure/process is weak
  - better QA -> lower uncertainty, not magically stronger materials
  - harsher environment -> lower service limits
