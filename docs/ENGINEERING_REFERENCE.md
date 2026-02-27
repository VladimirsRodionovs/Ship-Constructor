# Engineering Reference (Materials & Technologies)

## Purpose

Reference document for ship construction engineering in the game:
- materials
- structural architectures
- manufacturing processes
- assembly processes
- quality/verification profiles

Scope:
- hard sci-fi only
- physically plausible engineering
- expandable with high-tech civilizations
- no "magic materials" without tradeoffs

This file is a structured reference and taxonomy. It is intended to be filled and extended over time.

## Core Principles

1. No fake engineering ("fuflo"): every entry must state benefits, limits, and tradeoffs.
2. No physics-breaking claims: improvements can approach physical limits, not ignore them.
3. Material != structure != process != assembly != quality.
4. Final structural performance is derived from the combination of all layers.
5. Bigger is not automatically better: scaling effects, buckling, joints, and inspection complexity must be considered.

## Rating Scales

### TechMaturityLevel (progression / "coolness" hierarchy)

- `1` - Primitive / Early Industrial
- `2` - Industrial
- `3` - Advanced Industrial
- `4` - High Performance
- `5` - Frontier Human
- `6` - Post-Human Industrial
- `7` - Ultra-Advanced (hard sci-fi, physical)
- `8` - Extreme Precision Civilization-grade
- `9` - Near-Theoretical Engineering

### PhysicsConfidenceLevel (reversed: lower = more confirmed)

- `1` - industrially established
- `2` - known principles / demonstrated in lab
- `3` - research-grounded extrapolation
- `4` - speculative but plausible

## Anti-Fuflo Validation Rules (for every entry)

Each entry must include:
- what it improves
- what it makes worse (cost/repair/fragility/complexity/etc.)
- operating limits (temperature, scale, shock, radiation, etc. when relevant)
- likely failure modes
- approximate tech maturity (`TechMaturityLevel`)
- physics confidence (`PhysicsConfidenceLevel`)
- notes on real-world basis or scientific plausibility

Entries that only provide "bonuses" without limitations are invalid.

## Layer Model (how final structural performance is formed)

Final chassis/frame performance is derived from:
1. `BaseMaterial`
2. `StructureType`
3. `ManufacturingProcess`
4. `AssemblyProcess`
5. `QualityProfile`
6. `EnvironmentProfile` (selected per mission/usage)

Optional later layer:
7. `CivilizationTechProfile` (not in current scope)

## Record Templates

### Template: BaseMaterial

```md
#### [BaseMaterialID] MaterialName

- Code:
- Category:
- SubCategory:
- Summary:
- TechMaturityLevel:
- PhysicsConfidenceLevel:
- Typical Use:
- Not Recommended For:

Physical / Mechanical:
- DensityKgM3:
- YieldStrengthMPa:
- UltimateStrengthMPa:
- CompressiveStrengthMPa:
- TensileStrengthMPa:
- ShearStrengthMPa:
- FatigueLimitMPa:
- ElasticModulusGPa:
- FractureToughnessMPaSqrtM:

Thermal / Environmental:
- MaxServiceTempC:
- MinServiceTempC:
- ThermalExpansionCoeff:
- ThermalConductivityWmK:
- RadiationResistanceIndex (0..100):
- CorrosionResistanceIndex (0..100):
- CryoPerformanceIndex (0..100):

Engineering Factors:
- ManufacturabilityIndex (0..100):
- RepairabilityIndex (0..100):
- StrategicRarityIndex (0..100):
- BaseCostIndex:

Tradeoffs:
- Pros:
- Cons:
- Failure Modes:

Plausibility Notes:
- Real-world basis / lab basis:
- Extrapolation notes:
```

### Template: StructureType

```md
#### [StructureTypeID] StructureName

- Code:
- StructureClass:
- Summary:
- TechMaturityLevel:
- PhysicsConfidenceLevel:
- Typical Use:
- Scale Guidance (min/max):

Relative Factors (vs monolithic baseline):
- EffectiveDensityFactor:
- SpecificStiffnessFactor:
- SpecificStrengthFactor:
- BucklingResistanceFactor:
- ImpactToleranceFactor:
- DamageToleranceFactor:
- FatigueBehaviorFactor:
- ThermalGradientToleranceFactor:
- VibrationDampingFactor:

Complexity / Lifecycle:
- ManufacturingComplexityFactor:
- InspectionComplexityFactor:
- RepairComplexityFactor:
- JointIntegrationDifficultyFactor:

Applicability:
- PressureVesselSuitabilityIndex (0..100):
- VacuumSuitabilityIndex (0..100):
- HighShockSuitabilityIndex (0..100):
- Notes on geometric constraints:

Tradeoffs:
- Pros:
- Cons:
- Failure Modes:

Plausibility Notes:
- Real-world basis / lab basis:
- Extrapolation notes:
```

### Template: ManufacturingProcess

```md
#### [ManufacturingProcessID] ProcessName

- Code:
- ProcessCategory:
- Summary:
- TechMaturityLevel:
- PhysicsConfidenceLevel:
- Typical Compatible Materials:
- Typical Compatible Structures:

Process Quality Factors:
- PrecisionFactor:
- RepeatabilityFactor:
- DefectRateFactor:
- MaterialPropertyRetentionFactor:
- ResidualStressControlFactor:
- MicrostructureControlFactor:
- SurfaceQualityFactor:
- LargePartCapabilityFactor:
- ComplexGeometryCapabilityFactor:
- ThroughputFactor:
- EnergyIntensityFactor:
- CostFactor:

Constraints:
- MinTechLevelRequired:
- MaxPartSizeMeters:
- Vacuum/In-space capability:
- Nano-structuring capability:

Tradeoffs:
- Pros:
- Cons:
- Main Defect Risks:

Plausibility Notes:
- Real-world basis / lab basis:
- Extrapolation notes:
```

### Template: AssemblyProcess

```md
#### [AssemblyProcessID] AssemblyName

- Code:
- AssemblyCategory:
- Summary:
- TechMaturityLevel:
- PhysicsConfidenceLevel:
- Typical Compatible Structures:
- Typical Compatible Manufacturing Processes:

Assembly Quality Factors:
- JointEfficiencyFactor:
- AlignmentPrecisionFactor:
- StressConcentrationFactor:
- FatigueJointFactor:
- ThermalMismatchPenaltyFactor:
- JointMassPenaltyFactor:
- SealIntegrityFactor:
- FieldRepairabilityFactor:
- DisassemblyAbilityFactor:
- InspectionAccessibilityFactor:
- AssemblySpeedFactor:
- AssemblyCostFactor:

Constraints:
- MinTechLevelRequired:
- MaxAssemblyScaleMeters:
- Mixed-material support:
- Pressure-hull support:
- Vacuum/In-space assembly capability:

Tradeoffs:
- Pros:
- Cons:
- Failure Modes:

Plausibility Notes:
- Real-world basis / lab basis:
- Extrapolation notes:
```

### Template: QualityProfile

```md
#### [QualityProfileID] QualityName

- Code:
- Summary:
- TechMaturityLevel:
- PhysicsConfidenceLevel:

Inspection / Verification:
- InspectionCoverageFactor:
- DefectDetectionThresholdMicrons:
- DefectCharacterizationFactor:
- ProcessTraceabilityFactor:
- ConfidenceLevelFactor:
- ResidualStressMappingFactor:
- MicrostructureVerificationFactor:
- JointVerificationFactor:
- LifecyclePredictionFactor:

Operational Impact:
- SafetyMarginReductionAllowedFactor:
- UnexpectedFailureRiskFactor:
- MaintenancePlanningBonusFactor:

Capabilities:
- Full-volume tomography:
- In-situ monitoring:
- Per-part digital twin:
- Full defect cartography:

Tradeoffs:
- Pros:
- Cons:
- Cost / time burden:

Plausibility Notes:
- Real-world basis / lab basis:
- Extrapolation notes:
```

### Template: EnvironmentProfile

```md
#### [EnvironmentProfileID] EnvironmentName

- Code:
- Summary:
- Typical Mission Context:

Conditions:
- VacuumLevelClass:
- AtmosphereType:
- PressureRange:
- TemperatureRange:
- ThermalCycleSeverityIndex:
- RadiationFluxIndex:
- ParticleErosionIndex:
- DustAbrasiveIndex:
- CorrosionChemicalIndex:
- ShockLoadEnvironmentIndex:
- VibrationSeverityIndex:

Derived Penalties / Multipliers:
- MaterialDegradationFactor:
- FatigueAccelerationFactor:
- InspectionDifficultyFactor:
- RepairDifficultyFactor:

Notes:
- Dominant risks:
- Which material/structure classes suffer most:
```

## Catalog Sections (to fill next)

### A. Base Materials (Extended)

Planned scope:
- conventional metals
- high-performance alloys
- refractory alloys
- ceramics and cermets
- composites (polymer, metal, ceramic matrix)
- bio-based structural materials
- nanostructured / hard-sci-fi advanced materials

#### A1. Conventional & Industrial Metals
- Structural Steel
- HSLA Steel (High-Strength Low-Alloy)
- Stainless Steel (Austenitic/General Structural)
- Cast Steel (Structural Grade)
- Aluminum Alloy (General Structural)
- Aluminum-Lithium Alloy
- Magnesium Alloy (Structural, restricted use)
- Copper Alloy (Structural/Thermal hybrid roles)
- Engineered Timber (structural grade)

#### A2. High-Performance & Aerospace Alloys
- Titanium Alloy (general aerospace structural)
- Beta Titanium Alloy
- Maraging Steel
- Nickel Superalloy (structural hot-zone class)
- Cobalt Superalloy (specialized)
- Zirconium Alloy (specialized structural/environmental)
- Beryllium Alloy (restricted, hazardous, high specific stiffness)
- Titanium Aluminide (intermetallic)
- Nickel Aluminide (intermetallic)

#### A1-A2 Detailed Entries (first pass)

#### [A1-01] Structural Steel
- Code: `MAT_STEEL_STRUCT`
- Category: Metal
- SubCategory: Carbon/low-alloy structural steel
- Summary: Baseline structural material for frames, brackets, pressure-capable support members, and cost-sensitive hull structures.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Typical Use: Bulk framing, industrial ships, docks, support trusses, low-cost chassis.
- Not Recommended For: Extreme mass-efficiency designs, high-temperature zones, advanced high-G ships where mass matters strongly.

Physical / Mechanical (typical range, game baseline):
- DensityKgM3: `7700-7850`
- YieldStrengthMPa: `250-550`
- UltimateStrengthMPa: `400-700`
- CompressiveStrengthMPa: `~yield-class dependent`
- ShearStrengthMPa: `moderate`
- FatigueLimitMPa: `moderate`
- ElasticModulusGPa: `~200`
- FractureToughnessMPaSqrtM: `good`

Thermal / Environmental:
- MaxServiceTempC: `moderate`
- CorrosionResistanceIndex: `30` (without protection)
- RadiationResistanceIndex: `70`
- CryoPerformanceIndex: `60` (depends on alloy/toughness control)

Engineering Factors:
- ManufacturabilityIndex: `90`
- RepairabilityIndex: `90`
- StrategicRarityIndex: `10`
- BaseCostIndex: `1.0`

Tradeoffs:
- Pros: Cheap, available, easy to weld/machine, forgiving, good toughness in many grades.
- Cons: Heavy, corrosion-prone, mediocre specific strength.
- Failure Modes: Corrosion thinning, fatigue cracking at welds, buckling if thin sections are over-optimized.

Plausibility Notes:
- Real-world basis / lab basis: Industrially ubiquitous.
- Extrapolation notes: None required.

#### [A1-02] HSLA Steel (High-Strength Low-Alloy)
- Code: `MAT_STEEL_HSLA`
- Category: Metal
- SubCategory: HSLA steel
- Summary: Improved structural steel with better strength-to-weight and toughness than baseline steel, good for rugged chassis and pressure structures.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Cargo ships, military hull frames, heavy-duty chassis, structural members needing durability.
- Not Recommended For: Ultra-light spacecraft frames where titanium/composites dominate.

Physical / Mechanical:
- DensityKgM3: `~7800`
- YieldStrengthMPa: `350-900` (grade dependent)
- UltimateStrengthMPa: `500-1000+`
- FatigueLimitMPa: `moderate-good`
- ElasticModulusGPa: `~200`
- FractureToughnessMPaSqrtM: `good`

Thermal / Environmental:
- MaxServiceTempC: `moderate`
- CorrosionResistanceIndex: `40` (coatings still needed)
- RadiationResistanceIndex: `70`
- CryoPerformanceIndex: `65`

Engineering Factors:
- ManufacturabilityIndex: `80`
- RepairabilityIndex: `80`
- StrategicRarityIndex: `15`
- BaseCostIndex: `1.4`

Tradeoffs:
- Pros: Stronger than baseline steel at similar stiffness, robust, scalable for large structures.
- Cons: Still heavy, welding/process quality affects final performance strongly.
- Failure Modes: Heat-affected zone degradation, weld fatigue, brittle behavior if wrong grade/process chosen.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Can scale with better quality control and alloy cleanliness.

#### [A1-03] Stainless Steel (Austenitic/General Structural)
- Code: `MAT_STEEL_STAINLESS_STRUCT`
- Category: Metal
- SubCategory: Stainless steel
- Summary: Corrosion-resistant structural metal used where environmental durability and maintainability are more important than mass efficiency.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Corrosive environments, atmospheric-capable ships, chemical handling modules, maintenance-heavy vessels.
- Not Recommended For: Mass-critical acceleration-focused frames.

Physical / Mechanical:
- DensityKgM3: `~7900-8000`
- YieldStrengthMPa: `200-600` (grade dependent)
- UltimateStrengthMPa: `500-900`
- ElasticModulusGPa: `~190-200`
- FatigueLimitMPa: `moderate`

Thermal / Environmental:
- CorrosionResistanceIndex: `80`
- RadiationResistanceIndex: `70`
- CryoPerformanceIndex: `70` (grade dependent)
- MaxServiceTempC: `moderate`

Engineering Factors:
- ManufacturabilityIndex: `70`
- RepairabilityIndex: `75`
- StrategicRarityIndex: `20`
- BaseCostIndex: `2.0`

Tradeoffs:
- Pros: Strong corrosion resistance, durable in dirty service, good lifecycle maintenance profile.
- Cons: Heavy, costlier than carbon/HSLA steels, some grades harder to machine.
- Failure Modes: Chloride stress corrosion cracking (grade/environment dependent), weld sensitization if mishandled.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Advanced grades can improve cryo/corrosion behavior further.

#### [A1-04] Cast Steel (Structural Grade)
- Code: `MAT_STEEL_CAST_STRUCT`
- Category: Metal
- SubCategory: Cast steel
- Summary: Good for large or complex industrial structural components where forging/machining is expensive.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Typical Use: Heavy nodes, large brackets, support castings, non-optimized industrial hull structures.
- Not Recommended For: Peak-performance lightweight frames, high-cycle fatigue-critical members without strict QA.

Tradeoffs:
- Pros: Complex shapes, scalable industrial production, cost-effective for large parts.
- Cons: More defect-sensitive than forged/wrought routes, lower confidence without strong QA.
- Failure Modes: Porosity defects, inclusions, casting shrinkage cracks, fatigue initiation at defects.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Performance improves significantly when paired with advanced QA/HIP-like refinement.

#### [A1-05] Aluminum Alloy (General Structural)
- Code: `MAT_AL_STRUCT`
- Category: Metal
- SubCategory: Aluminum alloy
- Summary: Light structural metal with strong manufacturability advantages; good for medium-performance hulls and modules where mass matters.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Small/medium spacecraft, secondary frames, atmospheric craft, modular hull sections.
- Not Recommended For: Extreme hot zones, abrasion-heavy exposed structures, some radiation/impact-sensitive roles.

Physical / Mechanical:
- DensityKgM3: `~2600-2800`
- YieldStrengthMPa: `150-600` (alloy/temper dependent)
- ElasticModulusGPa: `~70`
- FatigueLimitMPa: `low-moderate` (alloy dependent)

Thermal / Environmental:
- CorrosionResistanceIndex: `60`
- RadiationResistanceIndex: `55`
- CryoPerformanceIndex: `75`
- MaxServiceTempC: `lower than steels/titanium`

Engineering Factors:
- ManufacturabilityIndex: `85`
- RepairabilityIndex: `70`
- StrategicRarityIndex: `20`
- BaseCostIndex: `1.6`

Tradeoffs:
- Pros: Low density, easy forming/machining, good availability, excellent for many hull/module applications.
- Cons: Lower stiffness means thicker sections needed, heat sensitivity, fatigue design must be careful.
- Failure Modes: Fatigue crack growth, thermal softening, joint issues from poor welding/galvanic pairing.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Advanced alloys/process control can improve defects and weld quality.

#### [A1-06] Aluminum-Lithium Alloy
- Code: `MAT_AL_LI`
- Category: Metal
- SubCategory: Al-Li aerospace alloy
- Summary: Aerospace-class aluminum family with improved specific stiffness and reduced density over common Al alloys.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Mass-sensitive hull panels, aerospace structures, advanced transport ships.
- Not Recommended For: Cheap mass-production fleets without process discipline.

Tradeoffs:
- Pros: Better specific performance than standard Al, useful for lightweight large panels.
- Cons: Costlier, processing and damage tolerance considerations, tighter manufacturing control needed.
- Failure Modes: Anisotropic cracking behavior, fatigue and fracture sensitivity if process/temper is wrong.

Plausibility Notes:
- Real-world basis / lab basis: Aerospace-established.
- Extrapolation notes: High-end quality regimes can extract more consistency.

#### [A1-07] Magnesium Alloy (Structural, restricted use)
- Code: `MAT_MG_STRUCT`
- Category: Metal
- SubCategory: Magnesium alloy
- Summary: Very low-density metal for selective use in non-critical or carefully protected structures.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Typical Use: Weight-critical secondary structures, interior frames, special modules with strict fire/environment control.
- Not Recommended For: High-temperature zones, abrasion/corrosion heavy environments, primary damage-prone chassis.

Tradeoffs:
- Pros: Very low density, good machinability in some routes.
- Cons: Corrosion/reactivity concerns, lower stiffness, fire/process handling complexity.
- Failure Modes: Corrosion-assisted cracking, ignition risk in processing, impact damage.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established in limited structural roles.
- Extrapolation notes: Advanced coatings/assembly/QA can expand safe use envelope.

#### [A1-08] Copper Alloy (Structural/Thermal hybrid roles)
- Code: `MAT_CU_STRUCT_THERM`
- Category: Metal
- SubCategory: Copper alloy
- Summary: Usually not primary chassis material; used where heat transport and structural function are both needed.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Heat-structural members, thermal buses, reactor/engine-adjacent support parts.
- Not Recommended For: Main mass-efficient frame members.

Tradeoffs:
- Pros: High thermal conductivity, useful in multifunctional parts.
- Cons: Heavy, lower specific structural efficiency than steels/titanium.
- Failure Modes: Thermal fatigue, creep/softening in hot conditions (alloy dependent), joint mismatch issues.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Advanced copper alloys remain niche but valuable.

#### [A1-09] Engineered Timber (structural grade)
- Code: `MAT_TIMBER_ENGINEERED`
- Category: Bio / Natural Composite
- SubCategory: Engineered wood
- Summary: Legitimate low-tech structural material for interiors, temporary habitats, low-cost colonization frames, and non-extreme modules.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Typical Use: Colony infrastructure modules, interior deck structures, low-G low-budget ships.
- Not Recommended For: Primary high-G spacecraft chassis, high-radiation or high-heat exposed structures.

Tradeoffs:
- Pros: Renewable, cheap, easy to process/repair, good specific stiffness in some directions.
- Cons: Moisture/temperature/radiation vulnerability, anisotropy, biological degradation risks.
- Failure Modes: Delamination, creep, rot/biological attack (if unprotected), thermal degradation.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established in terrestrial structures.
- Extrapolation notes: Space use plausible with coatings and controlled environments.

#### [A2-01] Titanium Alloy (general aerospace structural)
- Code: `MAT_TI_ALLOY_STRUCT`
- Category: Metal
- SubCategory: Titanium alloy
- Summary: Premium aerospace structural material with strong corrosion resistance and excellent specific strength for mass-sensitive spacecraft chassis and modules.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Primary structural members, pressure-capable hull sections, high-performance ships, corrosive environments.
- Not Recommended For: Ultra-low-cost mass fleets, very large low-budget structures.

Physical / Mechanical:
- DensityKgM3: `~4400-4600`
- YieldStrengthMPa: `700-1100` (alloy/processing dependent)
- ElasticModulusGPa: `~110`
- FatigueLimitMPa: `good`
- FractureToughnessMPaSqrtM: `good` (grade dependent)

Thermal / Environmental:
- CorrosionResistanceIndex: `85`
- RadiationResistanceIndex: `70`
- CryoPerformanceIndex: `80`
- MaxServiceTempC: `moderate-high` (below superalloys for long hot service)

Engineering Factors:
- ManufacturabilityIndex: `55`
- RepairabilityIndex: `55`
- StrategicRarityIndex: `45`
- BaseCostIndex: `4.5`

Tradeoffs:
- Pros: Excellent specific strength, corrosion resistance, broad aerospace utility.
- Cons: Expensive, hard machining/process control, joining complexity compared to steel/aluminum.
- Failure Modes: Process-induced defects, notch sensitivity in poor joint design, contamination during high-temp processing.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established in aerospace.
- Extrapolation notes: Advanced routes improve consistency and scale.

#### [A2-02] Beta Titanium Alloy
- Code: `MAT_TI_BETA`
- Category: Metal
- SubCategory: Beta titanium alloy
- Summary: High-performance titanium class optimized for strength/formability combinations and specialized aerospace structural behavior.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Advanced load-bearing members, high-performance landing structures, elastic-critical components.
- Not Recommended For: General low-cost construction without process expertise.

Tradeoffs:
- Pros: High strength potential, tunable properties, useful in advanced designs.
- Cons: Expensive, strict heat treatment/process windows, complex qualification.
- Failure Modes: Property drift from improper processing, fatigue cracking from poorly controlled microstructure.

