package com.gt.copa.component;

import com.gt.copa.calc.engine.CopaCalculator;
import com.gt.copa.infra.CopaStatus;
import com.gt.copa.service.calcLoader.CalculatorBuilderService;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class CurrentStatus {

    @Setter
    CopaStatus copaStatus;
    
    @Getter
    @Setter
    CopaCalculator copaCalculator;

    public void buildCalculator(CalculatorBuilderService calculatorBuilderService) {
        copaCalculator = calculatorBuilderService.buildCalculator(copaStatus);
    }

    public CopaStatus getCopaStatus() {
        if(copaStatus == null) {
            copaStatus = new CopaStatus();
        }

        return copaStatus;
    }
}
