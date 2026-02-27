package org.example.shipconstructor.ui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModuleTypeCatalog {
    private static final Map<Integer, String> NAMES = buildNames();

    private ModuleTypeCatalog() {
    }

    public static String displayName(int typeId) {
        String name = NAMES.get(Integer.valueOf(typeId));
        return name == null ? (typeId + " - Unknown/Reserved") : (typeId + " - " + name);
    }

    public static Map<Integer, String> names() {
        return Collections.unmodifiableMap(NAMES);
    }

    private static Map<Integer, String> buildNames() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        map.put(Integer.valueOf(0), "Chassis");
        map.put(Integer.valueOf(1), "Casing front");
        map.put(Integer.valueOf(2), "Casing back");
        map.put(Integer.valueOf(3), "Casing top");
        map.put(Integer.valueOf(4), "Casing bottom");
        map.put(Integer.valueOf(5), "Casing port");
        map.put(Integer.valueOf(6), "Casing starboard");
        map.put(Integer.valueOf(7), "Drive engine");
        map.put(Integer.valueOf(8), "DE starter");
        map.put(Integer.valueOf(9), "Drive Engine and starter");
        map.put(Integer.valueOf(10), "Jump engine");
        map.put(Integer.valueOf(11), "JE starter");
        map.put(Integer.valueOf(12), "Jump Engine and starter");
        map.put(Integer.valueOf(13), "RCS fuel and control");
        map.put(Integer.valueOf(14), "RCS engine");
        map.put(Integer.valueOf(15), "Thrusters fuel and control");
        map.put(Integer.valueOf(16), "Thrusters engine");
        map.put(Integer.valueOf(17), "Inside energy source");
        map.put(Integer.valueOf(18), "Heat radiators");
        map.put(Integer.valueOf(19), "Outside energy source");
        map.put(Integer.valueOf(20), "Power controller and distributor");
        map.put(Integer.valueOf(21), "Life support");
        map.put(Integer.valueOf(22), "Waste management system");
        map.put(Integer.valueOf(23), "Bridge");
        map.put(Integer.valueOf(24), "Astrometrics, sensors and navigation");
        map.put(Integer.valueOf(25), "Engineering and damage control");
        map.put(Integer.valueOf(26), "Defense and fire control");
        map.put(Integer.valueOf(27), "Workshop");
        map.put(Integer.valueOf(28), "Recreation bay");
        map.put(Integer.valueOf(29), "Officers recreation bay");
        map.put(Integer.valueOf(30), "Crew cabins block");
        map.put(Integer.valueOf(31), "Captain cabin");
        map.put(Integer.valueOf(32), "Officers cabins block");
        map.put(Integer.valueOf(33), "Space suits bay");
        map.put(Integer.valueOf(34), "DE inertia dumping sync. and distributor");
        map.put(Integer.valueOf(35), "Medical station");
        map.put(Integer.valueOf(36), "Passenger recreation");
        map.put(Integer.valueOf(37), "Passenger cabins block");
        map.put(Integer.valueOf(38), "Passenger bay");
        map.put(Integer.valueOf(39), "Teletrap passenger/cargo lock");
        map.put(Integer.valueOf(40), "Passenger/cargo lock");
        map.put(Integer.valueOf(41), "Passenger/cargo lock apparel");
        map.put(Integer.valueOf(42), "Cargo bay small container");
        map.put(Integer.valueOf(43), "Cargo bay small container lock");
        map.put(Integer.valueOf(44), "Teletrap cargo bay small container lock");
        map.put(Integer.valueOf(45), "Cargo bay big container");
        map.put(Integer.valueOf(46), "Cargo bay big container lock");
        map.put(Integer.valueOf(47), "Teletrap cargo bay big container lock");
        map.put(Integer.valueOf(48), "Cargo bay general");
        map.put(Integer.valueOf(49), "Cargo bay general lock");
        map.put(Integer.valueOf(50), "Teletrap cargo bay general lock");
        map.put(Integer.valueOf(51), "Cargo bay bulk");
        map.put(Integer.valueOf(52), "Cargo bay bulk lock");
        map.put(Integer.valueOf(53), "Teletrap cargo bay bulk lock");
        map.put(Integer.valueOf(54), "Cargo bay liquid");
        map.put(Integer.valueOf(55), "Cargo bay liquid lock");
        map.put(Integer.valueOf(56), "Teletrap cargo bay liquid lock");
        map.put(Integer.valueOf(57), "Hangar");
        map.put(Integer.valueOf(58), "Hangar lock");
        map.put(Integer.valueOf(59), "Hangar apparel");
        map.put(Integer.valueOf(60), "Drone hangar");
        map.put(Integer.valueOf(61), "Drone hangar lock");
        map.put(Integer.valueOf(62), "Battle Link system");
        map.put(Integer.valueOf(63), "Communication");
        map.put(Integer.valueOf(64), "Communication array");
        map.put(Integer.valueOf(65), "Sensor array");
        map.put(Integer.valueOf(66), "Sensor module");
        map.put(Integer.valueOf(67), "ECM array");
        map.put(Integer.valueOf(68), "ECM module");
        map.put(Integer.valueOf(69), "Decoy launch module");
        map.put(Integer.valueOf(70), "Data processing module");
        map.put(Integer.valueOf(71), "Corridor and com module");
        map.put(Integer.valueOf(72), "Corridor and com module with lock");
        map.put(Integer.valueOf(73), "Defense station");
        map.put(Integer.valueOf(74), "Assault holders");
        map.put(Integer.valueOf(75), "Docking connecting holders");
        map.put(Integer.valueOf(76), "Adaptive teletrap docking lock");
        map.put(Integer.valueOf(77), "Tug mechanics");
        map.put(Integer.valueOf(78), "Landing gear");
        map.put(Integer.valueOf(79), "Turret mount");
        map.put(Integer.valueOf(80), "Large turret mount");
        map.put(Integer.valueOf(81), "Fixed mount");
        map.put(Integer.valueOf(82), "Missile launcher bay");
        map.put(Integer.valueOf(83), "Ammo bay");
        map.put(Integer.valueOf(84), "Self destruction module");
        map.put(Integer.valueOf(85), "Cleaning and disinfection module");
        map.put(Integer.valueOf(86), "Science lab");
        map.put(Integer.valueOf(87), "Escape pods");
        map.put(Integer.valueOf(88), "Armory");
        map.put(Integer.valueOf(89), "Casing penetration equipment");
        map.put(Integer.valueOf(90), "Drone armory");
        map.put(Integer.valueOf(91), "Vault");
        map.put(Integer.valueOf(92), "Kitchen");
        map.put(Integer.valueOf(93), "Small ship docking slot");
        map.put(Integer.valueOf(94), "Flight deck hangar and locks");
        map.put(Integer.valueOf(95), "Flight deck workshop");
        map.put(Integer.valueOf(96), "Flight deck ammo Bay");
        map.put(Integer.valueOf(97), "Flight deck fuel tank");
        map.put(Integer.valueOf(98), "Flight deck bridge");
        map.put(Integer.valueOf(99), "Gyroscopes");
        map.put(Integer.valueOf(100), "Hybernation block");
        map.put(Integer.valueOf(101), "Entertainment Block");
        map.put(Integer.valueOf(102), "Sanitation block");
        map.put(Integer.valueOf(103), "Utilization block");
        map.put(Integer.valueOf(104), "Cargo bay Cryogenic liquid");
        map.put(Integer.valueOf(105), "Cargo bay Cryogenic liquid lock");
        map.put(Integer.valueOf(106), "Cargo bay LPG");
        map.put(Integer.valueOf(107), "Cargo bay LPG lock");
        map.put(Integer.valueOf(108), "Cargo bay Ro-Ro");
        map.put(Integer.valueOf(109), "Cargo bay Ro-Ro apparel");
        map.put(Integer.valueOf(110), "Cargo bay Ro-Ro lock");
        map.put(Integer.valueOf(111), "Cargo bay Livestock");
        map.put(Integer.valueOf(112), "Cargo bay Livestock lock");
        map.put(Integer.valueOf(113), "Cargo bay Livestock apparel");
        map.put(Integer.valueOf(114), "Cargo bay Chemicals");
        map.put(Integer.valueOf(115), "Cargo bay Chemicals lock");
        map.put(Integer.valueOf(116), "Cargo bay Refrigerated");
        map.put(Integer.valueOf(117), "Cargo bay Refrigerated Lock");
        return map;
    }
}
