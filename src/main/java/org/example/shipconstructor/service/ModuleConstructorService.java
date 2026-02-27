package org.example.shipconstructor.service;

import org.example.shipconstructor.domain.ModuleEnergyProfile;
import org.example.shipconstructor.domain.ModuleStorageProfile;
import org.example.shipconstructor.domain.ShipModule;
import org.example.shipconstructor.domain.ShipModuleDraft;

import java.util.Map;

public class ModuleConstructorService {
    private static final int DEFAULT_RELIABILITY = 90;

    public ShipModule construct(ShipModuleDraft draft) {
        if (draft == null) {
            throw new IllegalArgumentException("Module draft is required");
        }

        String name = requiredText(draft.getModuleName(), "moduleName");
        int techDirection = requiredPositiveOrZero(draft.getTechnologyDirectionIndex(), "technologyDirectionIndex");
        int techLevel = requiredPositiveOrZero(draft.getTechnologyLevelIndex(), "technologyLevelIndex");

        double dryWeight = parseWeight(draft.getDryWeightText(), "dryWeight");
        double equippedWeight = parseWeight(draft.getFullWeightText(), "fullWeight");
        if (equippedWeight < dryWeight) {
            throw new IllegalArgumentException("fullWeight must be >= dryWeight for module " + name);
        }

        int reliability = draft.getReliability() == null ? DEFAULT_RELIABILITY : draft.getReliability().intValue();
        if (reliability < 0 || reliability > 100) {
            throw new IllegalArgumentException("reliability must be in range 0..100 for module " + name);
        }

        ModuleEnergyProfile energy = draft.getEnergyProfile() == null ? ModuleEnergyProfile.empty() : draft.getEnergyProfile();
        ModuleStorageProfile storage = draft.getStorageProfile() == null ? ModuleStorageProfile.empty() : draft.getStorageProfile();
        Map<String, Double> extraVolumes = draft.getExtraVolumesM3();

        return new ShipModule(
                draft.getModuleId(),
                name,
                draft.getTypeId(),
                techDirection,
                techLevel,
                dryWeight,
                equippedWeight,
                reliability,
                energy,
                storage,
                extraVolumes
        );
    }

    private String requiredText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }

    private int requiredPositiveOrZero(Integer value, String fieldName) {
        if (value == null || value.intValue() < 0) {
            throw new IllegalArgumentException(fieldName + " must be >= 0");
        }
        return value.intValue();
    }

    private double parseWeight(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        String normalized = value.trim().replace(',', '.');
        try {
            double parsed = Double.parseDouble(normalized);
            if (parsed < 0.0d) {
                throw new IllegalArgumentException(fieldName + " must be >= 0");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid " + fieldName + " value: " + value, ex);
        }
    }
}
