package org.example.shipconstructor.chassis.service;

import org.example.shipconstructor.chassis.domain.ChassisCalculationTrace;
import org.example.shipconstructor.chassis.domain.ChassisTraceEntry;

public final class ChassisTraceFormatter {
    private ChassisTraceFormatter() {
    }

    public static String format(ChassisCalculationTrace trace) {
        if (trace == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (ChassisTraceEntry e : trace.getEntries()) {
            sb.append('[').append(e.getStage()).append("] ").append(e.getKey());
            if (e.getValue() != null) {
                sb.append(" = ").append(trim(e.getValue().doubleValue()));
            }
            if (e.getNote() != null && !e.getNote().isEmpty()) {
                sb.append(" :: ").append(e.getNote());
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static String trim(double value) {
        String s = String.format(java.util.Locale.US, "%.6f", value);
        int i = s.length() - 1;
        while (i > 0 && s.charAt(i) == '0') {
            i--;
        }
        if (s.charAt(i) == '.') {
            i--;
        }
        return s.substring(0, i + 1);
    }
}
