/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.HashMap;
import java.util.Map;

import com.gt.copa.calc.api.IActividadEnOC;
import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IObjetoDeCosto;
import com.gt.copa.calc.api.IObjetoDeCostoEnArticulo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author guille
 */
public class ObjetoDeCostoCalculado implements Calculable, InducibleCalculado<IObjetoDeCosto> {

	@Getter
	@Setter
	IObjetoDeCosto dataObject;

	Map<IClasificacion, Double> montos = null;

	Double totalInductor = null;

	Map<IClasificacion, Double> montosInducidos = null;

	@Getter
	@Setter
	CopaCalculator copaCalculator;

	public ObjetoDeCostoCalculado(CopaCalculator copaCalculator) {
		this.copaCalculator = copaCalculator;
	}

	public ObjetoDeCostoCalculado(CopaCalculator copaCalculator, IObjetoDeCosto objetoDeCosto) {
		this(copaCalculator);
		this.dataObject = objetoDeCosto;
	}

	@Override
	public void calcular() throws CopaEngineException {
		this.getMontos();
		this.getMontosInducidos();
		this.getValorUnitarioTotal();
	}

	@Override
	public void clearCalc() {
		this.montos = null;
		this.montosInducidos = null;
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
	public Double getMonto(IClasificacion IClasificacion) throws CopaEngineException {
		Double ret = this.getMontos().get(IClasificacion);
		if (ret == null) {
			ret = 0d;
		}
		return ret;
	}

	public Double calcularMonto(IClasificacion IClasificacion) throws CopaEngineException {

		Double monto = 0d;
		for (IActividadEnOC axoc : this.getDataObject().getActividadesInducidas()) {
			monto += this.copaCalculator.getMontoAsignado(axoc, IClasificacion);
		}

		return monto;
	}

	public Double getValorUnitario(IClasificacion IClasificacion) throws CopaEngineException {
		return this.getValorUnitario(this.getMonto(IClasificacion));
	}

	public Double getValorUnitarioTotal() throws CopaEngineException {
		Double ret = 0d;

		for (IClasificacion cd : this.getCopaCalculator().getClasificaciones().values()) {
			ret += this.getValorUnitario(cd);
		}

		return ret;
	}

	public Double getValorUnitario(Double valor) throws CopaEngineException {
		if (this.getDataObject().getComponenteDriver() == null && this.getDataObject().getValorParticular() == null) {
			throw new CopaEngineException(
					"No se puede calcultar el valor unitario sin driver y sin valor particular en OC "
							+ this.getDataObject().getCodigo() + " " + this.getDataObject().getNombre());
		}

		return valor / this.getCantidad();

	}

	public Double getCantidad() throws CopaEngineException {
		if (this.getDataObject().getComponenteDriver() == null && this.getDataObject().getValorParticular() == null) {
			throw new CopaEngineException("No se puede obtener la cantidad del OC sin valor particular en OC "
					+ this.getDataObject().getCodigo() + " " + this.getDataObject().getNombre());
		}

		if (this.getDataObject().getComponenteDriver() != null) {
			return this.getDataObject().getComponenteDriver().getValor();
		}

		return this.getDataObject().getValorParticular();
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

				for (IObjetoDeCostoEnArticulo ocxa : this.getDataObject().getAsignacionesArticulos()) {
					if (ocxa.getValorParticular() == null) {
						throw new CopaEngineException(
								"Al estar seteado como VALOR el tipo de distribución todas las asignaciones de oc en articulo deberán tener un valor");
					}
					totalInductor += ocxa.getValorParticular();
				}

				break;
			case DRIVER:
				for (IObjetoDeCostoEnArticulo ocxa : this.getDataObject().getAsignacionesArticulos()) {
					if (ocxa.getComponenteDriver() == null) {
						throw new CopaEngineException(
								"Al estar seteado como DRIVER el tipo de distribución todas las asignaciones de oc en articulo deberán tener un driver");
					}
					if (ocxa.getComponenteDriver().getValor() == null) {
						throw new CopaEngineException("Al estar seteado como DRIVER "
								+ ocxa.getComponenteDriver().getNombre() + " deberán tener un valor");
					}
					if (ocxa.getComponenteDriver().getValor() == 0d) {
						throw new CopaEngineException("Al estar seteado como DRIVER "
								+ ocxa.getComponenteDriver().getNombre() + " deberán tener un valor no cero");
					}
					totalInductor += ocxa.getComponenteDriver().getValor();
				}

				break;
			}
		}

		return totalInductor;
	}

	private Double calcularMontoInducido(IClasificacion IClasificacion) throws CopaEngineException {
		Double montoInducido = 0d;
		for (IObjetoDeCostoEnArticulo ocxa : this.getDataObject().getAsignacionesArticulos()) {
			montoInducido += copaCalculator.getMontoAsignado(ocxa, IClasificacion);
		}

		return montoInducido;
	}

	@Override
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
