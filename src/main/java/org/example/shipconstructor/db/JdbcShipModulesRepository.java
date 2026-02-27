package org.example.shipconstructor.db;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class JdbcShipModulesRepository implements ShipModulesRepository {
    private static final String SQL_IDS =
            "SELECT ModuleID FROM ShipModules " +
            "WHERE ModuleID IS NOT NULL AND ModuleID > 0 " +
            "ORDER BY ModuleID";
    private static final String SQL_EXISTS =
            "SELECT 1 FROM ShipModules WHERE ModuleID = ? LIMIT 1";
    private static final String SQL_INSERT =
            "INSERT INTO ShipModules (" +
            "ModuleID,PartyID,SizeType,SizeTotal,ModName,NumberProduced,Type,Name,SpecFunctionTypeName,ManufacturerName,DeveloperName," +
            "DryWeight,FullWeight,CargoVolumeMax,CargoType,Reliability,ManufactureDate,ManufactureTimeSec," +
            "EnergyConsMax,EnergyConsPowerUp,EnergyConsStandBy,EnergyConsOn,EnergyProdMin,EnergyProdMax,EnergyProdNominal,EnergyProdCritical," +
            "FuelConsumption,FuelQantity,FuelRadioactiveMaterial,DegradationSpeedPerYear,FuelType,DateOfRefueling," +
            "ControlLinesAmount,TermalLinesAmount,FuelLinesAmount,PowerLinesAmount," +
            "SizeDimentions,SpecificModParamCrew,SpecificModParamEmissions,SpecificModParamWeapon,SpecificModParamTrust," +
            "DockType,Armored,AmmoType,InternalDefence,MeshID,Blocks,technologyDirection,technologyLevel" +
            ") VALUES (" +
            "?,?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?," +
            "?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?,?,?,?" +
            ")";
    private static final String SQL_UPDATE =
            "UPDATE ShipModules SET " +
            "PartyID=?,SizeType=?,SizeTotal=?,ModName=?,NumberProduced=?,Type=?,Name=?,SpecFunctionTypeName=?,ManufacturerName=?,DeveloperName=?," +
            "DryWeight=?,FullWeight=?,CargoVolumeMax=?,CargoType=?,Reliability=?,ManufactureDate=?,ManufactureTimeSec=?," +
            "EnergyConsMax=?,EnergyConsPowerUp=?,EnergyConsStandBy=?,EnergyConsOn=?,EnergyProdMin=?,EnergyProdMax=?,EnergyProdNominal=?,EnergyProdCritical=?," +
            "FuelConsumption=?,FuelQantity=?,FuelRadioactiveMaterial=?,DegradationSpeedPerYear=?,FuelType=?,DateOfRefueling=?," +
            "ControlLinesAmount=?,TermalLinesAmount=?,FuelLinesAmount=?,PowerLinesAmount=?," +
            "SizeDimentions=?,SpecificModParamCrew=?,SpecificModParamEmissions=?,SpecificModParamWeapon=?,SpecificModParamTrust=?," +
            "DockType=?,Armored=?,AmmoType=?,InternalDefence=?,MeshID=?,Blocks=?,technologyDirection=?,technologyLevel=? " +
            "WHERE ModuleID=?";

    private final DatabaseConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JdbcShipModulesRepository(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public long findNextFreeModuleId() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                config.getJdbcUrl(), config.getUsername(), config.getPassword());
             PreparedStatement statement = connection.prepareStatement(SQL_IDS);
             ResultSet rs = statement.executeQuery()) {

            long expected = 1L;
            while (rs.next()) {
                long current = rs.getLong(1);
                if (current < expected) {
                    continue;
                }
                if (current > expected) {
                    return expected;
                }
                expected++;
            }
            return expected;
        }
    }

    @Override
    public void upsertModuleDesign(Map<String, Object> moduleData) throws Exception {
        long moduleId = getRequiredLong(moduleData, "ModuleID");
        try (Connection connection = DriverManager.getConnection(
                config.getJdbcUrl(), config.getUsername(), config.getPassword())) {
            if (exists(connection, moduleId)) {
                try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
                    int index = bindCommonFields(statement, moduleData, false);
                    statement.setLong(index, moduleId);
                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
                    bindCommonFields(statement, moduleData, true);
                    statement.executeUpdate();
                }
            }
        }
    }

    private boolean exists(Connection connection, long moduleId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS)) {
            statement.setLong(1, moduleId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int bindCommonFields(PreparedStatement statement, Map<String, Object> data, boolean includeModuleId) throws Exception {
        int i = 1;
        if (includeModuleId) {
            statement.setLong(i++, getRequiredLong(data, "ModuleID"));
        }
        statement.setLong(i++, getRequiredLong(data, "PartyID"));
        statement.setInt(i++, getRequiredInt(data, "SizeType"));
        statement.setLong(i++, getRequiredLong(data, "SizeTotal"));
        statement.setString(i++, getRequiredString(data, "ModName"));
        statement.setLong(i++, getRequiredLong(data, "NumberProduced"));
        statement.setInt(i++, getRequiredInt(data, "Type"));
        statement.setString(i++, getRequiredString(data, "Name"));
        statement.setString(i++, getRequiredString(data, "SpecFunctionTypeName"));
        statement.setString(i++, getRequiredString(data, "ManufacturerName"));
        statement.setString(i++, getRequiredString(data, "DeveloperName"));
        statement.setString(i++, stringifyNumber(data.get("DryWeight")));
        statement.setString(i++, stringifyNumber(data.get("FullWeight")));
        statement.setLong(i++, getRequiredLong(data, "CargoVolumeMax"));
        statement.setInt(i++, getRequiredInt(data, "CargoType"));
        statement.setInt(i++, getRequiredInt(data, "Reliability"));
        statement.setString(i++, getRequiredString(data, "ManufactureDate"));
        statement.setLong(i++, getRequiredLong(data, "ManufactureTimeSec"));
        statement.setInt(i++, getRequiredInt(data, "EnergyConsMax"));
        statement.setInt(i++, getRequiredInt(data, "EnergyConsPowerUp"));
        statement.setInt(i++, getRequiredInt(data, "EnergyConsStandBy"));
        statement.setInt(i++, getRequiredInt(data, "EnergyConsOn"));
        statement.setInt(i++, getRequiredInt(data, "EnergyProdMin"));
        statement.setInt(i++, getRequiredInt(data, "EnergyProdMax"));
        statement.setInt(i++, getRequiredInt(data, "EnergyProdNominal"));
        statement.setInt(i++, getRequiredInt(data, "EnergyProdCritical"));
        statement.setInt(i++, getRequiredInt(data, "FuelConsumption"));
        statement.setInt(i++, getRequiredInt(data, "FuelQantity"));
        statement.setBytes(i++, booleanToBinary(data.get("FuelRadioactiveMaterial")));
        statement.setDouble(i++, getRequiredDouble(data, "DegradationSpeedPerYear"));
        statement.setString(i++, getRequiredString(data, "FuelType"));
        statement.setString(i++, getRequiredString(data, "DateOfRefueling"));
        statement.setInt(i++, getRequiredInt(data, "ControlLinesAmount"));
        statement.setInt(i++, getRequiredInt(data, "TermalLinesAmount"));
        statement.setInt(i++, getRequiredInt(data, "FuelLinesAmount"));
        statement.setInt(i++, getRequiredInt(data, "PowerLinesAmount"));
        statement.setString(i++, toJson(data.get("SizeDimentions")));
        statement.setString(i++, toJson(data.get("SpecificModParamCrew")));
        statement.setString(i++, toJson(data.get("SpecificModParamEmissions")));
        statement.setString(i++, toJson(data.get("SpecificModParamWeapon")));
        statement.setString(i++, toJson(data.get("SpecificModParamTrust")));
        statement.setInt(i++, getRequiredInt(data, "DockType"));
        statement.setString(i++, toJson(data.get("Armored")));
        statement.setString(i++, getRequiredString(data, "AmmoType"));
        statement.setInt(i++, getRequiredInt(data, "InternalDefence"));
        statement.setInt(i++, getRequiredInt(data, "MeshID"));
        statement.setString(i++, toJson(data.get("Blocks")));
        statement.setInt(i++, getRequiredInt(data, "technologyDirection"));
        statement.setInt(i++, getRequiredInt(data, "technologyLevel"));
        return i;
    }

    private long getRequiredLong(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required field for DB save: " + key);
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }

    private int getRequiredInt(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required field for DB save: " + key);
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private double getRequiredDouble(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required field for DB save: " + key);
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(String.valueOf(value));
    }

    private String getRequiredString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required field for DB save: " + key);
        }
        return String.valueOf(value);
    }

    private String stringifyNumber(Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    private byte[] booleanToBinary(Object value) {
        boolean flag;
        if (value instanceof Boolean) {
            flag = ((Boolean) value).booleanValue();
        } else {
            flag = Boolean.parseBoolean(String.valueOf(value));
        }
        return new byte[] { (byte) (flag ? 1 : 0) };
    }

    private String toJson(Object value) throws Exception {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return objectMapper.writeValueAsString(value);
    }
}