Plausibility Notes:
- Real-world basis / lab basis: Known alloy classes with specialized applications.
- Extrapolation notes: Expanded structural use is realistic with better process control.

#### [A2-03] Maraging Steel
- Code: `MAT_STEEL_MARAGING`
- Category: Metal
- SubCategory: Maraging steel
- Summary: Ultra-high-strength steel family with excellent toughness and dimensional stability when properly processed.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: High-load joints, landing gear-like structures, precision structural nodes, tool-like structural inserts.
- Not Recommended For: Bulk hull mass-sensitive structures.

Tradeoffs:
- Pros: Very high strength, good toughness, excellent precision applications.
- Cons: Dense/heavy, expensive alloying, heat treatment critical.
- Failure Modes: Heat treatment errors causing strength loss, fatigue at stress concentrations.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established specialty material.
- Extrapolation notes: Excellent candidate for advanced precision chassis nodes.

#### [A2-04] Nickel Superalloy (structural hot-zone class)
- Code: `MAT_NI_SUPERALLOY_STRUCT_HOT`
- Category: Metal
- SubCategory: Nickel superalloy
- Summary: High-temperature structural material for engine-adjacent and thermal-extreme components; generally too heavy/expensive for full-frame use.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Hot structures, engine mounts, thermal barriers with load-bearing function.
- Not Recommended For: Main chassis except very specialized short sections.

Tradeoffs:
- Pros: Excellent hot strength and creep resistance.
- Cons: High density, very expensive, difficult machining/manufacturing.
- Failure Modes: Thermal fatigue, creep in extreme duty, oxidation/coating failure (grade dependent).

Plausibility Notes:
- Real-world basis / lab basis: Industrially established in engines/turbomachinery.
- Extrapolation notes: Structural use remains localized due to mass/cost.

#### [A2-05] Cobalt Superalloy (specialized)
- Code: `MAT_CO_SUPERALLOY`
- Category: Metal
- SubCategory: Cobalt superalloy
- Summary: Specialized high-temperature/wear/corrosion-resistant alloy class for extreme local structural applications.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Wear-intensive hot joints, exotic environmental interfaces, reactor-adjacent mechanisms.
- Not Recommended For: Large-area structures due to cost and mass.

Tradeoffs:
- Pros: Excellent hot/wear/corrosion performance in niche roles.
- Cons: Expensive, heavy, strategic supply concerns.
- Failure Modes: Thermal fatigue, joint mismatch issues, process-induced defects.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established specialty alloys.
- Extrapolation notes: Mostly niche even in advanced settings.

#### [A2-06] Zirconium Alloy (specialized structural/environmental)
- Code: `MAT_ZR_ALLOY_STRUCT`
- Category: Metal
- SubCategory: Zirconium alloy
- Summary: Specialized alloy family useful in select thermal/radiation/chemical regimes rather than general ship framing.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Reactor systems, corrosive/process modules, niche structural components with environment constraints.
- Not Recommended For: General hull/chassis unless mission-specific.

Tradeoffs:
- Pros: Strong niche environmental compatibility.
- Cons: Cost, specialization, limited general structural advantage.
- Failure Modes: Environment-specific degradation if wrong alloy/condition, embrittlement in adverse service.

Plausibility Notes:
- Real-world basis / lab basis: Known and used in specialized industries.
- Extrapolation notes: Broader space use plausible for reactor-heavy civilizations.

#### [A2-07] Beryllium Alloy (restricted, hazardous, high specific stiffness)
- Code: `MAT_BE_ALLOY_RESTRICTED`
- Category: Metal
- SubCategory: Beryllium alloy
- Summary: Extreme specific stiffness material with severe toxicity and handling constraints; strategic high-cost niche use only.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `1`
- Typical Use: Precision optical/sensor structures, ultra-stiff lightweight local members, specialized aerospace subsystems.
- Not Recommended For: General construction, field-repair fleets, low-tech industry.

Tradeoffs:
- Pros: Excellent stiffness-to-weight in niche applications.
- Cons: Toxicity, brittle behavior concerns, processing and safety burden, strategic rarity.
- Failure Modes: Brittle fracture, contamination incidents, damage sensitivity.

Plausibility Notes:
- Real-world basis / lab basis: Industrially established but heavily restricted.
- Extrapolation notes: Advanced civilizations may manage toxicity better, but tradeoffs remain.

#### [A2-08] Titanium Aluminide (intermetallic)
- Code: `MAT_TIAL_INTERMETALLIC`
- Category: Metal / Intermetallic
- SubCategory: TiAl
- Summary: Lightweight high-temperature intermetallic class useful in hot rotating and structural components where titanium is not enough thermally.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Hot-zone lightweight parts, engine-near structural members, advanced high-temp systems.
- Not Recommended For: Shock-dominated or repair-heavy bulk structures.

Tradeoffs:
- Pros: Good high-temperature specific performance.
- Cons: Brittle behavior / toughness limitations vs conventional alloys, hard processing/joining.
- Failure Modes: Brittle cracking, thermal shock sensitivity, manufacturing defect sensitivity.

Plausibility Notes:
- Real-world basis / lab basis: Demonstrated and used in demanding applications.
- Extrapolation notes: Wider use depends on advanced process + quality control.

#### [A2-09] Nickel Aluminide (intermetallic)
- Code: `MAT_NIAL_INTERMETALLIC`
- Category: Metal / Intermetallic
- SubCategory: NiAl
- Summary: Intermetallic family with attractive high-temperature behavior, best suited to specialized hot-structure applications.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Hot structural inserts, oxidation-resistant high-temp components, advanced propulsion-adjacent systems.
- Not Recommended For: General chassis framing or impact-prone structural members.

Tradeoffs:
- Pros: High-temperature oxidation/strength advantages in niche roles.
- Cons: Brittleness, joining complexity, process sensitivity.
- Failure Modes: Brittle fracture, thermal shock cracking, interface failures at mixed-material joints.

Plausibility Notes:
- Real-world basis / lab basis: Known intermetallic systems with active/advanced use cases.
- Extrapolation notes: Better when paired with advanced assembly and defect control.

#### A3. Refractory / Extreme-Temperature Alloys
- Tungsten Alloy
- Molybdenum Alloy
- Tantalum Alloy
- Niobium Alloy
- Refractory High-Entropy Alloy (RHEA)
- ODS Nickel-Based Alloy
- ODS Ferritic/Steel Alloy
- UHT-capable Metal Matrix Composite (refractory MMC class)

#### A4. Ceramics / Cermets / UHTC
- Alumina Ceramic (structural grade)
- Silicon Carbide Ceramic (SiC)
- Boron Carbide Ceramic
- Zirconia Ceramic (stabilized)
- Silicon Nitride Ceramic
- Aluminum Nitride Ceramic (specialized thermal)
- Cermet (general metal-ceramic composite)
- Graded Cermet (advanced)
- MAX-Phase Ceramic-Class Material
- Ultra-High-Temperature Ceramic (UHTC class)
- Ceramic Foam (structural core grade)

#### A5. Polymer / Fiber Composites
- Glass Fiber Reinforced Polymer (GFRP)
- Carbon Fiber Reinforced Polymer (CFRP)
- Aramid/Kevlar Composite
- Hybrid Carbon-Aramid Composite
- Basalt Fiber Composite
- High-Temperature Polymer Matrix Composite
- Radiation-tailored Polymer Composite Matrix (advanced)
- Bio-composite Structural Polymer

#### A6. MMC / CMC / Hybrid Composites
- Metal Matrix Composite (MMC, general)
- Ceramic Matrix Composite (CMC, general)
- Carbon-Carbon Composite (specialized high-temp)
- Fiber Metal Laminate (FML)
- Nanolaminate Metal Composite
- CNT-Reinforced Metal Matrix Composite
- Graphene-Reinforced Metal Matrix Composite
- Multi-material Gradient Structural Composite Node
- Hybrid Metal-Ceramic-Composite Structural Section

#### A7. Bio-Engineered Structural Materials
- Engineered Biomass Composite Frame Stock
- Bio-grown Composite Preform (for later infiltration)
- Biomineralized Ceramic Scaffold
- Bio-template Lattice (pre-mineralization)
- Protein-guided Crystallization Template Material (advanced)
- Bio-derived High-Performance Resin Matrix (advanced)

#### A8. Nanostructured / Advanced Hard-Sci-Fi Materials
- Nanograined Steel
- Nanograined Titanium Alloy
- Nanostructured High-Entropy Alloy
- Bulk Metallic Glass (BMG, structural-limited)
- Bulk Metallic Glass Composite (toughened)
- Functionally Graded Material (FGM structural class)
- Atomically Engineered Alloy (advanced)
- Programmable Grain Texture Alloy (advanced)
- Atomic-Interface-Optimized Multi-Phase Material (advanced)
- Cryo-optimized Lattice Titanium Feedstock/Material System

#### A3-A8 Detailed Entries (first pass)

#### [A3-01] Tungsten Alloy
- Code: `MAT_W_ALLOY`
- Category: Metal
- SubCategory: Refractory heavy alloy
- Summary: Extremely dense, high-temperature capable structural/special-purpose material for local reinforcement, radiation/kinetic protection, and hot load paths.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Countermasses, local armor-core supports, reactor shielding structures, extreme wear/hot inserts.
- Not Recommended For: Primary chassis framing (mass penalty is usually unacceptable).
- Tradeoffs:
- Pros: High temperature capability, stiffness and compressive robustness, excellent mass per volume.
- Cons: Very heavy, expensive, difficult machining/forming, brittle tendencies in some alloy systems.
- Failure Modes: Brittle cracking under shock, joint/interface failure due to thermal mismatch, fatigue at hard transitions.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established tungsten alloy classes.
- Extrapolation notes: Advanced processing improves joinability and defect control, not density.

#### [A3-02] Molybdenum Alloy
- Code: `MAT_MO_ALLOY`
- Category: Metal
- SubCategory: Refractory alloy
- Summary: High-temperature refractory alloy for structural roles where nickel superalloys are too heavy/hot limits are exceeded.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Hot structures in vacuum, propulsion-adjacent load members, thermal shields with structural duty.
- Not Recommended For: Corrosive atmospheric use without protection, impact-heavy outer hull sections.
- Tradeoffs:
- Pros: Strong high-temp potential, lower density than tungsten, useful refractory niche.
- Cons: Oxidation/environment sensitivity, brittle behavior risk, manufacturing complexity.
- Failure Modes: Oxidation-assisted degradation, brittle crack initiation, thermal fatigue.
- Plausibility Notes:
- Real-world basis / lab basis: Known refractory alloy systems.
- Extrapolation notes: Better coatings and vacuum manufacturing expand use.

#### [A3-03] Tantalum Alloy
- Code: `MAT_TA_ALLOY`
- Category: Metal
- SubCategory: Refractory alloy
- Summary: Dense, corrosion-resistant refractory alloy family for extreme chemical/thermal niche structures.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Extreme chemical handling, reactor interfaces, highly specialized structural inserts.
- Not Recommended For: Mass-sensitive frames, wide-area structures.
- Tradeoffs:
- Pros: Strong niche chemical and thermal resilience.
- Cons: Very expensive, heavy, strategic rarity high.
- Failure Modes: Local overheating/creep in extreme duty, interface/joint mismatch failures.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially known specialized material.
- Extrapolation notes: Stays niche even in advanced fleets.

#### [A3-04] Niobium Alloy
- Code: `MAT_NB_ALLOY`
- Category: Metal
- SubCategory: Refractory alloy
- Summary: Refractory alloy class with valuable high-temperature niche roles, especially in vacuum and specialized propulsion structures.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Heat-critical structures, propulsion-adjacent lightweight refractory applications.
- Not Recommended For: General hull use, corrosive atmosphere exposure without protection.
- Tradeoffs:
- Pros: Useful refractory performance niche with lower mass than tungsten-class options.
- Cons: Cost, oxidation sensitivity, process difficulty.
- Failure Modes: Oxidation degradation, thermal fatigue, embrittlement in wrong service envelope.
- Plausibility Notes:
- Real-world basis / lab basis: Demonstrated in advanced aerospace/high-temp domains.
- Extrapolation notes: Benefits strongly from coatings and quality control.

#### [A3-05] Refractory High-Entropy Alloy (RHEA)
- Code: `MAT_RHEA`
- Category: Metal
- SubCategory: High-entropy refractory alloy
- Summary: Advanced high-temperature alloy class targeting combined refractory performance and improved microstructural stability.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Advanced propulsion structures, civilization-grade hot load paths, extreme mission chassis inserts.
- Not Recommended For: Low-tech mass manufacture, field repair fleets.
- Tradeoffs:
- Pros: Potential high-temp strength retention, tunable compositions, advanced performance ceiling.
- Cons: Very high process complexity, phase control difficulty, qualification burden, cost extreme.
- Failure Modes: Phase instability, brittleness, composition drift, defect sensitivity.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded alloy family direction.
- Extrapolation notes: Hard-sci-fi use assumes major advances in process control and validation.

#### [A3-06] ODS Nickel-Based Alloy
- Code: `MAT_ODS_NI`
- Category: Metal
- SubCategory: ODS superalloy
- Summary: Nickel alloy strengthened by oxide dispersions for superior high-temperature creep resistance in long-duration hot service.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Long-life hot structural members, reactor/engine-adjacent supports.
- Not Recommended For: Cheap fabrication, geometry-heavy mass production without PM/HIP capability.
- Tradeoffs:
- Pros: Exceptional high-temp creep behavior and stability.
- Cons: Processing complexity, anisotropy/process sensitivity, cost.
- Failure Modes: Defect clusters from powder/process issues, joint degradation, local creep if microstructure control fails.
- Plausibility Notes:
- Real-world basis / lab basis: Known class; challenging but real.
- Extrapolation notes: Better scalability expected in advanced civilizations.

#### [A3-07] ODS Ferritic/Steel Alloy
- Code: `MAT_ODS_FE`
- Category: Metal
- SubCategory: ODS ferritic alloy
- Summary: Advanced steel-like alloy system with improved temperature/radiation resilience for harsh structural environments.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Radiation-exposed structural zones, long-duration power systems, high-temperature steel-class structures.
- Not Recommended For: General low-cost shipbuilding.
- Tradeoffs:
- Pros: Better high-temp/radiation behavior than conventional steels.
- Cons: Manufacturing route complexity, limited processing options, cost.
- Failure Modes: Property inconsistency from powder route defects, joint and fabrication-induced degradation.
- Plausibility Notes:
- Real-world basis / lab basis: Research and specialized development basis.
- Extrapolation notes: Industrialization scale-up is plausible future step.

#### [A3-08] UHT-capable Metal Matrix Composite (refractory MMC class)
- Code: `MAT_MMC_REFRACT_UHT`
- Category: Hybrid Composite
- SubCategory: Refractory MMC
- Summary: Metal-matrix structural composite optimized for high-temperature stiffness/strength retention and reduced creep.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Extreme hot-zone structural components where pure alloys underperform.
- Not Recommended For: General hulls or high-impact exposed structures.
- Tradeoffs:
- Pros: Tunable high-temp behavior, improved specific performance in niche roles.
- Cons: Process complexity, interface failures, difficult repair and qualification.
- Failure Modes: Matrix-reinforcement debonding, thermal mismatch cracking, creep-localization.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded across MMC classes; UHT structural scaling is extrapolative.
- Extrapolation notes: Requires strong interface engineering + QA.

#### [A4-01] Alumina Ceramic (structural grade)
- Code: `MAT_CER_ALUMINA`
- Category: Ceramic
- SubCategory: Oxide ceramic
- Summary: Hard, wear-resistant ceramic useful in insulation, wear surfaces, and compressive structural inserts, but limited by brittleness.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Typical Use: Wear inserts, thermal barriers, insulating supports, ceramic cores.
- Not Recommended For: Primary tension/shock-loaded chassis members.
- Tradeoffs:
- Pros: Hardness, temperature capability, corrosion resistance, electrical insulation.
- Cons: Brittleness, poor impact tolerance, difficult joining to metals.
- Failure Modes: Brittle fracture, thermal shock cracking, interface spallation.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Structural use improves with graded interfaces and confinement.

#### [A4-02] Silicon Carbide Ceramic (SiC)
- Code: `MAT_CER_SIC`
- Category: Ceramic
- SubCategory: Carbide ceramic
- Summary: High-stiffness, high-temperature, high-hardness ceramic with strong value in thermal and wear-critical components.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Hot structures, armor layers, wear parts, high-temp ceramic components.
- Not Recommended For: Unprotected shock-prone structural members.
- Tradeoffs:
- Pros: Excellent hardness, temperature behavior, corrosion resistance.
- Cons: Brittle fracture risk, processing cost, joining complexity.
- Failure Modes: Impact fracture, thermal shock cracking, flaw-driven catastrophic failure.
- Plausibility Notes:
- Real-world basis / lab basis: Established advanced ceramic.
- Extrapolation notes: Better QA and graded joints improve structural applicability.

#### [A4-03] Boron Carbide Ceramic
- Code: `MAT_CER_B4C`
- Category: Ceramic
- SubCategory: Carbide ceramic
- Summary: Very hard lightweight ceramic, especially useful for protective roles rather than general structural load-bearing.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Armor systems, protective inserts, specialized lightweight hard layers.
- Not Recommended For: Main chassis members, cyclic impact-heavy structural frames.
- Tradeoffs:
- Pros: Exceptional hardness and low density for a ceramic.
- Cons: Brittleness and shock sensitivity, expensive processing.
- Failure Modes: Catastrophic brittle fracture, impact fragmentation, flaw sensitivity.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established in protection applications.
- Extrapolation notes: Structural integration should remain hybridized.

#### [A4-04] Zirconia Ceramic (stabilized)
- Code: `MAT_CER_ZRO2_STAB`
- Category: Ceramic
- SubCategory: Toughened ceramic
- Summary: Tougher ceramic option than many oxide ceramics, useful where ceramic benefits are needed with better damage tolerance.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Wear parts, thermal/structural interfaces, localized ceramic structural components.
- Not Recommended For: Large lightweight primary structures.
- Tradeoffs:
- Pros: Improved toughness relative to many ceramics, good wear/corrosion behavior.
- Cons: Dense for ceramic, still brittle vs metals, thermal constraints.
- Failure Modes: Thermal cracking, phase-related degradation (variant dependent), interface failure.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Advanced grades improve reliability envelope.

#### [A4-05] Silicon Nitride Ceramic
- Code: `MAT_CER_SI3N4`
- Category: Ceramic
- SubCategory: Nitride ceramic
- Summary: Advanced structural ceramic with strong high-temperature and wear properties, useful in specialized moving and load-bearing hot components.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Bearings, hot mechanical parts, high-wear structural interfaces.
- Not Recommended For: Large shock-loaded hull framing.
- Tradeoffs:
- Pros: Good toughness among ceramics (relative), high-temp wear performance.
- Cons: Processing cost and defect sensitivity remain significant.
- Failure Modes: Brittle fracture from defects, thermal shock damage, contact fatigue.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established advanced ceramic.
- Extrapolation notes: Better quality profiles expand reliability, not eliminate brittleness.

#### [A4-06] Aluminum Nitride Ceramic (specialized thermal)
- Code: `MAT_CER_ALN`
- Category: Ceramic
- SubCategory: Nitride ceramic
- Summary: Thermally conductive electrical-insulating ceramic mainly for thermal-electrical structural interfaces.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Heat spreader mounts, thermal-electric support structures.
- Not Recommended For: General structural load paths.
- Tradeoffs:
- Pros: Unusual thermal/electrical property combination.
- Cons: Brittleness, cost, niche use.
- Failure Modes: Thermal shock and brittle cracking, interface mismatch.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established niche ceramic.
- Extrapolation notes: Important as multifunctional subsystem material.

#### [A4-07] Cermet (general metal-ceramic composite)
- Code: `MAT_CERMET_GEN`
- Category: Hybrid Composite
- SubCategory: Metal-ceramic composite
- Summary: Hybrid class balancing hardness/temperature benefits of ceramics with some toughness/processability from metal phases.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Wear-resistant structural interfaces, thermal/mechanical inserts, specialized modules.
- Not Recommended For: Broad primary chassis use without tailored design.
- Tradeoffs:
- Pros: Tunable balance of hardness and toughness, niche structural/wear utility.
- Cons: Interface complexity, processing difficulty, unpredictable failure if poorly qualified.
- Failure Modes: Interface debonding, thermal mismatch cracking, brittle cluster fracture.
- Plausibility Notes:
- Real-world basis / lab basis: Known class with many variants.
- Extrapolation notes: Advanced grading and interface engineering expand usefulness.

#### [A4-08] Graded Cermet (advanced)
- Code: `MAT_CERMET_GRADED`
- Category: Hybrid Composite
- SubCategory: Functionally graded cermet
- Summary: Cermet with controlled property gradient to reduce thermal and stress mismatch problems.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Hot structural transitions, armor-structure interfaces, engine mount transitions.
- Not Recommended For: Low-tech production or rough field repair contexts.
- Tradeoffs:
- Pros: Better interface survivability, more controlled stress distribution.
- Cons: High process complexity and QA burden.
- Failure Modes: Gradient defects, local delamination, process drift causing weak layers.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded via FGM/cermet work.
- Extrapolation notes: Mass production is hard-sci-fi but plausible.

#### [A4-09] MAX-Phase Ceramic-Class Material
- Code: `MAT_MAX_PHASE`
- Category: Advanced Ceramic / Layered Compound
- SubCategory: MAX-phase family
- Summary: Layered ceramic-like materials with mixed metallic/ceramic behavior in some systems, promising for extreme environments.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Specialized hot structural skins, damage-tolerant ceramic-adjacent applications, coatings/insert systems.
- Not Recommended For: Untested bulk chassis use.
- Tradeoffs:
- Pros: Interesting thermal/oxidation and damage-tolerance profile relative to conventional ceramics.
- Cons: Material family complexity, production scale limitations, qualification uncertainty.
- Failure Modes: Layer/interface degradation, oxidation/path-dependent damage, brittle-local failures.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded materials family.
- Extrapolation notes: Large structural implementation is advanced extrapolation.

