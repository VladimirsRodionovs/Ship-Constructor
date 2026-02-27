package org.example.shipconstructor.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShipDraft {
    private final Long shipId;
    private final String shipName;
    private final String shipClassName;
    private final int maxAllowedTechLevelGap;
    private final List<ShipModule> modules;

    public ShipDraft(Long shipId, String shipName, String shipClassName, int maxAllowedTechLevelGap, List<ShipModule> modules) {
        this.shipId = shipId;
        this.shipName = shipName;
        this.shipClassName = shipClassName;
        this.maxAllowedTechLevelGap = maxAllowedTechLevelGap;
        this.modules = modules == null
                ? Collections.<ShipModule>emptyList()
                : Collections.unmodifiableList(new ArrayList<ShipModule>(modules));
    }

    public Long getShipId() {
        return shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public String getShipClassName() {
        return shipClassName;
    }

    public int getMaxAllowedTechLevelGap() {
        return maxAllowedTechLevelGap;
    }

    public List<ShipModule> getModules() {
        return modules;
    }
}
