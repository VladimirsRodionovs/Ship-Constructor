package org.example.shipconstructor.chassis.service;

import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.ChassisCoefficientSet;

public interface ChassisCoefficientProvider {
    ChassisCoefficientSet resolve(ChassisCalculationInput input);
}
