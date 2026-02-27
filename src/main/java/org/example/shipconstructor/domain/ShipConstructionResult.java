package org.example.shipconstructor.domain;

import java.util.Collections;
import java.util.List;

public class ShipConstructionResult {
    private final ShipDraft shipDraft;
    private final ShipMassSummary massSummary;
    private final ShipEnergySummary energySummary;
    private final ShipVolumeSummary volumeSummary;
    private final int averageReliability;
    private final List<String> warnings;

    public ShipConstructionResult(
            ShipDraft shipDraft,
            ShipMassSummary massSummary,
            ShipEnergySummary energySummary,
            ShipVolumeSummary volumeSummary,
            int averageReliability,
            List<String> warnings) {
        this.shipDraft = shipDraft;
        this.massSummary = massSummary;
        this.energySummary = energySummary;
        this.volumeSummary = volumeSummary;
        this.averageReliability = averageReliability;
        this.warnings = warnings == null ? Collections.<String>emptyList() : Collections.unmodifiableList(warnings);
    }

    public ShipDraft getShipDraft() {
        return shipDraft;
    }

    public ShipMassSummary getMassSummary() {
        return massSummary;
    }

    public ShipEnergySummary getEnergySummary() {
        return energySummary;
    }

    public ShipVolumeSummary getVolumeSummary() {
        return volumeSummary;
    }

    public int getAverageReliability() {
        return averageReliability;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}
