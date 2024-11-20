package com.simplogics.base.dto;

import com.simplogics.base.enums.RiskLevels;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DangerNotify {
    RiskLevels riskLevel;
    Long count;

}
