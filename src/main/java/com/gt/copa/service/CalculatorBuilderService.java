package com.gt.copa.service;

import org.springframework.stereotype.Service;

import com.gt.copa.calc.engine.CopaCalculator;

@Service
public class CalculatorBuilderService {

	public CopaCalculator buildCalculator(CopaFiltroDatos filtros) {

		CopaCalculator ret = new CopaCalculator();
		

		return ret;
	}
}
