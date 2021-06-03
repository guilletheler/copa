/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.HashMap;
import java.util.Map;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IActividadEnActividad;
import com.gt.copa.calc.api.IActividadEnOC;
import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IRecursoEnActividad;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author guille
 *
 */
public class ActividadCalculada<DO extends IActividad> implements Calculable, InducibleCalculado<DO> {

	@Getter
	@Setter
	DO dataObject;

	Map<IClasificacion, Double> montos = null;

	Map<IClasificacion, Double> montosRxA = null;

	Map<IClasificacion, Double> montosAxA = null;

	@Setter
	Double totalInductor = null;

	Map<IClasificacion, Double> montosInducidos = null;

	@Getter
	@Setter
	CopaCalculator copaCalculator;

	public ActividadCalculada(CopaCalculator copaCalculator) {
		this.copaCalculator = copaCalculator;
	}

	public ActividadCalculada(CopaCalculator copaCalculator, DO actividad) {
		this(copaCalculator);
		this.dataObject = actividad;
	}

	@Override
	public void clearCalc() {
		this.totalInductor = null;
		this.montos = null;
		this.montosRxA = null;
		this.montosAxA = null;
		this.montosInducidos = null;
	}

	@Override
	public void calcular() throws CopaEngineException {
		this.getMontos();
		this.getMontosInducidos();
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
				totalInductor = ((Integer) this.getDataObject().getAsignacionesActividades().size()).doubleValue()
						+ ((Integer) this.getDataObject().getAsignacionesOC().size()).doubleValue();
				break;
			case VALOR:

				for (IActividadEnActividad axa : this.getDataObject().getAsignacionesActividades()) {
					if (axa.getValorParticular() == null) {
						throw new CopaEngineException(
								"Al estar seteado como VALOR el tipo de distribución todas las asignaciones de actividad en actividad deberán tener un valor");
					}
					totalInductor += axa.getValorParticular();
				}
				for (IActividadEnOC axoc : this.getDataObject().getAsignacionesOC()) {
					if (axoc.getValorParticular() == null) {
						throw new CopaEngineException(
								"Al estar seteado como VALOR el tipo de distribución todas las asignaciones de actividad en oc deberán tener un valor");
					}
					totalInductor += axoc.getValorParticular();
				}
				break;
			case DRIVER:
				for (IActividadEnActividad axa : this.getDataObject().getAsignacionesActividades()) {
					if (axa.getComponenteDriver() == null) {
						throw new CopaEngineException(
								"Al estar seteado como DRIVER el tipo de distribución todas las asignaciones de actividad en actividad deberán tener un driver");
					}
					if (axa.getComponenteDriver().getValor() == null) {
						throw new CopaEngineException("Al estar seteado como DRIVER "
								+ axa.getComponenteDriver().getNombre() + " deberán tener un valor");
					}
					if (axa.getComponenteDriver().getValor() == 0d) {
						throw new CopaEngineException("Al estar seteado como DRIVER "
								+ axa.getComponenteDriver().getNombre() + " deberán tener un valor no cero");
					}
					totalInductor += axa.getComponenteDriver().getValor();
				}
				for (IActividadEnOC axoc : this.getDataObject().getAsignacionesOC()) {
					if (axoc.getComponenteDriver() == null) {
						throw new CopaEngineException(
								"Al estar seteado como DRIVER el tipo de distribución todas las asignaciones de actividad en oc deberán tener un driver");
					}

					if (axoc.getComponenteDriver().getValor() == null) {
						throw new CopaEngineException("Al estar seteado como DRIVER "
								+ axoc.getComponenteDriver().getNombre() + " deberán tener un valor");
					}

					if (axoc.getComponenteDriver().getValor() == 0d) {
						throw new CopaEngineException("Al estar seteado como DRIVER "
								+ axoc.getComponenteDriver().getNombre() + " deberán tener un valor no cero");
					}

					totalInductor += axoc.getComponenteDriver().getValor();
				}
				break;
			}
		}

		return totalInductor;
	}

	/**
	 * Calcula el monto inducido de esta actividad en otro lado
	 *
	 * @param clasificacionData
	 * @return
	 * @throws CopaEngineException
	 */
	private Double calcularMontoInducido(IClasificacion clasificacionData) throws CopaEngineException {

		if (this.getDataObject().getPrimaria() && this.getDataObject().getAsignacionesActividades().size() > 0) {

			StringBuilder sb = new StringBuilder();

			for (IActividadEnActividad axa : this.getDataObject().getAsignacionesActividades()) {
				if (sb.length() > 0) {
					sb.append(";");
				}
				sb.append("->");
				sb.append(axa.getDestino().getNombre());
			}

			throw new CopaEngineException(
					"La actividad primaria " + this.getDataObject().getNombre() + " está asignada en actividad(es) "
							+ sb.toString() + "\npero solo puede inducirse en objetos de costo");
		}

		Double montoInducido = 0d;
		for (IActividadEnActividad axa : this.getDataObject().getAsignacionesActividades()) {
			montoInducido += copaCalculator.getMontoAsignado(axa, clasificacionData);
		}
		for (IActividadEnOC axoc : this.getDataObject().getAsignacionesOC()) {
			montoInducido += copaCalculator.getMontoAsignado(axoc, clasificacionData);
		}

		return montoInducido;
	}

	@Override
	public Double getMontoInducido(IClasificacion clasificacionData) throws CopaEngineException {
		return this.getMontosInducidos().get(clasificacionData);
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

	public Double getMontoPrimeraFase() throws CopaEngineException {
		Double monto = 0d;
		for (IClasificacion cd : this.getMontosRxA().keySet()) {
			monto += this.getMontosRxA().get(cd);
		}
		return monto;
	}

	public Double getMontoUltimaFase() throws CopaEngineException {
		if (this.getDataObject().getPrimaria()) {
			return this.getMontoTotal();
		} else {
			return this.getMontoTotal() - this.getMontoInducidoTotal();
		}

	}

	@Override
	public Double getMonto(IClasificacion clasificacionData) throws CopaEngineException {
		Double ret = this.getMontos().get(clasificacionData);
		if (ret == null) {
			ret = 0d;
		}
		return ret;
	}

	private Double calcularMontoRxA(IClasificacion clasificacionData) throws CopaEngineException {

		Double monto = 0d;
		for (IRecursoEnActividad rxa : this.getDataObject().getRecursosInducidos()) {

			monto += copaCalculator.getMontoAsignado(rxa, clasificacionData);
		}

		return monto;
	}

	private Double calcularMontoAxA(IClasificacion clasificacionData) throws CopaEngineException {

		Double monto = 0d;
		for (IActividadEnActividad axa : this.getDataObject().getActividadesInducidas()) {

			monto += copaCalculator.getMontoAsignado(axa, clasificacionData);
		}

		return monto;
	}

	@Override
	public Map<IClasificacion, Double> getMontos() throws CopaEngineException {
		if (montos == null) {
			montos = new HashMap<>();
			for (IClasificacion cd : this.getCopaCalculator().getClasificaciones().values()) {
				montos.put(cd, this.getMontosRxA().get(cd) + this.getMontosAxA().get(cd));
			}
		}
		return montos;
	}

	public Map<IClasificacion, Double> getMontosRxA() throws CopaEngineException {
		if (montosRxA == null) {
			montosRxA = new HashMap<>();
			for (IClasificacion cd : this.getCopaCalculator().getClasificaciones().values()) {
				montosRxA.put(cd, this.calcularMontoRxA(cd));
			}
		}
		return montosRxA;
	}

	public Map<IClasificacion, Double> getMontosAxA() throws CopaEngineException {
		if (montosAxA == null) {
			montosAxA = new HashMap<>();
			for (IClasificacion cd : this.getCopaCalculator().getClasificaciones().values()) {
				montosAxA.put(cd, this.calcularMontoAxA(cd));
			}
		}
		return montosAxA;
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
