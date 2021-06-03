/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.HashMap;
import java.util.Map;

import com.gt.copa.calc.api.IArticulo;
import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.ICostoEstandarEnArticulo;
import com.gt.copa.calc.api.IObjetoDeCostoEnArticulo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author guillermo
 */
public class ArticuloCalculado implements Calculable {

	@Getter
	@Setter
	IArticulo dataObject;

	Map<IClasificacion, Double> montos = null;

	@Getter
	@Setter
	CopaCalculator copaCalculator;

	public ArticuloCalculado(CopaCalculator copaCalculator) {
		this.copaCalculator = copaCalculator;
	}

	public ArticuloCalculado(CopaCalculator copaCalculator, IArticulo articulo) {
		this(copaCalculator);
		this.dataObject = articulo;
	}

	@Override
	public void clearCalc() {
		this.montos = null;
	}

	@Override
	public void calcular() throws CopaEngineException {
		this.getMontoTotal();
	}

	public Double getMontoTotal() throws CopaEngineException {
		Double monto = 0d;
		for (IClasificacion cd : this.getMontos().keySet()) {
			if (this.getMontos().get(cd) != null) {
				if (this.getMontos().get(cd) != null) {
					monto += this.getMontos().get(cd);
				}
			}

		}
		return monto;
	}

	private Double calcularMonto(IClasificacion IClasificacion) throws CopaEngineException {

		Double monto = 0d;
		for (IObjetoDeCostoEnArticulo ocxa : this.getDataObject().getObjetosDeCostoInducidos()) {
			ObjetoDeCostoCalculado occ = this.copaCalculator.searchObjetoDeCostoCalculado(ocxa.getOrigen().getCodigo());
			monto += occ.getValorUnitario(copaCalculator.getMontoAsignado(ocxa, IClasificacion));
		}

		for (ICostoEstandarEnArticulo cexa : this.getDataObject().getCostosEstandarInducidos()) {
			monto += copaCalculator.getMontoAsignado(cexa, IClasificacion);
		}

		return monto;
	}

	public Map<IClasificacion, Double> getMontos() throws CopaEngineException {
		if (montos == null) {
			montos = new HashMap<>();
			for (IClasificacion cd : this.getCopaCalculator().getClasificaciones().values()) {
				montos.put(cd, this.calcularMonto(cd));
			}
		}
		return montos;
	}

	public void setMontos(Map<IClasificacion, Double> montos) {
		this.montos = montos;
	}
}