#### [A4-10] Ultra-High-Temperature Ceramic (UHTC class)
- Code: `MAT_UHTC_CLASS`
- Category: Ceramic
- SubCategory: UHTC
- Summary: Extreme-temperature ceramic class for the harshest thermal environments; typically protective/hot-structure role, not general chassis material.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Hypersonic/entry hot structures, engine-near shields, extreme thermal panels.
- Not Recommended For: Shock-loaded primary frames or low-cost manufacturing.
- Tradeoffs:
- Pros: Extreme thermal capability.
- Cons: Brittleness, manufacturing difficulty, thermal shock risk, expensive QA.
- Failure Modes: Thermal shock cracking, oxidation/environment-dependent degradation, flaw-driven fracture.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded and niche demonstrators.
- Extrapolation notes: Broader use needs advanced joining and defect control.

#### [A4-11] Ceramic Foam (structural core grade)
- Code: `MAT_CER_FOAM_STRUCT`
- Category: Ceramic / Cellular
- SubCategory: Ceramic foam
- Summary: Lightweight high-temperature core material for sandwich and thermal-structural applications; not a standalone primary load frame.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Core structures, thermal isolation load paths, sacrificial impact/heat layers.
- Not Recommended For: Direct primary chassis tension members.
- Tradeoffs:
- Pros: Very low effective density, thermal resistance, useful multifunctional core.
- Cons: Brittle cell walls, defect variability, limited tensile/impact behavior.
- Failure Modes: Cell crush localization, brittle collapse, thermal shock cracking.
- Plausibility Notes:
- Real-world basis / lab basis: Known class; structural optimization ongoing.
- Extrapolation notes: Advanced grading and QA improve predictability.

#### [A5-01] Glass Fiber Reinforced Polymer (GFRP)
- Code: `MAT_GFRP`
- Category: Composite
- SubCategory: Polymer matrix fiber composite
- Summary: Affordable composite with good corrosion resistance and moderate specific properties for non-extreme structural roles.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Panels, housings, moderate-load structures, corrosion-resistant modules.
- Not Recommended For: Peak-performance primary spacecraft chassis.
- Tradeoffs:
- Pros: Cheap composite route, corrosion resistance, manufacturable.
- Cons: Lower specific performance than CFRP, matrix temperature/radiation limitations.
- Failure Modes: Delamination, matrix cracking, moisture/thermal degradation.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Better resins/QA extend service envelope.

#### [A5-02] Carbon Fiber Reinforced Polymer (CFRP)
- Code: `MAT_CFRP`
- Category: Composite
- SubCategory: Polymer matrix carbon fiber composite
- Summary: High specific stiffness/strength composite class central to advanced lightweight structures, with strong anisotropy and joining tradeoffs.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Mass-critical hull panels, beams, shell structures, advanced ship modules.
- Not Recommended For: Poorly controlled low-tech manufacturing, hot zones beyond matrix limits.
- Tradeoffs:
- Pros: Excellent specific stiffness and strength (directional), corrosion resistant, highly tailorable.
- Cons: Anisotropy, impact/delamination issues, repair and inspection complexity, matrix temperature limits.
- Failure Modes: Delamination, fiber breakage, matrix cracking, hidden impact damage.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established in aerospace.
- Extrapolation notes: Advanced matrices and QA significantly improve reliability.

#### [A5-03] Aramid/Kevlar Composite
- Code: `MAT_ARAMID_COMPOSITE`
- Category: Composite
- SubCategory: Aramid fiber composite
- Summary: Composite class with strong toughness and impact resistance characteristics, often used in protective and damage-tolerant roles.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Impact-resistant panels, protective structures, spall liners, hybrid structures.
- Not Recommended For: Ultra-stiff precision primary members.
- Tradeoffs:
- Pros: High toughness and impact energy absorption, low density.
- Cons: Lower compressive stiffness than CFRP, moisture/UV/environment sensitivity (system dependent), difficult machining.
- Failure Modes: Delamination, fiber pullout, compression-kink failures.
- Plausibility Notes:
- Real-world basis / lab basis: Industrially established.
- Extrapolation notes: Excellent when hybridized with carbon fibers.

#### [A5-04] Hybrid Carbon-Aramid Composite
- Code: `MAT_COMPOSITE_CARBON_ARAMID_HYB`
- Category: Composite
- SubCategory: Hybrid fiber composite
- Summary: Blends stiffness-focused carbon fibers with toughness-focused aramid fibers to balance performance and damage tolerance.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Lightweight hulls and panels needing both stiffness and impact tolerance.
- Not Recommended For: Low-quality manufacturing environments lacking laminate control.
- Tradeoffs:
- Pros: Better balance than single-fiber systems in many applications.
- Cons: Design complexity, interface/stackup sensitivity, inspection complexity.
- Failure Modes: Hybrid laminate delamination, uneven load sharing, interlaminar failures.
- Plausibility Notes:
- Real-world basis / lab basis: Known composite design approach.
- Extrapolation notes: Advanced layup optimization makes this much stronger in-game.

#### [A5-05] Basalt Fiber Composite
- Code: `MAT_BASALT_COMPOSITE`
- Category: Composite
- SubCategory: Basalt fiber polymer composite
- Summary: Mid-tier composite with useful corrosion and thermal behavior, often a pragmatic alternative to glass/carbon in certain fleets.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `2`
- Typical Use: Cost-balanced panels and structures in industrial/colony settings.
- Not Recommended For: Top-tier mass-critical spacecraft chassis.
- Tradeoffs:
- Pros: Cost/performance compromise, environmental durability.
- Cons: Lower peak performance than carbon composites, still matrix-limited.
- Failure Modes: Delamination, matrix aging, impact damage.
- Plausibility Notes:
- Real-world basis / lab basis: Known and applied; less widespread than glass/carbon.
- Extrapolation notes: Strong candidate for utilitarian factions/civilizations.

#### [A5-06] High-Temperature Polymer Matrix Composite
- Code: `MAT_HT_PMC`
- Category: Composite
- SubCategory: High-temp polymer matrix composite
- Summary: Composite system with improved matrix thermal capability extending composite structural use into hotter service zones.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Advanced lightweight structures near moderate hot zones, high-performance aerospace hull sections.
- Not Recommended For: Extreme UHT environments where CMC/metal systems are needed.
- Tradeoffs:
- Pros: Extends composite benefits into hotter regimes.
- Cons: Cost, process complexity, matrix chemistry sensitivity, inspection burden.
- Failure Modes: Matrix degradation, delamination under thermal cycling, bondline failures.
- Plausibility Notes:
- Real-world basis / lab basis: Known principles and advanced materials development.
- Extrapolation notes: Wider reliable use assumes improved processing/quality.

#### [A5-07] Radiation-tailored Polymer Composite Matrix (advanced)
- Code: `MAT_PMC_RAD_TAILORED`
- Category: Composite
- SubCategory: Advanced polymer matrix system
- Summary: Composite matrix chemistry optimized for radiation aging resistance and long-duration space service.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Long-endurance deep-space lightweight structures, sensor booms, habitat shell subsystems.
- Not Recommended For: Low-tech or cost-sensitive manufacturing.
- Tradeoffs:
- Pros: Better long-term matrix stability in radiation-heavy missions.
- Cons: Expensive chemistry/process qualification, uncertain repair in field conditions.
- Failure Modes: Gradual matrix embrittlement, interface degradation, thermal-radiation synergistic damage.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded direction.
- Extrapolation notes: Advanced spacefaring civilizations can industrialize this.

#### [A5-08] Bio-composite Structural Polymer
- Code: `MAT_BIO_COMPOSITE_POLY_STRUCT`
- Category: Bio / Composite
- SubCategory: Bio-derived polymer composite
- Summary: Structurally useful composite system leveraging bio-derived feedstocks/matrices for sustainable production and colony industry.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `2`
- Typical Use: Colony modules, low- to mid-performance ship structures, interior and non-critical panels.
- Not Recommended For: Extreme high-G or high-radiation primary chassis.
- Tradeoffs:
- Pros: Renewable feedstock, local manufacturability potential, useful for logistics-heavy worlds.
- Cons: Performance variability, environmental aging, lower peak properties than top composites.
- Failure Modes: Matrix aging, moisture/chemical damage, delamination.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded and emerging industrial directions.
- Extrapolation notes: Space adaptation is plausible with coatings and QA.

#### [A6-01] Metal Matrix Composite (MMC, general)
- Code: `MAT_MMC_GEN`
- Category: Composite
- SubCategory: Metal matrix composite
- Summary: Composite class combining metallic matrix toughness with reinforcement-driven stiffness/wear/thermal improvements.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: High-performance structural inserts, heat-structural members, wear-loaded supports.
- Not Recommended For: Low-tech field-repair platforms or mass cheap hulls.
- Tradeoffs:
- Pros: Multifunctional structural behavior, good thermal and wear performance options.
- Cons: Interface and process complexity, cost, machining difficulty.
- Failure Modes: Reinforcement debonding, interface cracks, processing defects.
- Plausibility Notes:
- Real-world basis / lab basis: Known and used in specialized contexts.
- Extrapolation notes: Broader adoption needs better manufacturing/QA.

#### [A6-02] Ceramic Matrix Composite (CMC, general)
- Code: `MAT_CMC_GEN`
- Category: Composite
- SubCategory: Ceramic matrix composite
- Summary: High-temperature composite class designed to improve damage tolerance over monolithic ceramics while preserving thermal capability.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Hot-zone structures, thermal protection with structural load, propulsion-adjacent supports.
- Not Recommended For: Cheap bulk chassis production.
- Tradeoffs:
- Pros: Strong thermal capability, improved ceramic damage tolerance.
- Cons: Costly manufacturing, inspection complexity, still not metal-like toughness.
- Failure Modes: Matrix cracking, fiber/matrix interface degradation, oxidation/environment damage.
- Plausibility Notes:
- Real-world basis / lab basis: Demonstrated and growing industrial use.
- Extrapolation notes: Large-scale use plausible with better manufacturing.

#### [A6-03] Carbon-Carbon Composite (specialized high-temp)
- Code: `MAT_CARBON_CARBON`
- Category: Composite
- SubCategory: Carbon-carbon composite
- Summary: Extreme thermal composite for specialized high-temperature applications with serious oxidation/environment constraints.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Very high temperature components in protected/vacuum conditions.
- Not Recommended For: Oxidizing atmospheres without robust protection, general hull use.
- Tradeoffs:
- Pros: Excellent high-temp capability, low density vs many hot metals.
- Cons: Oxidation vulnerability, expensive processing, specialized inspection/repair.
- Failure Modes: Oxidation loss, delamination, thermal shock/interface damage.
- Plausibility Notes:
- Real-world basis / lab basis: Established in extreme applications.
- Extrapolation notes: Advanced coatings can widen use envelope.

#### [A6-04] Fiber Metal Laminate (FML)
- Code: `MAT_FML`
- Category: Hybrid Composite
- SubCategory: Fiber-metal laminate
- Summary: Laminated hybrid combining metal and fiber-composite layers for balanced fatigue, impact, and manufacturability behavior.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Hull panels, pressure-capable skins, fatigue-managed structures.
- Not Recommended For: Extremely hot zones or crude manufacturing lines.
- Tradeoffs:
- Pros: Good damage/fatigue behavior balance, hybrid advantages.
- Cons: Interface complexity, repair complexity, moisture/galvanic issues if poorly designed.
- Failure Modes: Delamination, interface corrosion, crack propagation through layers.
- Plausibility Notes:
- Real-world basis / lab basis: Known aerospace concept with real implementations.
- Extrapolation notes: Strong candidate for advanced spacecraft hull skins.

#### [A6-05] Nanolaminate Metal Composite
- Code: `MAT_NANOLAM_METAL_COMP`
- Category: Advanced Composite
- SubCategory: Nanolaminate metallic composite
- Summary: Layered metallic micro/nano-architecture designed to improve crack resistance, strength, or thermal behavior through interface engineering.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Critical joints, high-performance localized structural members, advanced skins.
- Not Recommended For: Low-cost large simple structures.
- Tradeoffs:
- Pros: Interface-engineered performance gains, crack-arrest opportunities.
- Cons: Fabrication complexity and scale limits, inspection difficulty.
- Failure Modes: Interface delamination, diffusion degradation, process variability.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded layered material concepts.
- Extrapolation notes: Larger structural use is hard-sci-fi but plausible.

#### [A6-06] CNT-Reinforced Metal Matrix Composite
- Code: `MAT_CNT_MMC`
- Category: Advanced Composite
- SubCategory: CNT-reinforced MMC
- Summary: Metal matrix composite using carbon nanotube reinforcement for potential gains in specific stiffness, fatigue, or thermal behavior if interfaces are controlled.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Typical Use: Ultra-premium structural inserts, advanced civilization precision frames.
- Not Recommended For: Broad industrial fleets; unreliable if process quality is weak.
- Tradeoffs:
- Pros: High theoretical payoff in multifunctional performance.
- Cons: Interface control is extremely hard, repeatability and scale are major constraints.
- Failure Modes: Reinforcement agglomeration, poor load transfer, interface degradation.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded direction.
- Extrapolation notes: Requires major manufacturing and QA advances.

#### [A6-07] Graphene-Reinforced Metal Matrix Composite
- Code: `MAT_GRAPHENE_MMC`
- Category: Advanced Composite
- SubCategory: Graphene-reinforced MMC
- Summary: Graphene-reinforced metallic composite family aiming at improved strength/stiffness/thermal performance in high-end engineered systems.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium lightweight structural members, thermal-structural multifunctional parts.
- Not Recommended For: Bulk structures or low-confidence production chains.
- Tradeoffs:
- Pros: Strong theoretical multifunctional potential.
- Cons: Dispersion/interface/repeatability challenges, cost extreme.
- Failure Modes: Poor dispersion, interface failure, brittle local behavior from processing artifacts.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded.
- Extrapolation notes: Physical, but industrial scale is advanced-civilization territory.

#### [A6-08] Multi-material Gradient Structural Composite Node
- Code: `MAT_GRAD_NODE_MULTI`
- Category: Advanced Hybrid Composite
- SubCategory: Multi-material graded structural node
- Summary: Purpose-built node material/system with graded transitions across metal/ceramic/composite domains to reduce joint stress and mismatch.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Civilization-grade chassis nodes, engine mounts, pressure-hull interfaces.
- Not Recommended For: Any low-tech production context.
- Tradeoffs:
- Pros: Potentially huge reduction in interface failures and stress concentrations.
- Cons: Extreme manufacturing and QA complexity, expensive, hard to repair.
- Failure Modes: Hidden gradient defects, local weak transition zones, process drift failures.
- Plausibility Notes:
- Real-world basis / lab basis: Built on graded materials + multi-material processing concepts.
- Extrapolation notes: Speculative but physically plausible at very high tech.

#### [A6-09] Hybrid Metal-Ceramic-Composite Structural Section
- Code: `MAT_HYBRID_MCC_SECTION`
- Category: Hybrid Composite
- SubCategory: Multi-domain hybrid section
- Summary: Engineered section-level material architecture combining metal load paths, ceramic thermal/wear zones, and composite mass-efficient zones.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: High-value ship classes, multifunctional structural segments.
- Not Recommended For: Cheap modular fleets or field repair-heavy operations.
- Tradeoffs:
- Pros: Tuned multifunctionality and mass efficiency.
- Cons: Design/analysis/repair complexity, difficult certification.
- Failure Modes: Interface cascade failure, thermal mismatch, inspection blind spots.
- Plausibility Notes:
- Real-world basis / lab basis: Conceptually grounded in hybrid structures.
- Extrapolation notes: Large integrated sections are hard-sci-fi but plausible.

#### [A7-01] Engineered Biomass Composite Frame Stock
- Code: `MAT_BIO_FRAME_STOCK`
- Category: Bio / Composite
- SubCategory: Engineered biomass structural stock
- Summary: Colony-manufacturable structural stock derived from engineered biomass with controlled fiber orientation and resin/mineral reinforcement.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `3`
- Typical Use: Frontier colonies, habitat modules, low-to-mid stress structures.
- Not Recommended For: High-G primary spacecraft chassis or long radiation-heavy missions without upgrades.
- Tradeoffs:
- Pros: Local production potential, strategic independence, repairability.
- Cons: Performance variability, environment sensitivity, lower ceiling than advanced alloys/composites.
- Failure Modes: Moisture/chemical degradation, creep, delamination, biological contamination if processing is poor.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded via engineered bio-composites and timber analogs.
- Extrapolation notes: Strong colony use-case in hard sci-fi.

#### [A7-02] Bio-grown Composite Preform (for later infiltration)
- Code: `MAT_BIO_PREFORM`
- Category: Bio / Manufacturing Intermediate
- SubCategory: Bio-grown preform
- Summary: Biologically grown porous/fibrous scaffold intended as precursor for resin, ceramic, or metal infiltration into final structural composite.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Advanced manufacturing feedstock for lattice/cellular structures and low-waste fabrication.
- Not Recommended For: Direct standalone structural use before infiltration and stabilization.
- Tradeoffs:
- Pros: Complex geometry at low manufacturing energy, scalable template generation.
- Cons: Tight process control required, contamination variability, multi-step production.
- Failure Modes: Incomplete infiltration, voids, weak scaffold zones, contamination defects.
- Plausibility Notes:
- Real-world basis / lab basis: Bio-template and biomaterial scaffold concepts exist.
- Extrapolation notes: Industrialized structural use is advanced but plausible.

#### [A7-03] Biomineralized Ceramic Scaffold
- Code: `MAT_BIOMIN_CER_SCAFFOLD`
- Category: Bio / Ceramic Hybrid
- SubCategory: Biomineralized scaffold
- Summary: Biologically templated and mineralized porous ceramic structural material emphasizing lightweight geometry and thermal resilience.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Thermal-structural cores, advanced habitat shells, niche lightweight ceramic frameworks.
- Not Recommended For: Shock-heavy exposed primary load paths without confinement/hybridization.
- Tradeoffs:
- Pros: Geometry efficiency, potentially low-waste fabrication, good thermal behavior.
- Cons: Brittleness and process variability, difficult certification.
- Failure Modes: Brittle cell failure, incomplete mineralization defects, crack percolation through scaffold.
- Plausibility Notes:
- Real-world basis / lab basis: Biomineralization principles are real.
- Extrapolation notes: Structural aerospace-grade industrialization is speculative but plausible.

#### [A7-04] Bio-template Lattice (pre-mineralization)
- Code: `MAT_BIO_TEMPLATE_LATTICE`
- Category: Bio / Manufacturing Intermediate
- SubCategory: Bio-generated lattice template
- Summary: Precision biological template for lattice manufacture, later converted into metal/ceramic/composite structural lattice.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Advanced low-waste lattice manufacturing pipelines.
- Not Recommended For: Direct final load-bearing use (template stage only).
- Tradeoffs:
- Pros: Complex geometry generation at scale, low machining burden.
- Cons: Requires advanced downstream conversion, high contamination/process QA needs.
- Failure Modes: Template geometry defects, conversion shrinkage cracks, incomplete replication.
- Plausibility Notes:
- Real-world basis / lab basis: Template-assisted manufacturing has basis.
- Extrapolation notes: Bio precision and industrial scale are speculative but plausible.

#### [A7-05] Protein-guided Crystallization Template Material (advanced)
- Code: `MAT_PROTEIN_CRYSTAL_TEMPLATE`
- Category: Bio / Advanced Manufacturing Template
- SubCategory: Molecular template system
- Summary: Molecular/biological templating system used to influence nucleation and microstructure formation in advanced materials processing.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Ultra-advanced materials fabrication and interface engineering.
- Not Recommended For: Routine industrial fleets.
- Tradeoffs:
- Pros: Potential microstructure precision gains beyond conventional control.
- Cons: Extreme complexity, process sensitivity, validation burden.
- Failure Modes: Template mismatch, contamination-driven microstructural defects, reproducibility loss.
- Plausibility Notes:
- Real-world basis / lab basis: Protein-guided mineralization/crystallization concepts exist in nature and research.
- Extrapolation notes: Industrial structural deployment is speculative but physically plausible.

#### [A7-06] Bio-derived High-Performance Resin Matrix (advanced)
- Code: `MAT_BIO_HP_RESIN_MATRIX`
- Category: Bio / Polymer
- SubCategory: Advanced bio-derived matrix chemistry
- Summary: Bio-derived high-performance resin system intended for advanced composites with improved sustainability and local synthesis potential.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `3`
- Typical Use: Composite manufacture in developed colonies or eco-constrained economies.
- Not Recommended For: Extreme thermal/radiation missions unless specially tailored.
- Tradeoffs:
- Pros: Strategic supply flexibility, potentially local production.
- Cons: Chemistry stability and repeatability challenges, environmental sensitivity risks.
- Failure Modes: Matrix aging, cure variability, interface weakness.
- Plausibility Notes:
- Real-world basis / lab basis: Bio-based resin research is active.
- Extrapolation notes: High-performance aerospace-grade variants are plausible future development.

#### [A8-01] Nanograined Steel
- Code: `MAT_STEEL_NANOGRAIN`
- Category: Advanced Metal
- SubCategory: Nanostructured steel
- Summary: Steel with refined grain structure targeting strength/toughness/fatigue improvements through advanced processing.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: High-performance chassis members, improved joints, fatigue-critical structures.
- Not Recommended For: Low-quality mass production without strict process control.
- Tradeoffs:
- Pros: Potential improvements without abandoning steel ecosystem.
- Cons: Microstructure stability and scaling challenges, cost and process drift.
- Failure Modes: Grain growth in service, property inconsistency, process defects.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded nanostructuring approaches.
- Extrapolation notes: Industrial scaling is plausible in advanced civilizations.

