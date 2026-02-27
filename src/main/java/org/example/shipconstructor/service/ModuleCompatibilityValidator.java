package org.example.shipconstructor.service;

import org.example.shipconstructor.domain.ShipModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleCompatibilityValidator {
    public List<String> validate(List<ShipModule> modules, int maxAllowedTechLevelGap) {
        List<String> issues = new ArrayList<String>();
        if (modules == null || modules.isEmpty()) {
            issues.add("Ship has no modules");
            return issues;
        }

        Integer baseDirection = null;
        Integer minLevel = null;
        Integer maxLevel = null;

        for (ShipModule module : modules) {
            if (module == null) {
                issues.add("Null module in ship module list");
                continue;
            }

            if (baseDirection == null) {
                baseDirection = Integer.valueOf(module.getTechnologyDirectionIndex());
            } else if (baseDirection.intValue() != module.getTechnologyDirectionIndex()) {
                issues.add("Technology direction mismatch for module: " + module.getModuleName());
            }

            if (minLevel == null || module.getTechnologyLevelIndex() < minLevel.intValue()) {
                minLevel = Integer.valueOf(module.getTechnologyLevelIndex());
            }
            if (maxLevel == null || module.getTechnologyLevelIndex() > maxLevel.intValue()) {
                maxLevel = Integer.valueOf(module.getTechnologyLevelIndex());
            }
        }

        if (minLevel != null && maxLevel != null) {
            int gap = maxLevel.intValue() - minLevel.intValue();
            if (gap > maxAllowedTechLevelGap) {
                issues.add("Technology level gap exceeds limit: " + gap + " > " + maxAllowedTechLevelGap);
            }
        }

        return issues;
    }
}
