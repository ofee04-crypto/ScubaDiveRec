package com._7.scuba_fun.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DivingRecord {
    private Integer recId;
    private String divingType;
    private Integer diverId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String weather;
    private String location;
    private String gasType;
    private BigDecimal startGasBar;
    private BigDecimal endGasBar;
    private BigDecimal maxDeepthMeter;
    private BigDecimal visibilityMeter;
    private String account;
    private String creatureFound;
    private String experienceDescribe;
}
