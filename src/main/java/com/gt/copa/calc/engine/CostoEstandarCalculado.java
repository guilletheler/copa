/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.HashMap;
import java.util.Map;

import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.ICostoEstandar;
import com.gt.copa.calc.api.ICostoEstandarEnArticulo;

/**
 *
 * @author guille
 */
public class CostoEstandarCalculado implements Calculable, InducibleCalculado<ICostoEstandar> {

	ICostoEstandar dataObject;

	Double totalInductor = null;

	Map<IClasificacion, Double> montosInducidos = null;

	CopaCalculator copaCalculator;

	public CostoEstandarCalculado(CopaCalculator copaCalculator) {
		this.copaCalculator = copaCalculator;
	}

	public CostoEstandarCalculado(CopaCalculator copaCalculator, ICostoEstandar costoestandar) {
		this.dataObject = costoestandar;
		this.copaCalculator = copaCalculator;
	}

	@Override
	public ICostoEstandar getDataObject() {
		return dataObject;
	}

	public void setDataObject(ICostoEstandar dataObject) {
		this.dataObject = dataObject;
	}

	@Override
	public void calcular() throws CopaEngineException {
		this.getMontos();
		this.getMontosInducidos();
	}

	@Override
	public void clearCalc() {
		this.totalInductor = null;
		this.montosInducidos = null;
	}

	@Override
	public Double getMonto(IClasificacion IClasificacion) {

		Double ret = this.getDataObject().getPreciosUnitarios().get(IClasificacion);

		if (ret == null) {
			ret = 0d;
		}

		return ret;
	}

	private Double calcularMontoInducido(IClasificacion IClasificacion) throws CopaEngineException {
		Double montoInducido = 0d;
		for (ICostoEstandarEnArticulo cexa : this.getDataObject().getAsignacionesArticulos()) {
			montoInducido += copaCalculator.getMontoAsignado(cexa, IClasificacion);
		}

		return montoInducido;
	}

	@Override
	public Double getMontoInducido(IClasificacion IClasificacion) throws CopaEngineException {
		return this.getMontosInducidos().get(IClasificacion);
	}

	@Override
	public Double getMontoInducidoTotal() throws CopaEngineException {
		Double ret = 0d;

		for (Double d : this.getMontosInducidos().values()) {
			ret += d;
		}

		return ret;
	}

	@Override
	public Double getMontoTotal() throws CopaEngineException {
		Double monto = 0d;
		for (IClasificacion cd : this.getMontos().keySet()) {
			monto += this.getMontos().get(cd);
		}
		return monto;
	}

	@Override
	public Double getTotalInductor() throws CopaEngineException {
		if (totalInductor == null) {
			totalInductor = 0d;

			switch (this.getDataObject().getTipoDistribucion()) {
			case PORCENTAJE:
				totalInductor = 100d;
				break;
			case UNIFORME:
				totalInductor = ((Integer) this.getDataObject().getAsignacionesArticulos().size()).doubleValue();
				break;
			case VALOR:
				for (ICostoEstandarEnArticulo cexa : this.getDataObject().getAsignacionesArticulos()) {
					if (cexa.getValorParticular() == null) {
						throw new CopaEngineException(
								"Al estar seteado como VALOCostoEstandarCalc el tipo de distribución todas las asignaciones de costoestandar en actividad deberán tener un valor");
					}
					totalInductor += cexa.getValorParticular();
				}
				break;
			case DRIVER:
				for (ICostoEstandarEnArticulo cexa : this.getDataObject().getAsignacionesArticulos()) {
					if (cexa.getComponenteDriver() == null) {
						throw new CopaEngineException(
								"Al estar seteado como DRIVECostoEstandarCalc el tipo de distribución todas las asignaciones de costoestandar en actividad deberán tener un driver");
					}
					totalInductor += cexa.getComponenteDriver().getValor();
				}
				break;
			}

		}

		return totalInductor;
	}

	public CopaCalculator getCopaCalculator() {
		return copaCalculator;
	}

	public void setCopaCalculator(CopaCalculator copaCalculator) {
		this.copaCalculator = copaCalculator;
	}

	@Override
	public Map<IClasificacion, Double> getMontos() throws CopaEngineException {
		return this.getDataObject().getPreciosUnitarios();
	}

	@Override
	public Map<IClasificacion, Double> getMontosInducidos() throws CopaEngineException {
		if (montosInducidos == null) {
			montosInducidos = new HashMap<>();
			for (IClasificacion cd : this.getCopaCalculator().getClasificaciones().values()) {
				montosInducidos.put(cd, this.calcularMontoInducido(cd));
			}
		}

		return montosInducidos;
	}

}