#### [A8-02] Nanograined Titanium Alloy
- Code: `MAT_TI_NANOGRAIN`
- Category: Advanced Metal
- SubCategory: Nanostructured titanium alloy
- Summary: Titanium alloy with controlled nanograin structure for enhanced strength/fatigue performance while retaining titanium’s corrosion advantages.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium chassis, fatigue-critical aerospace members, high-G structures.
- Not Recommended For: Broad low-cost fleets.
- Tradeoffs:
- Pros: High specific performance ceiling, strong premium chassis candidate.
- Cons: Processing complexity, thermal stability concerns, qualification burden.
- Failure Modes: Grain coarsening, localized brittleness, property variance by process.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded.
- Extrapolation notes: Hard-sci-fi industrialization plausible.

#### [A8-03] Nanostructured High-Entropy Alloy
- Code: `MAT_HEA_NANO`
- Category: Advanced Metal
- SubCategory: Nanostructured HEA
- Summary: High-entropy alloy with nanostructural control for extreme tuning of strength, fatigue, and temperature behavior.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Elite civilization chassis nodes and mission-critical structures.
- Not Recommended For: Anything cost-sensitive or poorly instrumented in QA.
- Tradeoffs:
- Pros: Very high tunability and top-end potential.
- Cons: Material science + process complexity extreme, certification expensive.
- Failure Modes: Phase instability, property drift, hidden defect sensitivity.
- Plausibility Notes:
- Real-world basis / lab basis: HEA and nanostructuring research basis exists.
- Extrapolation notes: Combined mature industrial use is speculative but plausible.

#### [A8-04] Bulk Metallic Glass (BMG, structural-limited)
- Code: `MAT_BMG`
- Category: Advanced Metal
- SubCategory: Amorphous metal
- Summary: Amorphous metal class with very high strength and elastic limits, but limited by size, toughness, and brittle/shear-band failure concerns.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Small high-strength inserts, springs, precision components, localized structural elements.
- Not Recommended For: Large primary chassis members.
- Tradeoffs:
- Pros: High strength and elastic behavior in suitable geometries.
- Cons: Size limitations, brittle/shear localization risks, processing constraints.
- Failure Modes: Shear banding, brittle fracture, thermal stability issues.
- Plausibility Notes:
- Real-world basis / lab basis: Demonstrated materials family.
- Extrapolation notes: Structural use remains selective unless toughened/composited.

#### [A8-05] Bulk Metallic Glass Composite (toughened)
- Code: `MAT_BMG_COMPOSITE`
- Category: Advanced Composite Metal
- SubCategory: Toughened amorphous metal composite
- Summary: BMG-based composite engineered to improve toughness/damage tolerance while preserving some high-strength advantages.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Precision structural nodes, high-stress compact components.
- Not Recommended For: Cheap large structures.
- Tradeoffs:
- Pros: Better structural practicality than pure BMG in some roles.
- Cons: Complex processing and difficult qualification.
- Failure Modes: Interface shear failures, localized cracking, processing-induced heterogeneity.
- Plausibility Notes:
- Real-world basis / lab basis: Research-grounded toughened BMG directions.
- Extrapolation notes: Advanced but plausible for premium applications.

#### [A8-06] Functionally Graded Material (FGM structural class)
- Code: `MAT_FGM_STRUCT`
- Category: Advanced Hybrid Material Class
- SubCategory: Functionally graded material
- Summary: Material class with intentionally varying composition/properties across volume to manage stress, thermal gradient, and interfaces.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Typical Use: Interface regions, thermal transitions, advanced hull and propulsion structure transitions.
- Not Recommended For: Low-tech repair-heavy platforms.
- Tradeoffs:
- Pros: Reduces mismatch failures, tailors behavior to local loads.
- Cons: Manufacturing and verification complexity, difficult repair and rework.
- Failure Modes: Gradient process defects, unexpected weak zones, delamination/cracking at imperfect transitions.
- Plausibility Notes:
- Real-world basis / lab basis: Strong research basis with niche applications.
- Extrapolation notes: Broad structural deployment is plausible with advanced manufacturing.

#### [A8-07] Atomically Engineered Alloy (advanced)
- Code: `MAT_ALLOY_ATOMIC_ENGINEERED`
- Category: Advanced Metal
- SubCategory: Atomically engineered alloy system
- Summary: Alloy family engineered with near-atomic precision in composition and local ordering to approach practical performance ceilings.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Typical Use: Flagship chassis members, ultra-premium structural systems, civilization-grade extreme applications.
- Not Recommended For: Mass fleets or low-precision industrial chains.
- Tradeoffs:
- Pros: Near-maximum practical extraction of material class performance.
- Cons: Extreme cost, QA burden, tiny tolerance for process drift.
- Failure Modes: Catastrophic underperformance from subtle process errors, interface incompatibility with lower-grade parts.
- Plausibility Notes:
- Real-world basis / lab basis: Based on known materials science principles and atomic-scale control trends.
- Extrapolation notes: Speculative but plausible at extreme tech.

#### [A8-08] Programmable Grain Texture Alloy (advanced)
- Code: `MAT_ALLOY_PROG_TEXTURE`
- Category: Advanced Metal
- SubCategory: Texture-controlled alloy
- Summary: Alloy processed to impose spatially varying grain orientation/texture, tailoring anisotropy to expected load paths.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Optimized beams, shells, and joints in premium ships.
- Not Recommended For: Generic manufacturing, uncertain mission profiles where load path is not known.
- Tradeoffs:
- Pros: Better use of material via orientation-specific properties.
- Cons: Complex design/manufacturing integration, can underperform if loads differ from design assumptions.
- Failure Modes: Off-axis loading failures, texture drift, localized fatigue.
- Plausibility Notes:
- Real-world basis / lab basis: Texture control exists; spatial programming at scale is advanced extrapolation.
- Extrapolation notes: Physically plausible with extreme process control.

#### [A8-09] Atomic-Interface-Optimized Multi-Phase Material (advanced)
- Code: `MAT_MULTIPHASE_ATOMIC_INTERFACE_OPT`
- Category: Advanced Hybrid Material
- SubCategory: Interface-engineered multiphase material
- Summary: Multi-phase structural material where phase boundaries are engineered at near-atomic precision to improve load transfer and damage tolerance.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Typical Use: Elite critical structures, mixed-function premium components.
- Not Recommended For: Broad industrial deployment.
- Tradeoffs:
- Pros: Potentially major gains in real structural efficiency by reducing interface losses.
- Cons: Massive process and verification complexity, repair nearly impossible without advanced infrastructure.
- Failure Modes: Hidden interface defects, phase instability, catastrophic local decohesion.
- Plausibility Notes:
- Real-world basis / lab basis: Interface engineering is real; full structural-scale precision is speculative.
- Extrapolation notes: Hard-sci-fi but within physics.

#### [A8-10] Cryo-optimized Lattice Titanium Feedstock/Material System
- Code: `MAT_TI_CRYO_LATTICE_SYSTEM`
- Category: Advanced Material System
- SubCategory: Cryogenic-optimized titanium-lattice material/process package
- Summary: Not just an alloy, but a qualified material system (feedstock + process + heat treatment) tuned for cryogenic lattice structures and low-temperature fatigue.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Cryogenic tank supports, deep-space structures, low-temp high-reliability lattice members.
- Not Recommended For: General warm-service commodity hulls.
- Tradeoffs:
- Pros: Mission-optimized performance at cryogenic conditions.
- Cons: Narrow service envelope, specialized process dependence, high qualification cost.
- Failure Modes: Off-envelope thermal cycling damage, lattice node fatigue, process-induced weak nodes.
- Plausibility Notes:
- Real-world basis / lab basis: Builds on cryogenic metallurgy + lattice AM + qualification trends.
- Extrapolation notes: Speculative integration level, but physically plausible.

### B. Structure Types (Extended)

Planned scope:
- monolithic
- framed / truss / space frame
- shell grids
- honeycomb / cellular
- sandwich
- lattice / hierarchical lattice
- auxetic / graded / metamaterial-like structures

#### B. Structural Load Sharing Reference (for chassis pre-solver)

Purpose:
- define coarse recommended load partition between `members` and `structural panels`
- support early chassis graph diagnostics before full structural solving
- keep `frame-heavy` vs `shell-heavy` behavior consistent between reference and code

Definition (coarse):
- `memberShare`: fraction of global structural load path assumed to be carried by beams/rails/rings/spines/braces
- `panelShare`: fraction of global structural load path assumed to be carried by structural panels / shell closures / shear panels
- `memberShare + panelShare = 1.0`

Important:
- this is a pre-solver architectural heuristic, not a final physical law
- local concentrated loads (engine mounts, landing supports, mass anchors) still enter through local transfer members first
- exact distribution depends on material, joints, panel thickness, cutouts, and manufacturing quality

Recommended coarse ranges by `B` family (v1):
- `B1 Monolithic / Basic Sections`: `memberShare ~ 0.85..0.95`, `panelShare ~ 0.05..0.15`
- `B2 Frames / Trusses / Space Frames`: `memberShare ~ 0.82..0.92`, `panelShare ~ 0.08..0.18`
- `B3 Shell Grid (Iso-grid / Ortho-grid)`: `memberShare ~ 0.50..0.70`, `panelShare ~ 0.30..0.50`
- `B4 Honeycomb / Cellular / Foam`: `memberShare ~ 0.45..0.70`, `panelShare ~ 0.30..0.55` (depends on skins and core role)
- `B5 Sandwich Panels`: `memberShare ~ 0.40..0.65`, `panelShare ~ 0.35..0.60`
- `B6 Lattice / Hierarchical / Auxetic`: `memberShare ~ 0.60..0.85`, `panelShare ~ 0.15..0.40`
- `B7 Graded / Metamaterial-Inspired Cores`: `memberShare ~ 0.45..0.75`, `panelShare ~ 0.25..0.55` (system-dependent)

Current code alignment (`chassis.graph` pre-solver):
- `B2 Truss Frame` -> `members=0.88`, `panels=0.12`
- `B3 Iso-grid Shell` -> `members=0.60`, `panels=0.40`
- `B5 Sandwich (provisional mapping)` -> `members=0.55`, `panels=0.45`
- `B6 Hierarchical Lattice` -> `members=0.70`, `panels=0.30`

Current implementation bridge (code catalog):
- solver uses concrete profile keys (example): `STR_TRUSS_FRAME`, `STR_ISOGRID_SHELL`, `STR_SANDWICH_METAL_HONEYCOMB`, `STR_HIERARCHICAL_LATTICE`
- current runtime input still provides `structureTypeId`, so code resolves:
  - `structureTypeId -> profileKey -> load sharing profile`
- this keeps the model ready for future direct use of `B` structure codes from DB/reference data

Calibration note:
- refine these values later by detailed `B` entries (e.g. `STR_ISOGRID_SHELL`, `STR_MULTI_CORE_SANDWICH`) instead of family-level defaults.

#### B1. Monolithic / Basic Sections
- Monolithic Solid Section
- Hollow Structural Section
- Plate-and-Rib Section
- Corrugated Structural Panel
- Box Beam
- I-Beam / Spar Section (generic)
- Tubular Frame Member
- Laminated Plate (non-composite-metal stack)

#### B2. Frames / Trusses / Space Frames
- Ribbed Frame
- Truss Frame
- Space Frame
- Redundant Load-Path Frame
- Modular Node-Lock Frame
- Tensegrity-Assisted Internal Frame (advanced, restricted)
- Damage-Arrest Segmented Frame
- Morphology-Optimized Pressure Frame

#### B3. Shell Grid Structures (Iso-grid / Ortho-grid)
- Iso-grid Shell
- Ortho-grid Shell
- Stiffened Shell Panel
- Ring-and-Stringer Shell
- Graded Grid Shell (density varies by load zone)
- Thermal-Strain-Compensated Shell Grid

#### B4. Honeycomb / Cellular / Foam Structures
- Honeycomb Core (metal)
- Honeycomb Core (composite)
- Honeycomb Core (ceramic)
- Graded Honeycomb Core
- Cellular Foam Core (metal foam)
- Cellular Foam Core (ceramic foam)
- Gas-Filled Cellular Structure
- Vacuum Cellular Structure (advanced)
- Damage-Arrest Cellular Core

#### B5. Sandwich Panels / Multi-Core Panels
- Sandwich Panel (single core)
- Sandwich Panel (metal skins + honeycomb core)
- Sandwich Panel (composite skins + foam core)
- Multi-Core Sandwich Panel
- Graded-Core Sandwich Panel
- Impact-Optimized Sandwich Panel
- Thermal-Isolation Structural Sandwich Panel
- Microchannel Structural Sandwich Panel

#### B6. Lattice / Hierarchical / Auxetic Structures
- Uniform Lattice Structure
- Topology-Optimized Lattice Structure
- Hierarchical Lattice (multi-scale)
- Auxetic Lattice (re-entrant)
- Hybrid Lattice-Shell Structure
- Lattice-Reinforced Beam
- Lattice-Reinforced Panel
- Damage-Tolerant Redundant Lattice
- Cryo-optimized Lattice Geometry
- High-Damping Metageometry Lattice (advanced)

#### B7. Graded / Metamaterial-Inspired Structural Cores
- Functionally Graded Structural Core
- Metamaterial-Inspired Damping Core
- Metamaterial-Inspired Shock-Spreading Core
- Directional-Stiffness Core
- Thermal-Gradient Buffer Core
- Embedded-Channel Utility Core
- Sensor-Integrated Structural Core
- Gas-Damped Cellular Metacore (advanced)

#### B1-B7 Detailed Entries (first pass)

#### [B1-01] Monolithic Solid Section
- Code: `STR_MONOLITHIC_SOLID`
- StructureClass: Monolithic
- Summary: Baseline fully solid structural member with predictable behavior and simple analysis/manufacture.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Typical Use: Small parts, high-stress nodes, simple frames, conservative designs.
- Tradeoffs: Very robust and easy to inspect; poor mass efficiency for large structures.
- Failure Modes: Yielding, buckling (if slender), fatigue at stress concentrations.

#### [B1-02] Hollow Structural Section
- Code: `STR_HOLLOW_SECTION`
- StructureClass: Monolithic / Hollow
- Summary: Closed-section member improving stiffness-to-mass versus solid sections.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Beams, frames, pressure-adjacent supports, ship skeleton members.
- Tradeoffs: Better specific stiffness; local buckling and weld/joint quality become more important.
- Failure Modes: Wall buckling, seam/joint cracking, dent-induced instability.

#### [B1-03] Plate-and-Rib Section
- Code: `STR_PLATE_RIB`
- StructureClass: Stiffened Section
- Summary: Plate reinforced by ribs/stringers; common scalable chassis and hull logic.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Hull panels, deck structures, module casings, medium ships.
- Tradeoffs: Efficient and manufacturable; rib spacing and joint quality dominate fatigue/buckling behavior.
- Failure Modes: Plate buckling between ribs, rib-root fatigue cracks, weld heat-affected failures.

#### [B1-04] Corrugated Structural Panel
- Code: `STR_CORRUGATED_PANEL`
- StructureClass: Panel
- Summary: Corrugation adds directional stiffness with relatively simple fabrication.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Utility hull sections, internal decks, low-cost panelized structures.
- Tradeoffs: Cheap directional stiffness; anisotropic load response and difficult packaging geometry.
- Failure Modes: Local fold buckling, fatigue at corrugation roots, crush under transverse loads.

#### [B1-05] Box Beam
- Code: `STR_BOX_BEAM`
- StructureClass: Beam
- Summary: Closed-section beam with strong bending and torsional performance for chassis spines and spars.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Main longitudinal members, wing-like supports, heavy module carriers.
- Tradeoffs: Strong torsional rigidity; sensitive to local buckling and access/inspection complexity.
- Failure Modes: Panel buckling, joint crack at end fittings, internal corrosion/hidden damage.

#### [B1-06] I-Beam / Spar Section (generic)
- Code: `STR_IBEAM_SPAR`
- StructureClass: Beam
- Summary: High bending efficiency in one dominant axis, less torsion-efficient than closed sections.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Typical Use: Internal supports, known-load-path structures, industrial ships.
- Tradeoffs: Excellent for directional bending; weak to torsion/off-axis abuse.
- Failure Modes: Flange buckling, web shear buckling, lateral torsional buckling.

#### [B1-07] Tubular Frame Member
- Code: `STR_TUBULAR_MEMBER`
- StructureClass: Frame Member
- Summary: Versatile closed tubular member balancing manufacturability and specific stiffness for frames/trusses.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Space frames, roll-cage analogs, medium ship internal chassis.
- Tradeoffs: Good all-around structural efficiency; node connections become the limiting factor.
- Failure Modes: Node cracking, local dent buckling, ovalization under bending.

#### [B1-08] Laminated Plate (non-composite-metal stack)
- Code: `STR_LAMINATED_PLATE_METAL`
- StructureClass: Laminated Section
- Summary: Multi-layer plate stack used for tailored damping, crack arrest, or manufacturing convenience.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `2`
- Typical Use: Specialized hull plates, blast/shock-managed panels.
- Tradeoffs: Can improve damage tolerance; interface and corrosion issues add complexity.
- Failure Modes: Delamination/slip, interface corrosion, crack transfer between layers.

#### [B2-01] Ribbed Frame
- Code: `STR_RIBBED_FRAME`
- StructureClass: Frame
- Summary: Frame network with repeated ribs and primary load paths; baseline ship-chassis architecture.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: Small-to-large hulls, pressure and non-pressure frameworks.
- Tradeoffs: Scalable and familiar; rib spacing, joints, and load distribution strongly affect mass efficiency.
- Failure Modes: Rib junction fatigue, panel/frame buckling coupling, overload at local mounts.

#### [B2-02] Truss Frame
- Code: `STR_TRUSS_FRAME`
- StructureClass: Truss
- Summary: Axial-force-dominant structure efficient for long spans and low-mass open frameworks.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: External booms, cargo spines, internal large open volume support.
- Tradeoffs: High efficiency for axial loads; poor if real loads depart from truss assumptions or joints are weak.
- Failure Modes: Member buckling in compression, node failure, progressive collapse after member loss.

#### [B2-03] Space Frame
- Code: `STR_SPACE_FRAME`
- StructureClass: Space Frame
- Summary: 3D frame/truss network with strong load path redundancy and scalability.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Typical Use: Large hull skeletons, modular ship cores, docking structures.
- Tradeoffs: Excellent redundancy and scalability; high joint count raises mass and inspection burden.
- Failure Modes: Node fatigue, alignment-induced preload issues, buckling of slender members.

#### [B2-04] Redundant Load-Path Frame
- Code: `STR_REDUNDANT_LOAD_PATH_FRAME`
- StructureClass: Redundant Frame
- Summary: Frame intentionally designed with alternate load paths for damage tolerance and survivability.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Military ships, long-endurance civilian platforms, safety-priority hulls.
- Tradeoffs: Better damage tolerance; heavier and more complex than optimized minimal-mass frames.
- Failure Modes: Hidden overload migration, fatigue at shared nodes, maintenance complexity causing missed defects.

#### [B2-05] Modular Node-Lock Frame
- Code: `STR_MODULAR_NODE_LOCK_FRAME`
- StructureClass: Modular Frame
- Summary: Frame architecture built around repeatable node-and-member locking interfaces for modularity and rapid assembly.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Shipyards, modular fleets, field-repair-friendly platforms.
- Tradeoffs: Excellent modularity and maintenance; mass and stress penalties at interfaces.
- Failure Modes: Node lock wear, joint looseness/fretting, localized stress concentration failure.

#### [B2-06] Tensegrity-Assisted Internal Frame (advanced, restricted)
- Code: `STR_TENSEGRITY_ASSISTED`
- StructureClass: Hybrid Frame
- Summary: Uses tension networks to offload/complement compressive members in controlled internal structures.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Specialized lightweight internal structures, deployables, vibration-tuned assemblies.
- Tradeoffs: Potential mass savings and dynamic tuning; highly sensitive to preload control and damage.
- Failure Modes: Cable/preload loss, instability cascade, anchor node overload.

#### [B2-07] Damage-Arrest Segmented Frame
- Code: `STR_DAMAGE_ARREST_SEGMENTED`
- StructureClass: Segmented Frame
- Summary: Frame partitioned to limit crack/failure propagation between structural zones.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Military and high-survivability hulls, hazardous payload ships.
- Tradeoffs: Excellent fault containment; adds joints, mass, and assembly complexity.
- Failure Modes: Segment interface failures, overload transfer across barriers, maintenance neglect at interfaces.

#### [B2-08] Morphology-Optimized Pressure Frame
- Code: `STR_MORPH_OPT_PRESSURE_FRAME`
- StructureClass: Pressure Frame
- Summary: Pressure-supporting frame optimized for pressure + maneuver loads simultaneously.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Advanced pressurized ships, habitats, high-performance crew vessels.
- Tradeoffs: Better integrated mass efficiency; requires high-fidelity load modeling and manufacturing precision.
- Failure Modes: Off-design loading hotspots, local buckling, interface mismatch with non-optimized modules.

#### [B3-01] Iso-grid Shell
- Code: `STR_ISOGRID_SHELL`
- StructureClass: Shell Grid
- Summary: Triangular grid-stiffened shell with strong mass efficiency for thin-walled pressure and aerospace structures.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Pressure hull shells, tank structures, advanced chassis skins.
- Tradeoffs: Excellent shell efficiency; manufacturing, inspection, and local repair are more complex than simple ribbed shells.
- Failure Modes: Local skin buckling, crack growth at grid intersections, manufacturing defect sensitivity.

#### [B3-02] Ortho-grid Shell
- Code: `STR_ORTHOGRID_SHELL`
- StructureClass: Shell Grid
- Summary: Orthogonal grid-stiffened shell easier to fabricate/inspect than some iso-grid variants, with directional optimization.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Cylindrical hull sections, tanks, panelized shell structures.
- Tradeoffs: Good manufacturability and strong directional optimization; anisotropic stiffness can be a liability.
- Failure Modes: Directional buckling, joint fatigue at grid intersections, panel instability under off-axis loads.

#### [B3-03] Stiffened Shell Panel
- Code: `STR_STIFFENED_SHELL_PANEL`
- StructureClass: Shell Panel
- Summary: Generic shell panel with integrated stiffeners; broad practical baseline for hull and casing design.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Typical Use: General ship hulls, casings, module skins.
- Tradeoffs: Flexible and manufacturable; less mass-efficient than optimized grid shells at top performance.
- Failure Modes: Skin buckling, stiffener detachment/crack, local dent collapse.

