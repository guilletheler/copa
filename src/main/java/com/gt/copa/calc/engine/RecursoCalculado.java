/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.HashMap;
import java.util.Map;

import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IDato;
import com.gt.copa.calc.api.IRecurso;
import com.gt.copa.calc.api.IRecursoEnActividad;
import com.gt.copa.calc.api.TratamientoMuestra;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author guille
 */
public class RecursoCalculado implements Calculable, InducibleCalculado<IRecurso> {

	@Getter
	@Setter
	IRecurso dataObject;

	Double totalInductor = null;

	Map<IClasificacion, Double> montosInducidos = null;

	@Getter
	@Setter
	CopaCalculator copaCalculator;

	Map<IClasificacion, Double> montos;

	public RecursoCalculado(CopaCalculator copaCalculator) {
		this.copaCalculator = copaCalculator;
	}

	public RecursoCalculado(CopaCalculator copaCalculator, IRecurso recurso) {
		this.dataObject = recurso;
		this.copaCalculator = copaCalculator;
	}

	@Override
	public void calcular() throws CopaEngineException {
		this.getMontos();
		this.getMontosInducidos();
	}

	@Override
	public void clearCalc() {
		this.totalInductor = null;
		this.montos = null;
		this.montosInducidos = null;
	}

	@Override
	public Double getMontoTotal() {
		Double monto = 0d;
		for (IClasificacion cd : this.getMontos().keySet()) {
			monto += this.getMontos().get(cd);
		}
		return monto;
	}

	@Override
	public Double getMonto(IClasificacion clasificacion) {
		Double ret = this.getMontos().get(clasificacion);
		if (ret == null) {
			ret = 0d;
		}
		return ret;
	}

	private Double calcularMonto(IClasificacion IClasificacion) {

		Double monto = 0d;

		int cant = 0;
		for (IDato d : this.getDataObject().getDatos()) {

			if (d == null || d.getMonto() == null) {
				if (!this.getDataObject().getPromedioNoVacio()) {
					cant++;
				}
			} else {
				if (d.getClasificaciones().contains(IClasificacion)) {
					monto += d.getMonto();
				}
				cant++;
			}
		}

		if (this.getDataObject().getTratamientoMuestra() == TratamientoMuestra.PROMEDIO) {
			monto = monto / cant;
		}
		return monto;
	}

	public Double calcularMontoInducido(IClasificacion IClasificacion) throws CopaEngineException {
		Double montoInducido = 0d;
		for (IRecursoEnActividad rxa : this.getDataObject().getAsignacionesActividades()) {
			montoInducido += copaCalculator.getMontoAsignado(rxa, IClasificacion);
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
	public Double getTotalInductor() throws CopaEngineException {
		if (totalInductor == null) {
			totalInductor = 0d;

			switch (this.getDataObject().getTipoDistribucion()) {
			case PORCENTAJE:
				totalInductor = 100d;
				break;
			case UNIFORME:
				totalInductor = ((Integer) this.getDataObject().getAsignacionesActividades().size()).doubleValue();
				break;
			case VALOR:
				for (IRecursoEnActividad rxa : this.getDataObject().getAsignacionesActividades()) {
					if (rxa.getValorParticular() == null) {
						throw new CopaEngineException(
								"Al estar seteado como VALORecursoCalc el tipo de distribución todas las asignaciones de recurso en actividad deberán tener un valor");
					}
					totalInductor += rxa.getValorParticular();
				}
				break;
			case DRIVER:
				for (IRecursoEnActividad rxa : this.getDataObject().getAsignacionesActividades()) {
					if (rxa.getComponenteDriver() == null) {
						throw new CopaEngineException(
								"Al estar seteado como DRIVERecursoCalc el tipo de distribución todas las asignaciones de recurso en actividad deberán tener un driver");
					}
					totalInductor += rxa.getComponenteDriver().getValor();
				}
				break;
			}

		}

		return totalInductor;
	}

	@Override
	public Map<IClasificacion, Double> getMontos() {
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

	public void setMontosInducidos(Map<IClasificacion, Double> montosInducidos) {
		this.montosInducidos = montosInducidos;
	}

}
