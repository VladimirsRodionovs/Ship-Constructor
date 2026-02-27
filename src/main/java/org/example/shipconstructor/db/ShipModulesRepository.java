package org.example.shipconstructor.db;

import java.util.Map;

public interface ShipModulesRepository {
    long findNextFreeModuleId() throws Exception;

    void upsertModuleDesign(Map<String, Object> moduleData) throws Exception;
}