#### [B3-04] Ring-and-Stringer Shell
- Code: `STR_RING_STRINGER_SHELL`
- StructureClass: Shell Frame Hybrid
- Summary: Classic cylindrical shell strategy combining circumferential rings and longitudinal stringers.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Typical Use: Pressure vessels, cylindrical fuselage/hull segments.
- Tradeoffs: Very practical and scalable; intersections become fatigue/inspection hotspots.
- Failure Modes: Stringer buckling, ring-frame fatigue, skin-frame interaction instability.

#### [B3-05] Graded Grid Shell (density varies by load zone)
- Code: `STR_GRADED_GRID_SHELL`
- StructureClass: Advanced Shell Grid
- Summary: Grid shell with variable stiffener density/thickness matched to local load map.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium hulls and high-performance pressure structures.
- Tradeoffs: Strong mass efficiency gains; sensitive to load modeling errors and process variability.
- Failure Modes: Underbuilt local zones, transition stress concentrations, repair mismatch after damage.

#### [B3-06] Thermal-Strain-Compensated Shell Grid
- Code: `STR_THERMAL_COMP_SHELL_GRID`
- StructureClass: Advanced Shell Grid
- Summary: Shell grid geometry tuned to reduce thermal distortion and stress under severe gradients.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Sun-facing hulls, engine-near shells, thermally cycled structures.
- Tradeoffs: Better thermal survivability; design and manufacturing complexity high.
- Failure Modes: Off-profile thermal loading hotspots, geometry drift, joint mismatch under cycling.

#### [B4-01] Honeycomb Core (metal)
- Code: `STR_HONEYCOMB_METAL`
- StructureClass: Cellular Core
- Summary: Lightweight core architecture for sandwich panels with strong stiffness-to-mass performance.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Typical Use: Hull panels, floors, lightweight structural skins.
- Tradeoffs: Great panel efficiency; poor tolerance to crushed cells and fluid ingress unless sealed.
- Failure Modes: Cell wall buckling/crush, debonding from skins, impact damage.

#### [B4-02] Honeycomb Core (composite)
- Code: `STR_HONEYCOMB_COMPOSITE`
- StructureClass: Cellular Core
- Summary: Composite honeycomb core for lightweight panels and tuned environmental behavior.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Typical Use: Lightweight premium panels, corrosion-sensitive environments.
- Tradeoffs: Lightweight and tailorable; matrix/environment effects and inspection complexity.
- Failure Modes: Cell crush, moisture/thermal degradation, skin-core debond.

#### [B4-03] Honeycomb Core (ceramic)
- Code: `STR_HONEYCOMB_CERAMIC`
- StructureClass: Cellular Core
- Summary: High-temperature honeycomb core suited to thermal-structural sandwich systems.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Hot-zone panels, thermal shields with structural duty.
- Tradeoffs: Thermal capability strong; brittle behavior and thermal shock remain concerns.
- Failure Modes: Cell fracture, thermal shock cracking, interface spallation.

#### [B4-04] Graded Honeycomb Core
- Code: `STR_HONEYCOMB_GRADED`
- StructureClass: Advanced Cellular Core
- Summary: Honeycomb with variable cell geometry/density across panel for tuned load and impact response.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium hull panels, impact-managed structures.
- Tradeoffs: Better performance targeting; harder to manufacture, inspect, and repair.
- Failure Modes: Transition zone stress concentrations, local crush bands, manufacturing inconsistency.

#### [B4-05] Cellular Foam Core (metal foam)
- Code: `STR_METAL_FOAM_CORE`
- StructureClass: Foam Core
- Summary: Metal foam core for energy absorption, thermal behavior, and moderate structural support in sandwich systems.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Blast/impact mitigation panels, multifunctional structural cores.
- Tradeoffs: Good damage absorption; property variability and lower stiffness efficiency than optimized honeycomb.
- Failure Modes: Crush localization, fatigue collapse of cell network, skin-core debond.

#### [B4-06] Cellular Foam Core (ceramic foam)
- Code: `STR_CERAMIC_FOAM_CORE`
- StructureClass: Foam Core
- Summary: High-temperature core for thermal isolation and lightweight hot-structure sandwich systems.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Thermal barriers, hot panel cores.
- Tradeoffs: High thermal resistance; brittle and impact-sensitive.
- Failure Modes: Brittle crush, thermal cracking, interface failure.

#### [B4-07] Gas-Filled Cellular Structure
- Code: `STR_CELLULAR_GAS_FILLED`
- StructureClass: Cellular
- Summary: Cellular structure intentionally containing gas for damping, thermal control, or crush behavior tuning.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Advanced damping cores, thermal/acoustic control panels.
- Tradeoffs: Multifunctionality gains; sealing complexity and leak degradation risks.
- Failure Modes: Gas leakage, cell wall fatigue, pressure imbalance causing local collapse.

#### [B4-08] Vacuum Cellular Structure (advanced)
- Code: `STR_CELLULAR_VACUUM`
- StructureClass: Cellular
- Summary: Cellular architecture using evacuated cells for thermal or mass-performance benefits, highly stability-limited.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Premium thermal-structural panels in carefully controlled roles.
- Tradeoffs: Potential high performance; extreme buckling sensitivity and manufacturing/QA burden.
- Failure Modes: Cell wall buckling, vacuum loss cascades, hidden microleaks.

#### [B4-09] Damage-Arrest Cellular Core
- Code: `STR_CELLULAR_DAMAGE_ARREST`
- StructureClass: Cellular Core
- Summary: Cellular core designed to localize crush/fracture and prevent wide-area panel failure.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Military panels, hazardous cargo containment structures.
- Tradeoffs: Better survivability; added complexity and lower peak efficiency vs optimized simple cores.
- Failure Modes: Arrest feature overload, transition debonding, repeated impact fatigue.

#### [B5-01] Sandwich Panel (single core)
- Code: `STR_SANDWICH_SINGLE_CORE`
- StructureClass: Sandwich
- Summary: Generic sandwich panel combining face skins and lightweight core for high bending stiffness per mass.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Typical Use: Hull panels, decks, fairings, module casings.
- Tradeoffs: Excellent panel efficiency; damage and debonding can be hard to detect.
- Failure Modes: Skin wrinkling, core shear failure, skin-core debonding.

#### [B5-02] Sandwich Panel (metal skins + honeycomb core)
- Code: `STR_SANDWICH_METAL_HONEYCOMB`
- StructureClass: Sandwich
- Summary: Durable and manufacturable sandwich configuration for many aerospace/space structural panels.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Ship hull skins, floors, pressure-adjacent panels.
- Tradeoffs: Strong practical baseline; can suffer impact crush and hidden debonds.
- Failure Modes: Honeycomb crush, skin buckling, adhesive/interface failure.

#### [B5-03] Sandwich Panel (composite skins + foam core)
- Code: `STR_SANDWICH_COMP_FOAM`
- StructureClass: Sandwich
- Summary: Lightweight sandwich optimized for mass efficiency and damping with moderate structural demand.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Typical Use: Lightweight hull sections, internal partitions, vibration-sensitive panels.
- Tradeoffs: Very light; foam creep and impact damage can limit life.
- Failure Modes: Foam shear failure, delamination, impact dent leading to hidden damage.

#### [B5-04] Multi-Core Sandwich Panel
- Code: `STR_SANDWICH_MULTI_CORE`
- StructureClass: Advanced Sandwich
- Summary: Panel combining multiple core types/zones for tailored stiffness, damping, and thermal behavior.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium hull panels, mission-specific structures.
- Tradeoffs: High tunability; manufacturing and inspection complexity rise sharply.
- Failure Modes: Core transition failures, differential fatigue, interface sequencing defects.

#### [B5-05] Graded-Core Sandwich Panel
- Code: `STR_SANDWICH_GRADED_CORE`
- StructureClass: Advanced Sandwich
- Summary: Core density/geometry changes across panel thickness or area to manage loads and impacts.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Impact-managed hull skins, pressure panel optimization.
- Tradeoffs: Better local optimization; sensitive to load-model mismatch and QA.
- Failure Modes: Transition shear failures, localized crush, debonding at gradient discontinuities.

#### [B5-06] Impact-Optimized Sandwich Panel
- Code: `STR_SANDWICH_IMPACT_OPT`
- StructureClass: Sandwich
- Summary: Sandwich tuned for survivability and energy absorption over peak stiffness efficiency.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Military hull zones, debris-prone ships, cargo protection.
- Tradeoffs: Better impact tolerance; heavier and less stiff than purely stiffness-optimized designs.
- Failure Modes: Repeated impact damage accumulation, core crush exhaustion, hidden delamination.

#### [B5-07] Thermal-Isolation Structural Sandwich Panel
- Code: `STR_SANDWICH_THERMAL_ISO_STRUCT`
- StructureClass: Multifunctional Sandwich
- Summary: Sandwich carrying structural load while limiting thermal transmission between zones.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Habitat shells, cryo interfaces, reactor-adjacent compartment boundaries.
- Tradeoffs: Excellent multifunctionality; thermal mismatch and interface reliability become critical.
- Failure Modes: Thermal cycling debond, core degradation, local bridge conduction causing hotspots.

#### [B5-08] Microchannel Structural Sandwich Panel
- Code: `STR_SANDWICH_MICROCHANNEL`
- StructureClass: Multifunctional Sandwich
- Summary: Structural sandwich panel with integrated channels for cooling, gas routing, or heat management.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Typical Use: Advanced thermal-control hull sections, engine-adjacent structures.
- Tradeoffs: Very high multifunctional value; leak risk and manufacturing/inspection complexity are major penalties.
- Failure Modes: Channel leakage, clogging/erosion, fatigue cracks around channel walls.

#### [B6-01] Uniform Lattice Structure
- Code: `STR_LATTICE_UNIFORM`
- StructureClass: Lattice
- Summary: Repeating lattice architecture for lightweight structural members and cores.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Typical Use: Advanced cores, lightweight beams, additive-manufactured structures.
- Tradeoffs: High specific efficiency in design regime; sensitive to node defects and off-axis loads.
- Failure Modes: Node fatigue, buckling of struts, progressive collapse after local failure.

#### [B6-02] Topology-Optimized Lattice Structure
- Code: `STR_LATTICE_TOPO_OPT`
- StructureClass: Lattice
- Summary: Lattice geometry optimized to actual load paths for near-minimum mass structures.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium chassis members, aerospace components, advanced internal frameworks.
- Tradeoffs: Excellent efficiency; fragile to load-case mismatch, hard to repair/inspect.
- Failure Modes: Unexpected overload in underrepresented load paths, node defects, fatigue hot spots.

#### [B6-03] Hierarchical Lattice (multi-scale)
- Code: `STR_LATTICE_HIERARCHICAL`
- StructureClass: Hierarchical Lattice
- Summary: Lattice-within-lattice architecture for tuned stiffness, damping, and damage progression behavior.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Civilization-grade lightweight structures, premium hull cores.
- Tradeoffs: Potentially outstanding multifunctional efficiency; extreme analysis/manufacturing/QA complexity.
- Failure Modes: Scale-coupled buckling, hidden defect amplification, repair impracticality.

#### [B6-04] Auxetic Lattice (re-entrant)
- Code: `STR_LATTICE_AUXETIC`
- StructureClass: Auxetic Lattice
- Summary: Lattice with negative effective Poisson behavior in operating regime, useful for impact and confinement response.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Protective cores, shock-sensitive equipment mounts, advanced panel inserts.
- Tradeoffs: Specialized benefits; response highly geometry- and load-regime-dependent.
- Failure Modes: Cell hinge fatigue, out-of-regime collapse, manufacturing tolerance sensitivity.

#### [B6-05] Hybrid Lattice-Shell Structure
- Code: `STR_HYBRID_LATTICE_SHELL`
- StructureClass: Hybrid
- Summary: Shell skin carrying global loads with lattice substructure managing local stiffness and mass efficiency.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Advanced hull sections, pressure-adjacent lightweight structures.
- Tradeoffs: Strong global/local optimization; shell-lattice interface becomes critical.
- Failure Modes: Interface debond/crack, local buckling transitions, node-shell fatigue.

#### [B6-06] Lattice-Reinforced Beam
- Code: `STR_LATTICE_REINFORCED_BEAM`
- StructureClass: Hybrid Beam
- Summary: Beam architecture using lattice internals or lattice-reinforced webs/flanges.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Lightweight spines, long-span supports.
- Tradeoffs: Better mass efficiency than conventional beams in right load ranges; difficult fabrication and inspection.
- Failure Modes: Internal lattice node failure, skin-web separation, buckling interactions.

#### [B6-07] Lattice-Reinforced Panel
- Code: `STR_LATTICE_REINFORCED_PANEL`
- StructureClass: Hybrid Panel
- Summary: Panel with lattice reinforcement replacing or supplementing conventional ribs/core.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium hull panels, impact- and mass-sensitive sections.
- Tradeoffs: Tunable and efficient; high QA burden and complicated damage assessment.
- Failure Modes: Local panel-lattice debond, cell collapse, fatigue at reinforcement nodes.

#### [B6-08] Damage-Tolerant Redundant Lattice
- Code: `STR_LATTICE_REDUNDANT_DAMAGE_TOL`
- StructureClass: Redundant Lattice
- Summary: Lattice architecture with deliberate redundancy and crack/failure arrest behavior.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Military/critical ships where survivability outweighs absolute mass minimum.
- Tradeoffs: Better survivability; heavier and more complex than pure optimized lattices.
- Failure Modes: Cascading node overload after hidden damage, inspection misses in deep lattice volumes.

#### [B6-09] Cryo-optimized Lattice Geometry
- Code: `STR_LATTICE_CRYO_OPT`
- StructureClass: Specialized Lattice
- Summary: Lattice geometry tuned for cryogenic contraction, fatigue, and low-temperature fracture risk.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Cryogenic tank supports and deep-space structures.
- Tradeoffs: Excellent in cryo envelope; can underperform outside intended thermal regime.
- Failure Modes: Thermal mismatch cracking, node fatigue under warm-cold cycling, off-envelope instability.

#### [B6-10] High-Damping Metageometry Lattice (advanced)
- Code: `STR_LATTICE_HIGH_DAMP_META`
- StructureClass: Metageometry Lattice
- Summary: Lattice geometry tuned for enhanced vibration damping while retaining structural function.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Precision sensor platforms, vibration-sensitive modules, advanced hull zones.
- Tradeoffs: Multifunctional dynamic control; peak stiffness/strength may be lower than simpler lattices.
- Failure Modes: Hinge/feature fatigue, damping feature degradation, manufacturing tolerance drift.

#### [B7-01] Functionally Graded Structural Core
- Code: `STR_CORE_FGM`
- StructureClass: Graded Core
- Summary: Core with spatially varying stiffness/density/material behavior to match load and thermal fields.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Typical Use: Premium sandwich systems, load-managed hull panels, thermal transitions.
- Tradeoffs: Strong optimization potential; difficult certification and repair.
- Failure Modes: Gradient weak zones, transition cracking, manufacturing drift.

#### [B7-02] Metamaterial-Inspired Damping Core
- Code: `STR_CORE_META_DAMP`
- StructureClass: Metamaterial-Inspired Core
- Summary: Core geometry tuned to dissipate vibration/impact energy in target frequency/load bands.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Sensor and crew comfort zones, precision equipment foundations.
- Tradeoffs: Excellent dynamic behavior in design band; off-band benefits may be limited.
- Failure Modes: Fatigue in damping features, band-shift from damage or manufacturing variance.

#### [B7-03] Metamaterial-Inspired Shock-Spreading Core
- Code: `STR_CORE_META_SHOCK`
- StructureClass: Metamaterial-Inspired Core
- Summary: Core designed to distribute localized shocks over wider area/time to protect underlying structures.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Typical Use: Military hull zones, cargo impact protection, docking bump-tolerant structures.
- Tradeoffs: Better shock management; added complexity and possible mass penalty.
- Failure Modes: Local feature collapse, repeated shock degradation, hidden damage accumulation.

#### [B7-04] Directional-Stiffness Core
- Code: `STR_CORE_DIRECTIONAL_STIFFNESS`
- StructureClass: Anisotropic Core
- Summary: Core engineered to be stiff in chosen directions and compliant in others.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Managed load path hulls, vibration isolation and support combinations.
- Tradeoffs: Powerful design tool; dangerous if actual loads rotate or mission changes.
- Failure Modes: Off-axis buckling/crush, anisotropy mismatch at interfaces.

#### [B7-05] Thermal-Gradient Buffer Core
- Code: `STR_CORE_THERMAL_BUFFER`
- StructureClass: Multifunctional Core
- Summary: Structural core intended to soften thermal gradients while carrying moderate loads.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Habitat boundaries, cryo/hot compartment separators, reactor-adjacent non-primary panels.
- Tradeoffs: Strong thermal-structural utility; less efficient than single-purpose cores in pure structural role.
- Failure Modes: Thermal cycling fatigue, interface debond, local conduction bridges.

#### [B7-06] Embedded-Channel Utility Core
- Code: `STR_CORE_EMBEDDED_CHANNEL_UTILITY`
- StructureClass: Utility Core
- Summary: Core with integrated channels for cabling, fluids, gases, and thermal services.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Typical Use: Service-rich ship sections, modular infrastructure hull zones.
- Tradeoffs: Huge integration benefit; leakage, maintenance, and structural complexity penalties.
- Failure Modes: Channel wall cracking, leak paths, erosion, service-structure coupling failures.

#### [B7-07] Sensor-Integrated Structural Core
- Code: `STR_CORE_SENSOR_INTEGRATED`
- StructureClass: Smart Structural Core
- Summary: Structural core with embedded sensing network for health monitoring and predictive maintenance.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Typical Use: High-value ships, long-duration missions, critical hull sections.
- Tradeoffs: Improves diagnostics and safety; raises manufacturing and maintenance complexity.
- Failure Modes: Sensor network degradation, false confidence from partial sensor failure, embedding defects.

#### [B7-08] Gas-Damped Cellular Metacore (advanced)
- Code: `STR_CORE_GAS_DAMPED_META`
- StructureClass: Advanced Metacore
- Summary: Cellular metacore using controlled gas-filled cells for damping/thermal/structural tuning.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Typical Use: Elite civilization hulls, precision ships, shock-managed premium structures.
- Tradeoffs: Exceptional multifunctional tuning potential; sealing, QA, and lifecycle management are severe challenges.
- Failure Modes: Gas leakage, cell pressure imbalance, dynamic response drift, hidden cell damage.

### C. Manufacturing Processes (Extended)

Planned scope:
- traditional
- precision metalworking
- powder metallurgy and densification
- additive manufacturing
- composite manufacturing
- coatings/interface engineering
- bio-template and in-space advanced manufacturing

#### C1. Conventional Forming
- Casting
- Precision Casting
- Forging
- Isothermal Forging
- Rolling / Forming
- Extrusion
- Conventional Machining
- Heat Treatment (baseline controlled)
- Surface Shot Peening (conventional)

#### C2. Precision / High-Performance Metal Manufacturing
- High-Precision CNC Machining
- Cryogenic Machining
- Isothermal Precision Forming
- Superplastic Forming (restricted alloys)
- Friction Stir Processing
- Laser Surface Treatment
- Laser Peening (advanced)
- Electron Beam Processing
- Residual Stress Relief (advanced controlled)
- Directed Crystallographic Texture Processing (advanced)

#### C3. Powder Metallurgy / HIP / Sintering
- Powder Metallurgy (general)
- Hot Isostatic Pressing (HIP)
- PM + HIP Hybrid Route
- Vacuum Sintering
- Controlled-Atmosphere Sintering
- Spark Plasma Sintering (SPS)
- Field-Assisted Sintering
- Advanced Powder Classification / Purification
- Near-Net PM Structural Part Route

#### C4. Additive Manufacturing (Metal / Hybrid)
- Additive Manufacturing (PBF metal)
- Additive Manufacturing (DED metal)
- Additive Manufacturing (wire-fed arc/beam class)
- Additive + HIP Hybrid Manufacturing
- Multi-axis Hybrid Additive + CNC
- In-situ Monitored Additive Manufacturing
- In-situ Alloy Composition Tuning (advanced)
- Large-Part Additive Segment Fabrication
- Lattice-Optimized Additive Fabrication
- Cold Spray Additive / Repair (structural-limited)
- Ultrasonic Additive Consolidation (specialized)

#### C5. Composite Manufacturing
- Filament Winding
- Hand/Automated Layup (baseline)
- Autoclave Composite Cure
- Out-of-Autoclave Composite Cure (advanced)
- Resin Transfer Molding (RTM)
- Vacuum-Assisted Resin Infusion
- Braided Composite Preform Manufacturing
- Woven Composite Preform Manufacturing
- Pultrusion (continuous profiles)
- Thermoplastic Composite Consolidation (advanced)

#### C6. Ceramic / Cermet Processing
- Ceramic Slip Casting (structural-limited)
- Ceramic Pressing + Sintering
- Advanced Ceramic Sintering (controlled atmosphere)
- Sol-Gel Ceramic Processing
- Cermet Powder Processing
- CMC Infiltration / Densification Route
- Polymer Infiltration and Pyrolysis (PIP) for CMC
- CVI/CVD-based ceramic composite densification (advanced)
- Functionally Graded Ceramic-Cermet Processing (advanced)

#### C7. Surface / Interface Engineering (CVD/PVD/ALD/etc.)
- Chemical Vapor Deposition (CVD)
- Physical Vapor Deposition (PVD)
- Atomic Layer Deposition (ALD)
- Plasma Electrolytic Oxidation (PEO, compatible alloys)
- Thermal Spray Coatings
- Cold Spray Coatings
- Diffusion Coatings / Surface Alloying
- Ion Implantation / Surface Modification (advanced)
- Atomic-Interface Engineering (advanced)
- Nano-thickness Interface Layer Control (advanced)

