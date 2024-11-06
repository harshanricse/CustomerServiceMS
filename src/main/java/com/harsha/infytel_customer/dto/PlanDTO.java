package com.harsha.infytel_customer.dto;

import com.harsha.infytel_customer.entity.Plan;
import lombok.Data;

@Data
public class PlanDTO {
    private Integer planId;
    private String planName;
    private Integer nationalRate;
    private Integer localRate;

    //converts entity to dto
    public static PlanDTO valueOf(Plan plan){
        PlanDTO planDTO = new PlanDTO();
        planDTO.setPlanId(plan.getPlanId());
        planDTO.setPlanName(plan.getPlanName());
        planDTO.setNationalRate(plan.getNationalRate());
        planDTO.setLocalRate(plan.getLocalRate());
        return planDTO;
    }
}
