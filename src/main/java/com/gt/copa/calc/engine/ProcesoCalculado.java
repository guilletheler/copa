/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.HashMap;
import java.util.Map;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IProceso;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author guille
 */
public class ProcesoCalculado implements Calculable {

	@Getter
	@Setter
	IProceso dataObject;

	CopaCalculator copaCalculator;

	Map<IClasificacion, Double> montos = null;

	Map<IClasificacion, Double> montosInducidos = null;

	public ProcesoCalculado(CopaCalculator copaCalculator) {
		this.copaCalculator = copaCalculator;
	}

	public ProcesoCalculado(CopaCalculator copaCalculator, IProceso dataObject) {
		this.dataObject = dataObject;
		this.copaCalculator = copaCalculator;
	}

	@Override
	public void calcular() throws CopaEngineException {
		if (montos == null) {
			montos = new HashMap<>();
			montosInducidos = new HashMap<>();

			for (IClasificacion cd : copaCalculator.getClasificaciones().values()) {
				montos.put(cd, 0d);
				montosInducidos.put(cd, 0d);
			}

			for (IActividad ad : dataObject.getActividades()) {

				ActividadCalculada<?> ac = copaCalculator.searchActividadCalculada(ad);

				if (ac.getDataObject().getPrimaria()) {
					for (IClasificacion cd : copaCalculator.getClasificaciones().values()) {
						montos.put(cd, montos.get(cd) + ac.getMonto(cd));
						montosInducidos.put(cd, montosInducidos.get(cd) + ac.getMontoInducido(cd));
					}
				}

			}

		}
	}

	@Override
	public void clearCalc() {
		montos = null;
	}

	public Map<IClasificacion, Double> getMontos() throws CopaEngineException {
		if (montos == null) {
			calcular();
		}
		return montos;
	}

	public Map<IClasificacion, Double> getMontosInducidos() throws CopaEngineException {
		if (montosInducidos == null) {
			calcular();
		}
		return montosInducidos;
	}

	public Double getMonto(IClasificacion cd) throws CopaEngineException {
		return this.getMontos().get(cd);
	}

	public Double getMonto() throws CopaEngineException {
		Double ret = 0d;
		for (Double d : this.getMontos().values()) {
			ret += d;
		}
		return ret;
	}

	public Double getMontoInducido() throws CopaEngineException {
		Double ret = 0d;
		for (Double d : this.getMontosInducidos().values()) {
			ret += d;
		}
		return ret;
	}

}