#### C8. Advanced / Civilization-Grade Manufacturing
- Bio-template Growth + Mineralization Manufacturing
- Protein-guided Crystal Template Manufacturing (advanced)
- Self-Assembled Micro-Lattice Template Production
- Programmable Microstructure Foundry (advanced)
- Full In-situ Tomography Closed-Loop Manufacturing
- Full Defect-Cartography Guided Manufacturing
- Vacuum Megastructure Fabrication Line
- In-Space Vacuum Fabrication Chain
- Orbital Ultra-Pure Alloy Production
- Atomically Tuned Diffusion Interface Fabrication
- Near-Atomic Precision Surface/Interface Finishing

### D. Assembly Processes (Extended)

Planned scope:
- mechanical fastening
- welding and joining
- bonding
- hybrid joining
- robotic precision assembly
- vacuum / in-space precision assembly

#### D1. Mechanical Fastening & Modular Assembly
- Bolted Mechanical Assembly
- Riveted Assembly
- Pinned Modular Assembly
- Clamp-Band Structural Assembly
- Smart Fastener Assembly (preload monitored)
- Replaceable Sacrificial Joint Module Assembly
- Modular Node-Lock Assembly
- Field-Repair-Oriented Mechanical Assembly

#### D2. Welding & Thermal Joining
- Arc Welding (structural baseline)
- TIG/MIG-class Precision Welding
- Laser Welding
- Electron Beam Welding
- Friction Stir Welding
- Brazing (structural-limited / sealed assemblies)
- Diffusion-Bonded Panel Assembly
- In-situ Stress-Relieved Welded Assembly (advanced)
- Vacuum Thermal Joining (advanced)

#### D3. Bonding & Hybrid Bonding
- Structural Adhesive Bonding
- Adhesive + Mechanical Hybrid Bonding
- Bondline Thickness Controlled Bonding
- Nano-thickness Controlled Interface Bonding (advanced)
- Mixed-Material Structural Bonding
- Sealed Pressure-Hull Bonded Joint (advanced/restricted)
- Repair-Patch Bonded Assembly Method

#### D4. Composite-Integrated Assembly
- Composite Layup Integrated Assembly
- Co-cured Composite Joint Assembly
- Co-bonded Composite Joint Assembly
- Composite-to-Metal Transition Joint Assembly
- Braided Joint Region Integrated Assembly
- Embedded Insert Composite Assembly
- Composite Pressure Shell Integrated Assembly

#### D5. Precision Robotic / Metrology-Guided Assembly
- Robotic Precision Assembly
- Metrology-Guided Alignment Assembly
- Active Alignment with Interferometry
- Thermal-Compensated Precision Jig Assembly
- Vacuum Alignment Assembly
- Self-Jigging Precision Assembly
- Smart Fastener Telemetry-Guided Assembly
- Distributed Robotic Swarm Assembly (advanced)

#### D6. Advanced / Civilization-Grade Assembly
- Zero-Defect Certified Assembly Workflow
- Full Digital Twin Guided Assembly
- Joint-by-Joint Confidence Tagged Assembly
- Autonomous Recertifying Assembly Pipeline
- In-Space Precision Hull Assembly
- Radiation-Shielded Precision Assembly
- Zero-Backlash Docking-Aligned Hull Assembly
- Large-Scale Segment Stitch Assembly (ultra-precision)
- Atomic-Interface-Assisted Critical Joint Assembly (advanced)

#### C1-D6 Detailed Entries (first pass)

#### [C1-01] Casting
- Code: `MFG_CASTING`
- ProcessCategory: Conventional Forming
- Summary: Baseline melt-and-cast route for complex low-cost parts.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Shape complexity, cost, scale.
- Weaknesses: Porosity/inclusions/shrinkage defects, lower confidence for fatigue-critical parts.
- Main Defect Risks: Voids, inclusions, hot tearing, segregation.

#### [C1-02] Precision Casting
- Code: `MFG_PRECISION_CAST`
- ProcessCategory: Conventional Forming
- Summary: Improved casting with tighter dimensional and defect control.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Strengths: Better geometry and surface quality than baseline casting.
- Weaknesses: Still defect-sensitive; process control cost higher.
- Main Defect Risks: Shrinkage porosity, shell/process variability.

#### [C1-03] Forging
- Code: `MFG_FORGING`
- ProcessCategory: Conventional Forming
- Summary: Wrought shaping route with strong structural properties and toughness.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Strengths: Better grain flow, toughness, fatigue performance.
- Weaknesses: Tooling/geometry limits, cost for complex shapes.
- Main Defect Risks: Laps, folds, improper die fill, heat-treatment errors.

#### [C1-04] Isothermal Forging
- Code: `MFG_ISOTHERMAL_FORGING`
- ProcessCategory: Conventional Forming
- Summary: Controlled-temperature forging for difficult alloys and improved precision.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Better property control in high-performance alloys.
- Weaknesses: Expensive, slower, specialized equipment.
- Main Defect Risks: Temperature drift, microstructure inconsistency.

#### [C1-05] Rolling / Forming
- Code: `MFG_ROLL_FORM`
- ProcessCategory: Conventional Forming
- Summary: High-throughput production of plates, sheets, profiles, and formed structural stock.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Throughput, cost, scalable shipbuilding feedstock.
- Weaknesses: Geometry limited, anisotropy and residual stress effects.
- Main Defect Risks: Thickness variation, residual stress, edge cracking.

#### [C1-06] Extrusion
- Code: `MFG_EXTRUSION`
- ProcessCategory: Conventional Forming
- Summary: Efficient route for long constant-section structural members.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Strengths: Excellent for profiles and modular frame members.
- Weaknesses: Section constraints, alloy limitations, die costs.
- Main Defect Risks: Surface tearing, internal cracking, dimensional drift.

#### [C1-07] Conventional Machining
- Code: `MFG_MACHINING_CONV`
- ProcessCategory: Conventional Forming
- Summary: Baseline subtractive finishing and part generation.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Flexibility, repair/rework friendly.
- Weaknesses: Waste, slower for complex geometry, operator dependence.
- Main Defect Risks: Tolerance error, tool marks, thermal distortion.

#### [C1-08] Heat Treatment (baseline controlled)
- Code: `MFG_HEAT_TREAT_BASE`
- ProcessCategory: Conventional Processing
- Summary: Baseline thermal treatment to tune strength/toughness.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Major property gains for many alloys.
- Weaknesses: Uniformity and recipe control can be limiting.
- Main Defect Risks: Over/under treatment, distortion, embrittlement.

#### [C1-09] Surface Shot Peening (conventional)
- Code: `MFG_SHOT_PEEN_BASE`
- ProcessCategory: Surface Enhancement
- Summary: Introduces compressive surface stresses to improve fatigue life.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Strengths: Good fatigue improvement at modest cost.
- Weaknesses: Surface/process dependent, coverage control matters.
- Main Defect Risks: Inconsistent coverage, overpeening, surface damage.

#### [C2-01] High-Precision CNC Machining
- Code: `MFG_CNC_PRECISION`
- ProcessCategory: Precision Metal Manufacturing
- Summary: Precision subtractive route with strong dimensional repeatability and finishing quality.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Precision, repeatability, complex part finishing.
- Weaknesses: Cost/time, waste for large parts.
- Main Defect Risks: Tool wear drift, chatter, thermal distortion.

#### [C2-02] Cryogenic Machining
- Code: `MFG_CRYO_MACHINING`
- ProcessCategory: Precision Metal Manufacturing
- Summary: Machining with cryogenic cooling to improve tool life and material response in difficult alloys.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Strengths: Better machining of Ti/superalloys, cleaner surfaces.
- Weaknesses: Equipment complexity, process tuning burden.
- Main Defect Risks: Thermal shock microdamage, process instability.

#### [C2-03] Superplastic Forming (restricted alloys)
- Code: `MFG_SUPERPLASTIC_FORM`
- ProcessCategory: Precision Forming
- Summary: Produces complex thin-walled shapes in suitable alloys with low residual stresses.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Complex geometries, good surface/shape quality.
- Weaknesses: Slow cycle times, alloy/temperature window limits.
- Main Defect Risks: Thickness nonuniformity, grain growth, local thinning rupture.

#### [C2-04] Friction Stir Processing
- Code: `MFG_FRICTION_STIR_PROCESS`
- ProcessCategory: Precision Metal Manufacturing
- Summary: Solid-state process for local microstructure refinement and surface-layer improvement.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Strengths: Improves local properties, low melt-related defects.
- Weaknesses: Tooling/material limitations and path planning complexity.
- Main Defect Risks: Tunnel defects, insufficient mixing, local property inconsistency.

#### [C2-05] Laser Surface Treatment
- Code: `MFG_LASER_SURFACE_TREAT`
- ProcessCategory: Surface Engineering
- Summary: Targeted surface hardening/melting/alloying route for local property tuning.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Localized enhancement, low bulk distortion.
- Weaknesses: Narrow process windows, residual stress effects.
- Main Defect Risks: Cracking, uneven treatment depth, thermal distortion.

#### [C2-06] Laser Peening (advanced)
- Code: `MFG_LASER_PEEN`
- ProcessCategory: Surface Engineering
- Summary: High-end fatigue-life enhancement via deep compressive residual stress introduction.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Strong fatigue benefit on critical components.
- Weaknesses: Expensive and targeted, not universal.
- Main Defect Risks: Coverage errors, local surface damage if misapplied.

#### [C2-07] Electron Beam Processing
- Code: `MFG_EBEAM_PROCESS`
- ProcessCategory: Precision / Beam Processing
- Summary: Vacuum beam processing for welding/melting/refinement in high-value parts.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: High energy density and controlled vacuum environment.
- Weaknesses: Equipment complexity, vacuum requirement, scale constraints.
- Main Defect Risks: Porosity, cracking, beam parameter drift.

#### [C2-08] Directed Crystallographic Texture Processing (advanced)
- Code: `MFG_TEXTURE_DIRECTED`
- ProcessCategory: Advanced Precision Metal Manufacturing
- Summary: Process family targeting spatial texture control to tune anisotropic performance.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Strengths: Can materially improve load-path efficiency.
- Weaknesses: Extreme process and QA complexity.
- Main Defect Risks: Texture inconsistency, off-axis underperformance.

#### [C3-01] Powder Metallurgy (general)
- Code: `MFG_PM_GENERAL`
- ProcessCategory: PM / Sintering
- Summary: Powder route for near-net-shape components and hard-to-work alloys.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Shape efficiency, material utilization.
- Weaknesses: Density/porosity control and fatigue concerns without densification.
- Main Defect Risks: Porosity, contamination, incomplete sintering.

#### [C3-02] Hot Isostatic Pressing (HIP)
- Code: `MFG_HIP`
- ProcessCategory: PM / Densification
- Summary: High-pressure/high-temperature densification to reduce porosity and improve reliability.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Big quality boost for cast/PM/additive parts.
- Weaknesses: Cost, cycle time, equipment scale limits.
- Main Defect Risks: Incomplete closure, residual defects, parameter nonuniformity.

#### [C3-03] PM + HIP Hybrid Route
- Code: `MFG_PM_HIP_HYBRID`
- ProcessCategory: PM / Densification
- Summary: Combined route for high-quality near-net-shape structural parts.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Strong property consistency and reduced porosity.
- Weaknesses: Expensive and qualification-heavy.
- Main Defect Risks: Powder contamination, trapped defects, process drift.

#### [C3-04] Vacuum Sintering
- Code: `MFG_SINTER_VAC`
- ProcessCategory: PM / Sintering
- Summary: Cleaner sintering environment for sensitive powders and better contamination control.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Improved chemistry control and part quality.
- Weaknesses: Equipment complexity and throughput penalties.
- Main Defect Risks: Incomplete densification, distortion, contamination leaks.

#### [C3-05] Spark Plasma Sintering (SPS)
- Code: `MFG_SPS`
- ProcessCategory: Advanced Sintering
- Summary: Fast sintering route with good control for advanced materials and microstructures.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Enables difficult materials and microstructure preservation.
- Weaknesses: Scale limits and tooling complexity.
- Main Defect Risks: Temperature nonuniformity, incomplete bonding, cracking.

#### [C3-06] Field-Assisted Sintering
- Code: `MFG_FIELD_ASSIST_SINTER`
- ProcessCategory: Advanced Sintering
- Summary: Electric/field-assisted densification for advanced powders and composites.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Strengths: Microstructure control potential and difficult-material capability.
- Weaknesses: Complex scale-up and process predictability.
- Main Defect Risks: Nonuniform densification, field-induced gradients, hidden weak zones.

#### [C4-01] Additive Manufacturing (PBF metal)
- Code: `MFG_AM_PBF_METAL`
- ProcessCategory: Additive Manufacturing
- Summary: Powder-bed fusion route for complex parts and lattices with high geometric freedom.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Geometry complexity, lattice integration, part consolidation.
- Weaknesses: Defect control, residual stress, throughput and build size constraints.
- Main Defect Risks: Porosity, lack of fusion, residual stress cracking.

#### [C4-02] Additive Manufacturing (DED metal)
- Code: `MFG_AM_DED_METAL`
- ProcessCategory: Additive Manufacturing
- Summary: Directed energy deposition for larger builds, repair, and local reinforcement.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Large-part capability and repair utility.
- Weaknesses: Surface finish and precision usually lower than PBF.
- Main Defect Risks: Dilution/composition drift, porosity, heat accumulation defects.

#### [C4-03] Additive + HIP Hybrid Manufacturing
- Code: `MFG_AM_HIP_HYBRID`
- ProcessCategory: Additive Manufacturing
- Summary: Additive geometry freedom combined with HIP quality recovery for structural use.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Major reliability uplift for AM structural parts.
- Weaknesses: Cost and process-chain complexity.
- Main Defect Risks: Trapped unhealed defects, distortion, qualification gaps.

#### [C4-04] In-situ Monitored Additive Manufacturing
- Code: `MFG_AM_INSITU_MON`
- ProcessCategory: Additive Manufacturing
- Summary: AM with process sensing and feedback for defect reduction and traceability.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `2`
- Strengths: Better repeatability and QA confidence.
- Weaknesses: Data/controls burden and validation complexity.
- Main Defect Risks: Sensor blind spots, false confidence, control-loop instability.

#### [C4-05] In-situ Alloy Composition Tuning (advanced)
- Code: `MFG_AM_ALLOY_TUNING`
- ProcessCategory: Advanced Additive Manufacturing
- Summary: Dynamic composition adjustment during build to create locally optimized properties.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Extreme local optimization potential.
- Weaknesses: Massive qualification complexity and process risk.
- Main Defect Risks: Composition gradients out of spec, phase instability, hidden weak transitions.

#### [C4-06] Lattice-Optimized Additive Fabrication
- Code: `MFG_AM_LATTICE_OPT`
- ProcessCategory: Additive Manufacturing
- Summary: Production process stack specialized for reliable lattice structures and node quality.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Strengths: Better lattice reliability than generic AM routes.
- Weaknesses: Specialized and expensive; still inspection-intensive.
- Main Defect Risks: Node defects, strut dimensional drift, trapped powder issues.

#### [C5-01] Filament Winding
- Code: `MFG_COMP_FILAMENT_WIND`
- ProcessCategory: Composite Manufacturing
- Summary: Efficient route for axisymmetric composite pressure and tubular structures.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Strong fiber alignment and repeatability for tubes/tanks.
- Weaknesses: Geometry constraints.
- Main Defect Risks: Void content, fiber path error, resin cure defects.

#### [C5-02] Autoclave Composite Cure
- Code: `MFG_COMP_AUTOCLAVE`
- ProcessCategory: Composite Manufacturing
- Summary: High-quality composite curing route for aerospace-grade laminates.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Low voids, high consistency, strong structural performance.
- Weaknesses: Cost, cycle time, size limitations.
- Main Defect Risks: Cure nonuniformity, trapped volatiles, layup errors.

#### [C5-03] Out-of-Autoclave Composite Cure (advanced)
- Code: `MFG_COMP_OOA`
- ProcessCategory: Composite Manufacturing
- Summary: Lower-infrastructure composite route targeting aerospace-like performance with advanced materials/process control.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Larger/cheaper production potential vs autoclave.
- Weaknesses: Tighter process discipline needed to match quality.
- Main Defect Risks: Voids, cure variability, bond defects.

#### [C5-04] Resin Transfer Molding (RTM)
- Code: `MFG_COMP_RTM`
- ProcessCategory: Composite Manufacturing
- Summary: Closed-mold composite route suitable for repeatable structural components.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Repeatability and production scaling.
- Weaknesses: Tooling and flow/cure control complexity.
- Main Defect Risks: Dry spots, voids, incomplete infiltration.

#### [C5-05] Thermoplastic Composite Consolidation (advanced)
- Code: `MFG_COMP_THERMOPLASTIC_CONSOL`
- ProcessCategory: Composite Manufacturing
- Summary: Advanced route enabling tougher and more repairable composite structures.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Strengths: Damage tolerance and processing speed opportunities.
- Weaknesses: Consolidation quality and process hardware complexity.
- Main Defect Risks: Poor fusion, porosity, residual stress warpage.

#### [C6-01] Ceramic Pressing + Sintering
- Code: `MFG_CER_PRESS_SINTER`
- ProcessCategory: Ceramic Processing
- Summary: Core route for ceramic components with good repeatability in supported geometries.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Mature ceramic route.
- Weaknesses: Brittleness and defect sensitivity remain high.
- Main Defect Risks: Cracks, porosity, shrinkage distortion.

#### [C6-02] Sol-Gel Ceramic Processing
- Code: `MFG_CER_SOL_GEL`
- ProcessCategory: Ceramic Processing
- Summary: Chemistry-driven route for coatings and some advanced ceramic architectures.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Strengths: Fine compositional/microstructural control.
- Weaknesses: Scaling and processing complexity.
- Main Defect Risks: Cracking during drying/shrinkage, chemistry drift.

#### [C6-03] CMC Infiltration / Densification Route
- Code: `MFG_CMC_INFIL_DENS`
- ProcessCategory: Ceramic Composite Processing
- Summary: Multi-step route to build ceramic matrix composites around fiber preforms.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Enables CMC structural applications.
- Weaknesses: Expensive, slow, hard to verify internally.
- Main Defect Risks: Incomplete infiltration, matrix cracks, interface inconsistency.

#### [C6-04] CVI/CVD-based ceramic composite densification (advanced)
- Code: `MFG_CMC_CVI_CVD_DENS`
- ProcessCategory: Ceramic Composite Processing
- Summary: Vapor-phase densification route for high-performance CMCs and advanced ceramic structures.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Strengths: Fine control and high-temperature capable systems.
- Weaknesses: Slow, expensive, difficult large-scale throughput.
- Main Defect Risks: Density gradients, incomplete densification, residual porosity.

#### [C7-01] Chemical Vapor Deposition (CVD)
- Code: `MFG_CVD`
- ProcessCategory: Surface / Interface Engineering
- Summary: Vapor deposition route for coatings and interface layers with strong property control.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: High-quality coatings and functional layers.
- Weaknesses: Process cost, temperature/chemistry constraints.
- Main Defect Risks: Coating nonuniformity, adhesion failure, residual stresses.

#### [C7-02] Physical Vapor Deposition (PVD)
- Code: `MFG_PVD`
- ProcessCategory: Surface / Interface Engineering
- Summary: Thin-film/coating route for wear, barrier, and interface tuning.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Precise surface modification.
- Weaknesses: Often thin-layer-limited for heavy structural effects.
- Main Defect Risks: Poor adhesion, thickness variation, pinholes.

#### [C7-03] Atomic Layer Deposition (ALD)
- Code: `MFG_ALD`
- ProcessCategory: Surface / Interface Engineering
- Summary: Atomic-scale conformal coating process for barrier/interface engineering.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Exceptional conformality and interface control.
- Weaknesses: Slow throughput and limited direct bulk-structural role.
- Main Defect Risks: Incomplete coverage in complex industrial flows, contamination, process drift.

#### [C7-04] Atomic-Interface Engineering (advanced)
- Code: `MFG_ATOMIC_INTERFACE_ENGINEERING`
- ProcessCategory: Advanced Interface Engineering
- Summary: Advanced process family for controlling phase boundaries and interfacial chemistry in structural systems.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Potentially huge gains in real-world joint/material efficiency.
- Weaknesses: Extreme complexity and verification burden.
- Main Defect Risks: Invisible interface defects, process reproducibility failure.

#### [C8-01] Bio-template Growth + Mineralization Manufacturing
- Code: `MFG_BIO_TEMPLATE_MINERAL`
- ProcessCategory: Advanced / Civilization-Grade Manufacturing
- Summary: Uses bio-grown templates followed by mineralization/infiltration to create complex structural architectures.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `4`
- Strengths: Low-waste complex geometry and hierarchical structures.
- Weaknesses: Biological variability, multi-stage QA difficulty.
- Main Defect Risks: Incomplete conversion, contamination, geometry drift.

#### [C8-02] Full In-situ Tomography Closed-Loop Manufacturing
- Code: `MFG_CLOSED_LOOP_TOMO`
- ProcessCategory: Advanced / Civilization-Grade Manufacturing
- Summary: Manufacturing with continuous internal scanning and process correction.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Massive reduction in hidden defects and better traceability.
- Weaknesses: Enormous data/compute/process complexity and cost.
- Main Defect Risks: Sensor blind spots, model/control errors, false confidence.

#### [C8-03] Full Defect-Cartography Guided Manufacturing
- Code: `MFG_DEFECT_CARTO_GUIDED`
- ProcessCategory: Advanced / Civilization-Grade Manufacturing
- Summary: Builds and certifies parts with near-complete defect maps, feeding structural calculations directly.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Enables lower uncertainty and tighter real margins without magic.
- Weaknesses: Extreme cost and infrastructure requirements.
- Main Defect Risks: Mapping interpretation errors, missed sub-threshold critical defects.

#### [C8-04] In-Space Vacuum Fabrication Chain
- Code: `MFG_INSPACE_VAC_CHAIN`
- ProcessCategory: Advanced / Civilization-Grade Manufacturing
- Summary: Orbital/vacuum production chain for large or ultra-clean structures impossible or inefficient on planets.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `3`
- Strengths: Large-scale precision, unique process windows, reduced gravity-related defects in some routes.
- Weaknesses: Massive infrastructure cost and logistical complexity.
- Main Defect Risks: Distributed process mismatch, alignment drift, supply contamination.

#### [C8-05] Near-Atomic Precision Surface/Interface Finishing
- Code: `MFG_NEAR_ATOMIC_FINISHING`
- ProcessCategory: Advanced / Civilization-Grade Manufacturing
- Summary: Ultra-precision finishing and interface preparation for critical structural performance extraction.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Improves fatigue, joints, sealing, and interface repeatability.
- Weaknesses: Costly and usually only justified on critical parts.
- Main Defect Risks: Contamination, process overfit, hidden subsurface damage if coupled with poor upstream routes.

#### [D1-01] Bolted Mechanical Assembly
- Code: `ASM_BOLTED`
- AssemblyCategory: Mechanical Fastening
- Summary: Baseline serviceable assembly route with strong repairability and modularity.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Easy maintenance, disassembly, field repair.
- Weaknesses: Joint mass penalties, loosening/fretting, stress concentration.
- Failure Modes: Bolt preload loss, fatigue at holes, fretting wear.

#### [D1-02] Riveted Assembly
- Code: `ASM_RIVETED`
- AssemblyCategory: Mechanical Fastening
- Summary: Established fastening route for sheet/shell structures with predictable production methods.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Proven shell assembly, good distributed fastening behavior.
- Weaknesses: Labor/joint count heavy, leak paths and fatigue at holes.
- Failure Modes: Rivet loosening, crack initiation at fastener rows, seal degradation.

#### [D1-03] Modular Node-Lock Assembly
- Code: `ASM_NODE_LOCK_MODULAR`
- AssemblyCategory: Mechanical / Modular
- Summary: Repeatable modular structural node joining system for fast ship construction and replacement.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Strengths: Speed, modularity, maintainability.
- Weaknesses: Joint penalties and node standardization constraints.
- Failure Modes: Lock wear, misalignment preload, local overload at nodes.

#### [D1-04] Field-Repair-Oriented Mechanical Assembly
- Code: `ASM_FIELD_REPAIR_MECH`
- AssemblyCategory: Mechanical
- Summary: Assembly philosophy prioritizing accessibility and repair over peak structural efficiency.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Excellent serviceability and uptime.
- Weaknesses: Mass and volume overheads, lower ultimate efficiency.
- Failure Modes: Fastener loosening, cumulative tolerance stack-up, seal failures.

#### [D2-01] Arc Welding (structural baseline)
- Code: `ASM_WELD_ARC_BASE`
- AssemblyCategory: Welding
- Summary: Baseline welded structural assembly route for metals.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Strong joints and high throughput.
- Weaknesses: Heat-affected zones, residual stress, operator/process dependence.
- Failure Modes: HAZ cracking, porosity, distortion, fatigue at weld toes.

#### [D2-02] Laser Welding
- Code: `ASM_WELD_LASER`
- AssemblyCategory: Welding
- Summary: Precision welding with low heat input and good automation potential.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Precision and reduced distortion.
- Weaknesses: Fit-up precision demands and equipment cost.
- Failure Modes: Lack of fusion, cracking, alignment-sensitive defects.

#### [D2-03] Electron Beam Welding
- Code: `ASM_WELD_EBEAM`
- AssemblyCategory: Welding
- Summary: Vacuum high-energy welding for critical high-integrity joints.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Deep penetration, high-quality joints in controlled environment.
- Weaknesses: Vacuum infrastructure and part-size constraints.
- Failure Modes: Porosity, hot cracking, setup/alignment defects.

#### [D2-04] Friction Stir Welding
- Code: `ASM_WELD_FSW`
- AssemblyCategory: Solid-State Joining
- Summary: Solid-state joining route with reduced melt defects and good fatigue performance (material-dependent).
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Strong joints, low distortion, good for aluminum and some advanced alloys.
- Weaknesses: Tooling and geometry access constraints.
- Failure Modes: Tunnel defects, root flaws, local softening.

#### [D2-05] Diffusion-Bonded Panel Assembly
- Code: `ASM_DIFFUSION_PANEL`
- AssemblyCategory: Diffusion Bonding
- Summary: High-integrity panel joining route for advanced metals and panel architectures.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `2`
- Strengths: High joint efficiency and low stress concentrations when controlled.
- Weaknesses: Expensive, surface prep critical, difficult field repair.
- Failure Modes: Incomplete bond zones, contamination-driven weak interfaces, hidden defects.

#### [D2-06] In-situ Stress-Relieved Welded Assembly (advanced)
- Code: `ASM_WELD_STRESS_RELIEVED_INSITU`
- AssemblyCategory: Advanced Welding
- Summary: Welding sequence with integrated stress management and metrology to reduce residual stress penalties.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `3`
- Strengths: Better dimensional control and fatigue life than conventional welding.
- Weaknesses: Process planning and instrumentation overhead.
- Failure Modes: Stress relief mismatch, sequencing errors, hidden weld defects still possible.

#### [D3-01] Structural Adhesive Bonding
- Code: `ASM_ADHESIVE_STRUCT`
- AssemblyCategory: Bonding
- Summary: Bonded joints distributing load over area; valuable in composites and mixed structures.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Low stress concentrations, smooth load transfer, mixed-material compatibility.
- Weaknesses: Surface prep critical, environmental aging concerns, inspection difficulty.
- Failure Modes: Adhesive degradation, peel failures, bondline contamination.

#### [D3-02] Adhesive + Mechanical Hybrid Bonding
- Code: `ASM_HYBRID_BOND_MECH`
- AssemblyCategory: Hybrid Bonding
- Summary: Combines bonding and fasteners for redundancy and improved damage tolerance.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Better fail-safe behavior and mixed load handling.
- Weaknesses: Added mass/complexity, galvanic and interface management.
- Failure Modes: Adhesive degradation plus fastener fatigue, load redistribution surprises.

#### [D3-03] Bondline Thickness Controlled Bonding
- Code: `ASM_BONDLINE_CONTROLLED`
- AssemblyCategory: Bonding
- Summary: Precision-controlled bondline process improving repeatability and structural confidence.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Better joint consistency and predictable performance.
- Weaknesses: Process discipline and fixtures required.
- Failure Modes: Local bondline variation, cure defects, contamination.

#### [D3-04] Nano-thickness Controlled Interface Bonding (advanced)
- Code: `ASM_NANO_INTERFACE_BOND`
- AssemblyCategory: Advanced Bonding
- Summary: Ultra-precision interface control to improve adhesion and durability in critical joints.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Potentially very high joint reliability in advanced materials.
- Weaknesses: Extreme prep/process/QA complexity.
- Failure Modes: Invisible contamination defects, interface drift, over-optimized narrow process windows.

#### [D4-01] Composite Layup Integrated Assembly
- Code: `ASM_COMP_LAYUP_INTEGRATED`
- AssemblyCategory: Composite Assembly
- Summary: Structural features and joints integrated during composite layup to reduce fastener count and mass.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Strengths: Good mass efficiency and part consolidation.
- Weaknesses: Hard repair and strong dependency on manufacturing quality.
- Failure Modes: Delamination at integrated joints, cure defects, insert pullout.

#### [D4-02] Co-cured Composite Joint Assembly
- Code: `ASM_COMP_COCURED`
- AssemblyCategory: Composite Assembly
- Summary: Composite joint formed during cure for high integration and low mass.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: High integration and low joint mass.
- Weaknesses: Repair difficulty and process sensitivity.
- Failure Modes: Delamination, incomplete cure, fiber waviness at joints.

#### [D4-03] Composite-to-Metal Transition Joint Assembly
- Code: `ASM_COMP_TO_METAL_TRANSITION`
- AssemblyCategory: Composite Assembly
- Summary: Specialized joining strategy for dissimilar material interfaces in practical ship structures.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Enables multifunctional hybrid structures.
- Weaknesses: Thermal mismatch and galvanic/corrosion management complexity.
- Failure Modes: Interface debonding, thermal cycling cracks, galvanic degradation.

#### [D5-01] Robotic Precision Assembly
- Code: `ASM_ROBOTIC_PRECISION`
- AssemblyCategory: Precision Robotic
- Summary: Automated assembly with repeatability and reduced human variability.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Precision, repeatability, traceability.
- Weaknesses: Fixture/programming burden and capital cost.
- Failure Modes: Calibration drift, sensor alignment errors, repeated systematic defects.

#### [D5-02] Metrology-Guided Alignment Assembly
- Code: `ASM_METROLOGY_GUIDED`
- AssemblyCategory: Precision Robotic
- Summary: Assembly process tightly coupled to measurement systems to manage large-structure tolerances.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Strong for large hull alignment and reduced tolerance stack-up.
- Weaknesses: Metrology infrastructure and workflow complexity.
- Failure Modes: Measurement blind spots, model mismatch, alignment drift after clamping.

#### [D5-03] Active Alignment with Interferometry
- Code: `ASM_ACTIVE_ALIGN_INTERFEROMETRY`
- AssemblyCategory: Precision Robotic
- Summary: Ultra-precision alignment for critical structures and sensors using optical metrology.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Strengths: Very high alignment precision.
- Weaknesses: Environmental sensitivity and cost; not needed everywhere.
- Failure Modes: Optical path contamination, thermal drift, control instability.

#### [D5-04] Distributed Robotic Swarm Assembly (advanced)
- Code: `ASM_SWARM_ROBOTIC`
- AssemblyCategory: Advanced Precision Assembly
- Summary: Parallelized robotic assembly for large structures and in-space construction.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Scalability and speed for megastructural assembly.
- Weaknesses: Coordination complexity and systemic failure modes.
- Failure Modes: Synchronization errors, accumulated small misalignments, collision/damage events.

#### [D6-01] Zero-Defect Certified Assembly Workflow
- Code: `ASM_ZERO_DEFECT_CERT`
- AssemblyCategory: Civilization-Grade Assembly
- Summary: Process framework targeting extremely low hidden assembly defect rates through verification-heavy workflows.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: High confidence and lower uncertainty margins.
- Weaknesses: Costly, slow, infrastructure heavy.
- Failure Modes: False certification confidence, model overreliance, rare systemic blind spots.

#### [D6-02] Full Digital Twin Guided Assembly
- Code: `ASM_DIGITAL_TWIN_GUIDED`
- AssemblyCategory: Civilization-Grade Assembly
- Summary: Assembly process continuously checked against live digital twin state and tolerances.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Better traceability and consistency for complex ships.
- Weaknesses: Data integration complexity and cyber/control dependency.
- Failure Modes: Twin/model mismatch, sensor drift, stale model assumptions.

#### [D6-03] Joint-by-Joint Confidence Tagged Assembly
- Code: `ASM_JOINT_CONFIDENCE_TAGGED`
- AssemblyCategory: Civilization-Grade Assembly
- Summary: Every structural joint receives explicit confidence metrics feeding lifecycle maintenance and structural calculations.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Turns uncertainty into explicit engineering inputs.
- Weaknesses: Massive verification overhead and data management.
- Failure Modes: Confidence misclassification, sensor/inspection blind spots, data corruption.

#### [D6-04] In-Space Precision Hull Assembly
- Code: `ASM_INSPACE_PRECISION_HULL`
- AssemblyCategory: Civilization-Grade Assembly
- Summary: Precision assembly pipeline optimized for orbital/vacuum hull construction.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `3`
- Strengths: Enables very large precision structures and unique joining windows.
- Weaknesses: Logistics complexity, infrastructure dependence.
- Failure Modes: Alignment drift, thermal management issues, segment interface defects.

#### [D6-05] Atomic-Interface-Assisted Critical Joint Assembly (advanced)
- Code: `ASM_ATOMIC_INTERFACE_CRITICAL`
- AssemblyCategory: Civilization-Grade Assembly
- Summary: Ultra-advanced assembly of select joints with interface chemistry/structure controlled near atomic scales.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Approaches practical joint-efficiency ceiling for critical nodes.
- Weaknesses: Extremely expensive and difficult to validate at scale.
- Failure Modes: Hidden interface contamination, local decohesion, narrow process-window failures.

### E. Quality Profiles / Verification Regimes

Planned scope:
- basic QA
- industrial NDT
- aerospace-grade full verification
- digital twin + defect mapping
- civilization-grade full defect cartography

#### E1. Basic / Industrial QA
- Basic Industrial QA
- Industrial Batch QA
- Industrial QA + Sampling NDT
- Cost-Optimized High-Throughput QA
- Field Fabrication QA (limited confidence)

#### E2. High-Reliability NDT Profiles
- High-Reliability NDT (Ultrasound + X-ray spot checks)
- Full Ultrasonic Phased Array Profile
- X-ray CT Sampling Profile
- Residual Stress Spot Mapping Profile
- Joint-Critical NDT Focus Profile
- Pressure-Hull Reliability QA Profile

#### E3. Aerospace / Full-Traceability Profiles
- Aerospace Full Traceability QA
- Full Process Traceability + Material Lot Linking
- Full Joint Verification QA
- Residual Stress + Microstructure Verification QA
- High-Cycle Fatigue Certification QA
- Mission-Critical Structural Certification QA

#### E4. Advanced Digital Twin Quality Profiles
- Per-Part Digital Twin QA
- In-situ Monitoring + Digital Twin QA
- Defect Mesh Digital Twin QA
- Predictive Life Certification QA
- Structural Health Baseline Signature QA
- Recertification-Ready Lifecycle QA

#### E5. Civilization-Grade Verification Profiles
- Full Defect Cartography QA
- Closed-Loop Manufacturing + Verification QA
- Multiscale Tomography Certification QA
- Joint-by-Joint Confidence Tagged QA
- Predictive Crack Nucleation Mapping QA
- Autonomous Repair Recommendation QA
- Near-Theoretical Margin Reduction Certified QA

### F. Environment Profiles (for structural evaluation)

Planned scope:
- vacuum/cold
- vacuum/hot
- atmospheric/corrosive
- high-radiation
- dust/abrasive
- mixed multi-regime mission profiles

#### F1. Baseline Space Vacuum Profiles
- Low-Radiation Deep Space Vacuum
- Moderate-Radiation Deep Space Vacuum
- Inner-System Vacuum (thermal cycling moderate)
- Orbital Construction Vacuum Environment
- Pressurized-Habitat Interface Environment (mixed local loads)

#### F2. Thermal Extremes Profiles
- Cryogenic Vacuum Operations
- Hot-Side Solar Exposure Vacuum
- Extreme Thermal Cycling Orbit
- Engine-Proximal High-Temperature Structural Zone
- Cryo-Fuel Adjacent Structural Zone
- Thermal Gradient Shock Mission Profile

#### F3. Corrosive / Atmospheric Profiles
- Earth-like Atmosphere / Humidity Corrosion
- Saline Marine-Analog Atmosphere
- Dusty Oxidizing Atmosphere
- Corrosive Industrial Atmosphere
- Reactive Chemical Processing Bay Environment
- Planetary Surface Acidic/Alkaline Exposure (advanced mission)

#### F4. Radiation-Dominant Profiles
- High-Radiation Inner Belt / Magnetosphere Transit
- Stellar Flare Exposure Profile
- Long-Duration Deep Space Radiation Profile
- Reactor-Adjacent Structural Radiation Profile
- Particle Beam Hazard Environment (combat/industrial)

#### F5. Abrasive / Dust / Particle Profiles
- Regolith Dust Abrasion Environment
- Ice Particle / Cryo-Debris Erosion Environment
- Micrometeoroid-Rich Transit Profile
- Industrial Particle Blast / Debris Environment
- Sandstorm-like Planetary Atmosphere Abrasion
- Dockyard Particulate Contamination Environment

#### F6. Mixed Mission Profiles
- Civilian Freight Multi-Regime Profile
- Exploration Vessel Multi-Regime Profile
- Military High-G Maneuver + Combat Profile
- Long-Endurance Colony Support Profile
- Orbital Tug / Dock Operations Profile
- Atmospheric Entry-Capable Hybrid Mission Profile

#### E1-F6 Detailed Entries (first pass)

#### [E1-01] Basic Industrial QA
- Code: `QA_BASIC_INDUSTRIAL`
- Summary: Minimal production QA focused on dimensional checks and gross defects.
- TechMaturityLevel: `1`
- PhysicsConfidenceLevel: `1`
- Strengths: Cheap, fast, scalable.
- Weaknesses: Hidden defect risk remains high; large safety margins required.
- Operational Impact: High uncertainty, conservative structural sizing.

#### [E1-02] Industrial Batch QA
- Code: `QA_BATCH_INDUSTRIAL`
- Summary: Batch-based quality acceptance using statistical checks and process sampling.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Strengths: Better throughput/consistency than minimal QA.
- Weaknesses: Individual outlier parts/joints can slip through.
- Operational Impact: Moderate uncertainty; still margin-heavy.

#### [E1-03] Industrial QA + Sampling NDT
- Code: `QA_INDUSTRIAL_SAMPLING_NDT`
- Summary: Industrial QA supplemented by sampled non-destructive testing on critical lots.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Reduces major defect escapes at reasonable cost.
- Weaknesses: Sampling misses localized or rare defects.
- Operational Impact: Usable for mainstream structural fleets with moderate conservatism.

#### [E1-04] Cost-Optimized High-Throughput QA
- Code: `QA_HIGH_THROUGHPUT_COST_OPT`
- Summary: QA regime optimized for production volume and acceptable risk rather than peak reliability.
- TechMaturityLevel: `3`
- PhysicsConfidenceLevel: `1`
- Strengths: Fleet-scale affordability and output.
- Weaknesses: Risk concentrated in edge cases and long-life fatigue issues.
- Operational Impact: Good for civilian mass fleets, poor for extreme designs.

#### [E1-05] Field Fabrication QA (limited confidence)
- Code: `QA_FIELD_FAB_LIMITED`
- Summary: QA regime for remote/colony/repair fabrication with limited equipment and traceability.
- TechMaturityLevel: `2`
- PhysicsConfidenceLevel: `1`
- Strengths: Enables local production and repair autonomy.
- Weaknesses: Low confidence, high variability, weak defect characterization.
- Operational Impact: Requires robust structures and conservative load limits.

#### [E2-01] High-Reliability NDT (Ultrasound + X-ray spot checks)
- Code: `QA_HR_NDT_SPOT`
- Summary: Structured NDT regime focused on critical zones with multi-method spot coverage.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Better confidence in critical joints and load paths.
- Weaknesses: Coverage is still incomplete.
- Operational Impact: Moderate reduction in uncertainty, improved fatigue reliability.

#### [E2-02] Full Ultrasonic Phased Array Profile
- Code: `QA_FULL_UT_PA`
- Summary: High-coverage ultrasonic phased-array regime for welds, joints, and accessible volume.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `1`
- Strengths: Strong detection for many defect classes, scalable for large structures.
- Weaknesses: Geometry/material limitations and interpretation skill dependence.
- Operational Impact: Good confidence for welded metal structures and repeatable production.

#### [E2-03] X-ray CT Sampling Profile
- Code: `QA_CT_SAMPLING`
- Summary: CT-based sampling of representative parts/joints to characterize hidden defect distributions.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Deep defect visibility and process-learning value.
- Weaknesses: Slow and expensive; not full coverage.
- Operational Impact: Stronger process calibration, not direct full-part certainty.

#### [E2-04] Residual Stress Spot Mapping Profile
- Code: `QA_RES_STRESS_SPOT`
- Summary: QA focused on measuring residual stress in likely fatigue-critical zones.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Targets often-ignored failure driver in welded/processed structures.
- Weaknesses: Partial coverage and interpretation complexity.
- Operational Impact: Improves fatigue predictions and weld sequencing decisions.

#### [E2-05] Joint-Critical NDT Focus Profile
- Code: `QA_JOINT_CRITICAL_FOCUS`
- Summary: Quality profile prioritizing joints over bulk material due to joint-dominated failure risk.
- TechMaturityLevel: `4`
- PhysicsConfidenceLevel: `2`
- Strengths: Efficient reliability gains in joint-heavy architectures.
- Weaknesses: Bulk material defects may remain under-characterized.
- Operational Impact: Excellent for modular frames and complex assemblies.

#### [E2-06] Pressure-Hull Reliability QA Profile
- Code: `QA_PRESSURE_HULL_RELIABILITY`
- Summary: QA tailored to pressure boundaries, leak-critical interfaces, and cyclic pressurization fatigue.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `2`
- Strengths: Strong mission-specific reliability for crewed systems.
- Weaknesses: Specialized and costlier than general QA.
- Operational Impact: Enables lower leak/fatigue risk in pressurized vessels.

#### [E3-01] Aerospace Full Traceability QA
- Code: `QA_AERO_FULL_TRACE`
- Summary: Full traceability from raw material lot to final structural assembly with process history retention.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `1`
- Strengths: Strong root-cause analysis, certification confidence, repeatability.
- Weaknesses: Data/process overhead and cost.
- Operational Impact: Lower uncertainty and stronger lifecycle management.

#### [E3-02] Full Process Traceability + Material Lot Linking
- Code: `QA_TRACE_LOT_LINKED`
- Summary: Explicit lot/process tracking enabling probabilistic risk analysis by batch lineage.
- TechMaturityLevel: `5`
- PhysicsConfidenceLevel: `1`
- Strengths: Excellent for fleet reliability and recall/repair planning.
- Weaknesses: Does not directly detect hidden defects; needs NDT pairing.
- Operational Impact: Strong reduction in systemic production risk.

#### [E3-03] Full Joint Verification QA
- Code: `QA_FULL_JOINT_VERIFY`
- Summary: Every critical structural joint is directly inspected/verified to a defined confidence standard.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `2`
- Strengths: Major reliability gain in joint-dominated structures.
- Weaknesses: Time/cost intensive and access-limited in dense assemblies.
- Operational Impact: Allows tighter joint safety assumptions than generic QA.

#### [E3-04] Residual Stress + Microstructure Verification QA
- Code: `QA_RES_STRESS_MICRO_VERIFY`
- Summary: QA regime verifying not just geometry/defects, but actual microstructure and residual stress state.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `2`
- Strengths: Better correlation with true fatigue and fracture behavior.
- Weaknesses: Expensive, technically demanding, often partial coverage.
- Operational Impact: Improved fidelity of structural predictions.

#### [E3-05] High-Cycle Fatigue Certification QA
- Code: `QA_HCF_CERT`
- Summary: QA/qualification profile built around fatigue-critical service and long mission life.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `2`
- Strengths: Strong for long-life civilian and high-sortie military ships.
- Weaknesses: Testing/qualification time high; model assumptions matter.
- Operational Impact: Enables lower unexpected fatigue failure rate.

#### [E3-06] Mission-Critical Structural Certification QA
- Code: `QA_MISSION_CRITICAL_STRUCT`
- Summary: Certification-heavy profile for flagship, crewed, or catastrophic-failure-intolerant structures.
- TechMaturityLevel: `6`
- PhysicsConfidenceLevel: `2`
- Strengths: High confidence and documented reliability.
- Weaknesses: Expensive and slows production.
- Operational Impact: Supports lower operational risk and tighter readiness thresholds.

#### [E4-01] Per-Part Digital Twin QA
- Code: `QA_PER_PART_DIGITAL_TWIN`
- Summary: Each major part receives a digital twin capturing geometry, process, and inspection state.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Strengths: Great traceability and maintenance planning; supports individualized risk assessment.
- Weaknesses: Data infrastructure and model maintenance burden.
- Operational Impact: Lower uncertainty over lifecycle, better anomaly diagnosis.

#### [E4-02] In-situ Monitoring + Digital Twin QA
- Code: `QA_INSITU_PLUS_TWIN`
- Summary: Combines manufacturing process telemetry with digital twin QA records.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Strengths: Captures defects/process anomalies at origin, not just after-the-fact.
- Weaknesses: Sensor/model integration complexity and false-positive management.
- Operational Impact: Better early defect rejection and confidence weighting.

#### [E4-03] Defect Mesh Digital Twin QA
- Code: `QA_DEFECT_MESH_TWIN`
- Summary: Digital twin includes spatial defect maps/meshes that can feed structural analysis.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Converts uncertainty into explicit inputs for engineering calculations.
- Weaknesses: Heavy data, model fidelity challenges, interpretation complexity.
- Operational Impact: Enables tighter but defensible margins on premium structures.

#### [E4-04] Predictive Life Certification QA
- Code: `QA_PREDICTIVE_LIFE_CERT`
- Summary: QA linked to physics-based/validated predictive life models across mission profiles.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `3`
- Strengths: Excellent lifecycle planning and fleet readiness optimization.
- Weaknesses: Sensitive to model assumptions and environment/profile mismatch.
- Operational Impact: Strong maintenance planning and reduced surprise failures.

#### [E4-05] Structural Health Baseline Signature QA
- Code: `QA_SHM_BASELINE_SIGNATURE`
- Summary: Establishes baseline signatures (acoustic/vibration/strain/etc.) for future in-service health comparisons.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Strengths: Detects drift and damage progression during service.
- Weaknesses: Signal interpretation complexity and false alarms.
- Operational Impact: Improves preventive maintenance and mission confidence.

#### [E4-06] Recertification-Ready Lifecycle QA
- Code: `QA_RECERT_LIFECYCLE_READY`
- Summary: QA documentation and instrumentation designed for repeated in-service recertification cycles.
- TechMaturityLevel: `7`
- PhysicsConfidenceLevel: `3`
- Strengths: Extends usable life of expensive ships and modules.
- Weaknesses: Upfront instrumentation and record overhead.
- Operational Impact: Better long-term value and structural transparency.

#### [E5-01] Full Defect Cartography QA
- Code: `QA_FULL_DEFECT_CARTO`
- Summary: Near-complete defect mapping of critical structures/joints for direct engineering use.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Best uncertainty reduction without violating physics.
- Weaknesses: Extreme cost/time/infrastructure.
- Operational Impact: Enables civilization-grade margin optimization.

#### [E5-02] Closed-Loop Manufacturing + Verification QA
- Code: `QA_CLOSED_LOOP_MFG_VERIFY`
- Summary: Manufacturing and QA integrated into one corrective loop with defect-aware process adaptation.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Prevents defect propagation and improves consistency at source.
- Weaknesses: System complexity and cascading model/system failure risks.
- Operational Impact: Strong repeatability for elite production lines.

#### [E5-03] Multiscale Tomography Certification QA
- Code: `QA_MULTISCALE_TOMO_CERT`
- Summary: Certification regime using macro-to-micro imaging to validate structure and defect populations across scales.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Captures defects that simpler methods miss, improves confidence in advanced materials.
- Weaknesses: Immense data volume and interpretation burden.
- Operational Impact: Critical for hierarchical lattices and advanced composites.

#### [E5-04] Joint-by-Joint Confidence Tagged QA
- Code: `QA_JOINT_CONFIDENCE_TAGGED`
- Summary: Every major joint gets quantified confidence metadata feeding operational limits and maintenance.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Enables risk-aware operations and localized recertification.
- Weaknesses: Expensive verification and data governance.
- Operational Impact: Directly supports mission planning and degraded-mode operation.

#### [E5-05] Predictive Crack Nucleation Mapping QA
- Code: `QA_CRACK_NUCLEATION_PREDICT`
- Summary: Advanced QA/modeling profile estimating likely crack nucleation zones before service.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Powerful preventive maintenance and design feedback.
- Weaknesses: Model-sensitivity and potential false confidence.
- Operational Impact: High value for critical fleets and extreme loads.

#### [E5-06] Autonomous Repair Recommendation QA
- Code: `QA_AUTONOMOUS_REPAIR_RECOMMEND`
- Summary: QA+analysis system generating repair/limit recommendations from structural state data.
- TechMaturityLevel: `8`
- PhysicsConfidenceLevel: `4`
- Strengths: Speeds maintenance decisions and standardizes responses.
- Weaknesses: Requires reliable models and trusted data pipelines.
- Operational Impact: High for remote fleets and long missions.

#### [E5-07] Near-Theoretical Margin Reduction Certified QA
- Code: `QA_NEAR_THEORETICAL_MARGIN_CERT`
- Summary: Elite verification regime allowing practical margins to approach physical/engineering limits with documented confidence.
- TechMaturityLevel: `9`
- PhysicsConfidenceLevel: `4`
- Strengths: Maximum performance extraction without "magic."
- Weaknesses: Cost and complexity extreme; not scalable to common fleets.
- Operational Impact: Flagships and specialized civilization-grade craft only.

#### [F1-01] Low-Radiation Deep Space Vacuum
- Code: `ENV_SPACE_VAC_LOW_RAD`
- Summary: Baseline vacuum environment with low thermal/collision severity and modest radiation.
- Typical Mission Context: Standard cargo transit, low-risk operations.
- Dominant Risks: Thermal cycling, micrometeoroids (low), vacuum compatibility.
- Structural Notes: Broadly forgiving; good baseline for material comparisons.

#### [F1-02] Moderate-Radiation Deep Space Vacuum
- Code: `ENV_SPACE_VAC_MOD_RAD`
- Summary: Deep space vacuum with more significant radiation exposure over long durations.
- Typical Mission Context: Long-haul routes, patrols, exploration transits.
- Dominant Risks: Radiation aging in polymers/electronics, thermal fatigue.
- Structural Notes: Pushes composite matrices and coatings harder than metals.

#### [F1-03] Inner-System Vacuum (thermal cycling moderate)
- Code: `ENV_INNER_SYSTEM_VAC`
- Summary: Vacuum with stronger solar thermal cycles and intermittent heating/cooling swings.
- Typical Mission Context: Near-star logistics, orbital operations.
- Dominant Risks: Thermal gradients, expansion mismatch, surface degradation.
- Structural Notes: Thermal-compensated joints and shell structures gain value.

#### [F1-04] Orbital Construction Vacuum Environment
- Code: `ENV_ORBITAL_CONSTRUCTION_VAC`
- Summary: Assembly/manufacturing environment in orbit with contamination and handling loads.
- Typical Mission Context: Shipyards, megastructure docks.
- Dominant Risks: Alignment drift, particulate contamination, handling impact.
- Structural Notes: Assembly process and QA matter more than raw material extremes.

#### [F1-05] Pressurized-Habitat Interface Environment (mixed local loads)
- Code: `ENV_PRESS_HAB_INTERFACE`
- Summary: Structural boundary condition with pressure cycling, docking, and utility integration loads.
- Typical Mission Context: Crewed ships, stations, modules.
- Dominant Risks: Seal/joint fatigue, pressure-cycle cracking, thermal mismatch.
- Structural Notes: Joint-focused QA and pressure-specific structures are critical.

#### [F2-01] Cryogenic Vacuum Operations
- Code: `ENV_CRYO_VAC`
- Summary: Vacuum operations dominated by cryogenic temperatures and contraction effects.
- Typical Mission Context: Deep space, cryogenic storage systems.
- Dominant Risks: Brittle behavior, thermal contraction mismatch, low-temp fatigue.
- Structural Notes: Cryo-compatible alloys and specialized lattices/sandwiches become important.

#### [F2-02] Hot-Side Solar Exposure Vacuum
- Code: `ENV_HOT_SOLAR_VAC`
- Summary: Vacuum with persistent high radiative heating on exposed surfaces.
- Typical Mission Context: Inner-system stations, sun-facing operations.
- Dominant Risks: Thermal creep, coating degradation, differential expansion.
- Structural Notes: High-temp materials, thermal-buffer cores, compensated shell grids help.

#### [F2-03] Extreme Thermal Cycling Orbit
- Code: `ENV_EXTREME_THERMAL_CYCLE_ORBIT`
- Summary: Repeated rapid transitions between hot and cold states causing fatigue and interface stress.
- Typical Mission Context: Low/fast orbiting platforms, rotating duty cycles.
- Dominant Risks: Thermal fatigue, delamination, seal degradation.
- Structural Notes: Interfaces and graded materials often dominate lifetime.

#### [F2-04] Engine-Proximal High-Temperature Structural Zone
- Code: `ENV_ENGINE_PROX_HOT_STRUCT`
- Summary: Local environment near propulsion systems with heat, vibration, and dynamic loads.
- Typical Mission Context: Engine mounts, aft structures.
- Dominant Risks: Creep, thermal fatigue, vibration-assisted crack growth.
- Structural Notes: Superalloys/CMC/refractory classes and high QA are mandatory.

#### [F2-05] Cryo-Fuel Adjacent Structural Zone
- Code: `ENV_CRYO_FUEL_ADJ_STRUCT`
- Summary: Structural regions adjacent to cryogenic tanks and feed systems, with thermal gradients and leak risk.
- Typical Mission Context: Tank supports, transfer manifolds, hull interfaces.
- Dominant Risks: Thermal contraction mismatch, frost/condensation issues (in atmosphere), low-temp embrittlement.
- Structural Notes: Cryo-optimized systems and thermal-isolation structural cores are valuable.

#### [F2-06] Thermal Gradient Shock Mission Profile
- Code: `ENV_THERMAL_GRAD_SHOCK`
- Summary: Severe thermal gradients and rapid transitions, stressing shells, joints, and hybrid interfaces.
- Typical Mission Context: Aggressive maneuvers near heat sources, emergency operating modes.
- Dominant Risks: Thermal shock cracking, interface decohesion, distortion.
- Structural Notes: Graded interfaces/cores and high-confidence QA significantly improve survivability.

#### [F3-01] Earth-like Atmosphere / Humidity Corrosion
- Code: `ENV_ATMO_EARTHLIKE_HUMID`
- Summary: Oxygen/humidity-driven corrosion environment for atmospheric-capable or docked structures.
- Typical Mission Context: Planetary bases, atmospheric operations.
- Dominant Risks: Corrosion, coating breakdown, moisture ingress in composites.
- Structural Notes: Stainless, coated alloys, and corrosion-focused QA matter.

#### [F3-02] Saline Marine-Analog Atmosphere
- Code: `ENV_ATMO_SALINE`
- Summary: High-salinity corrosive environment accelerating galvanic and pitting damage.
- Typical Mission Context: Ocean worlds, marine dockyards.
- Dominant Risks: Pitting, crevice corrosion, galvanic attack.
- Structural Notes: Material pairing and sealing quality dominate long-term survivability.

#### [F3-03] Dusty Oxidizing Atmosphere
- Code: `ENV_ATMO_DUST_OX`
- Summary: Oxidizing atmosphere with abrasive dust loading surfaces and joints.
- Typical Mission Context: Desert worlds, mining colonies.
- Dominant Risks: Abrasion, seal wear, dust ingress, oxidation.
- Structural Notes: Wear-resistant surfaces and maintenance-friendly assemblies are important.

#### [F3-04] Corrosive Industrial Atmosphere
- Code: `ENV_ATMO_CORR_INDUSTRIAL`
- Summary: Industrial chemical vapors and contaminants degrade coatings, polymers, and exposed metals.
- Typical Mission Context: Refineries, orbital processing plants, chemical transport hubs.
- Dominant Risks: Chemical attack, coating failure, polymer aging.
- Structural Notes: Stainless, cermets, coatings, and strict maintenance regimes are favored.

#### [F3-05] Reactive Chemical Processing Bay Environment
- Code: `ENV_REACTIVE_CHEM_BAY`
- Summary: Internally contaminated environment with spills/vapors from reactive cargo or processing systems.
- Typical Mission Context: Chemical carriers, processing modules.
- Dominant Risks: Local corrosion, seal attack, embrittlement.
- Structural Notes: Localized material specialization and segmented/damage-arrest structures pay off.

#### [F3-06] Planetary Surface Acidic/Alkaline Exposure (advanced mission)
- Code: `ENV_PLANET_ACID_ALKALI`
- Summary: Highly hostile atmospheric/surface chemistry requiring specialized structural and coating choices.
- Typical Mission Context: Extreme xenoplanet industry/science missions.
- Dominant Risks: Rapid corrosion/etching, coating breach propagation, maintenance burden.
- Structural Notes: Niche materials and aggressive QA/inspection intervals are essential.

#### [F4-01] High-Radiation Inner Belt / Magnetosphere Transit
- Code: `ENV_RAD_BELT_TRANSIT`
- Summary: Elevated charged particle environment stressing electronics, polymers, and coatings.
- Typical Mission Context: Belt crossings, planetary orbital transfers.
- Dominant Risks: Radiation aging, charging, material property drift in sensitive matrices.
- Structural Notes: Metals often fare better structurally; composites need matrix/radiation tailoring.

#### [F4-02] Stellar Flare Exposure Profile
- Code: `ENV_RAD_STELLAR_FLARE`
- Summary: Intermittent high-intensity radiation events creating acute exposure conditions.
- Typical Mission Context: Active-star systems, risky exploration.
- Dominant Risks: Sudden radiation spikes, thermal/radiation coupled damage.
- Structural Notes: Conservative margins and robust shielding strategies required.

#### [F4-03] Long-Duration Deep Space Radiation Profile
- Code: `ENV_RAD_LONG_DEEP_SPACE`
- Summary: Long-term cumulative radiation environment driving slow degradation and property drift.
- Typical Mission Context: Deep-space expeditions, colony convoys.
- Dominant Risks: Polymer matrix aging, sensor drift, coating degradation.
- Structural Notes: Lifecycle QA and recertification-ready profiles are high value.

#### [F4-04] Reactor-Adjacent Structural Radiation Profile
- Code: `ENV_RAD_REACTOR_ADJ`
- Summary: Persistent local radiation and thermal environment around power/reactor systems.
- Typical Mission Context: Power modules, engine rooms.
- Dominant Risks: Radiation-assisted embrittlement/degradation, thermal cycling.
- Structural Notes: Specialized materials and targeted QA dominate over generic hull logic.

#### [F4-05] Particle Beam Hazard Environment (combat/industrial)
- Code: `ENV_RAD_PARTICLE_BEAM_HAZARD`
- Summary: Exposure risk to intense directed particle/plasma streams in combat or industrial settings.
- Typical Mission Context: Combat zones, beam processing facilities.
- Dominant Risks: Local heating, erosion, surface damage, secondary radiation effects.
- Structural Notes: Layered protection and damage-arrest structural designs help contain failures.

#### [F5-01] Regolith Dust Abrasion Environment
- Code: `ENV_ABRASIVE_REGOLITH_DUST`
- Summary: Fine abrasive particulate exposure causing wear, seal contamination, and surface damage.
- Typical Mission Context: Moon/asteroid mining and surface logistics.
- Dominant Risks: Abrasion, contamination ingress, moving-joint wear.
- Structural Notes: Surface treatments, modular repairs, and enclosed joints are valuable.

#### [F5-02] Ice Particle / Cryo-Debris Erosion Environment
- Code: `ENV_ABRASIVE_ICE_CRYO`
- Summary: Cryogenic particulate impacts and erosive flux affecting exposed structures.
- Typical Mission Context: Ring operations, icy moon industry.
- Dominant Risks: Erosion, embrittlement-assisted damage, cryo impact spallation.
- Structural Notes: Cryo-tough materials and impact-optimized sandwich structures help.

#### [F5-03] Micrometeoroid-Rich Transit Profile
- Code: `ENV_ABRASIVE_MICROMET_RICH`
- Summary: Elevated micro-impact risk environment requiring damage tolerance and layered protection.
- Typical Mission Context: Hazardous trade lanes, debris fields.
- Dominant Risks: Repeated micro-impacts, hidden crack initiation, puncture risk.
- Structural Notes: Sandwich, hybrid, and damage-arrest designs gain importance.

#### [F5-04] Industrial Particle Blast / Debris Environment
- Code: `ENV_ABRASIVE_INDUSTRIAL_DEBRIS`
- Summary: Human/industrial activity produces recurrent debris and abrasive particle loads.
- Typical Mission Context: Shipyards, mining processing zones.
- Dominant Risks: Surface pitting, edge damage, contamination.
- Structural Notes: Maintenance and coating strategy often matter more than exotic base materials.

#### [F5-05] Sandstorm-like Planetary Atmosphere Abrasion
- Code: `ENV_ABRASIVE_SANDSTORM_ATMO`
- Summary: High-velocity abrasive atmosphere degrading exposed shells, seals, and sensors.
- Typical Mission Context: Harsh planetary operations.
- Dominant Risks: Erosion, dust infiltration, fatigue from vibratory loading.
- Structural Notes: Corrosion/abrasion-resistant materials and serviceable assemblies are key.

#### [F5-06] Dockyard Particulate Contamination Environment
- Code: `ENV_CONTAM_DOCKYARD_PARTICULATE`
- Summary: Particulate contamination environment mainly affecting assembly, bonding, and precision alignment quality.
- Typical Mission Context: Crowded shipyards and orbital construction hubs.
- Dominant Risks: Bond contamination, alignment errors, sensor fouling.
- Structural Notes: Assembly and QA profiles are primary levers here.

#### [F6-01] Civilian Freight Multi-Regime Profile
- Code: `ENV_MIXED_CIV_FREIGHT`
- Summary: Mixed mission profile prioritizing durability, repairability, and cost efficiency over peak performance.
- Typical Mission Context: Routine commerce and logistics.
- Dominant Risks: Fatigue, maintenance delays, environment switching.
- Structural Notes: HSLA steel/aluminum/composite hybrids with strong serviceability often optimal.

#### [F6-02] Exploration Vessel Multi-Regime Profile
- Code: `ENV_MIXED_EXPLORATION`
- Summary: Wide-envelope mission profile with uncertain loads, long duration, and diverse environments.
- Typical Mission Context: Survey, science, frontier operations.
- Dominant Risks: Profile mismatch, cumulative degradation, logistics-limited repairs.
- Structural Notes: Redundancy, robust QA, and adaptable structures outperform razor-thin optimization.

#### [F6-03] Military High-G Maneuver + Combat Profile
- Code: `ENV_MIXED_MIL_HIGHG_COMBAT`
- Summary: Extreme dynamic load and damage environment prioritizing survivability and high acceleration capacity.
- Typical Mission Context: Combat ships, interceptors, assault transports.
- Dominant Risks: High-cycle + peak overload fatigue, impact damage, thermal spikes.
- Structural Notes: Redundant frames, damage-arrest structures, premium materials, and top-tier QA justify cost.

#### [F6-04] Long-Endurance Colony Support Profile
- Code: `ENV_MIXED_LONG_ENDURANCE_COLONY`
- Summary: Long service life with limited dock support and broad mission variety.
- Typical Mission Context: Colony support fleets and remote logistics.
- Dominant Risks: Wear accumulation, corrosion/abrasion, deferred maintenance, fatigue.
- Structural Notes: Repairability and lifecycle QA matter as much as material performance.

#### [F6-05] Orbital Tug / Dock Operations Profile
- Code: `ENV_MIXED_ORBITAL_TUG_DOCK`
- Summary: Repeated docking loads, low-to-moderate dynamic loads, alignment and local impact stresses.
- Typical Mission Context: Shipyard tugs, station service craft.
- Dominant Risks: Joint wear, docking shock, fatigue in local structures.
- Structural Notes: Modular node-lock + high-serviceability designs are often ideal.

#### [F6-06] Atmospheric Entry-Capable Hybrid Mission Profile
- Code: `ENV_MIXED_ATMO_ENTRY_HYBRID`
- Summary: Harsh mixed profile combining vacuum service, thermal extremes, aerodynamic/entry loads, and corrosion exposure.
- Typical Mission Context: Reentry-capable military/exploration/cargo hybrids.
- Dominant Risks: Thermal shock, oxidation/corrosion, fatigue across multiple regimes, maintenance complexity.
- Structural Notes: Multi-material and graded/thermal-buffer structures become strategically valuable.

## Next Planned Work

1. Fill the extended master lists (all categories).
2. Add detailed descriptions for every listed position.
3. Assign `TechMaturityLevel` and `PhysicsConfidenceLevel` to each position.
4. Mark questionable entries and justify plausibility.
5. Convert selected entries into structured game data (DB/JSON).
