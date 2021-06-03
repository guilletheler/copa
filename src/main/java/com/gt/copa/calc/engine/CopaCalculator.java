/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IActividadEnActividad;
import com.gt.copa.calc.api.IActividadEnOC;
import com.gt.copa.calc.api.IArticulo;
import com.gt.copa.calc.api.IAsignacion;
import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.ICostoEstandar;
import com.gt.copa.calc.api.ICostoEstandarEnArticulo;
import com.gt.copa.calc.api.IObjetoDeCosto;
import com.gt.copa.calc.api.IObjetoDeCostoEnArticulo;
import com.gt.copa.calc.api.IProceso;
import com.gt.copa.calc.api.IRecurso;
import com.gt.copa.calc.api.IRecursoEnActividad;

import lombok.Getter;
import lombok.Setter;

/**
 * Utilizar:<br/>
 * Cargar clasificaciones, ej: Fijo, Variable, Erogable, No Erogable<br/>
 * Cargar recursos, ej: Sueldos, Gas, Energía<br/>
 * Cargar datos en recursos<br/>
 * Cargar procesos, ej: Fabricar <br/>
 * Cargar actividades en procesos, ej: Moler, Peletear, Mantener<br/>
 * Cargar drivers, ej: kw, toneladas, hs mantenimiento<br/>
 * Cargar componentes de drivers, ej: kw moler, kw peletear, toneladas molido, toneladas peleteado<br/>
 * Cargar asignaciones de recursos en actividades<br/>
 * Cargar asignaciones de actividades em actividades<br/>
 * Cargar Objetos de costo, ej: Toneladas molidas, Toneladas peleteado<br/>
 * Cargar asignaciones de actividades en objetos de costo<br/>
 * Cargar artículos<br/>
 * 
 * @author guille
 */
public class CopaCalculator implements Calculable {

	List<Calculable> calculables;

	@Getter
	@Setter
	List<RecursoCalculado> recursos;

	@Getter
	@Setter
	List<ActividadCalculada<?>> actividades;

	@Getter
	@Setter
	List<ObjetoDeCostoCalculado> objetosDeCosto;

	@Getter
	@Setter
	List<ArticuloCalculado> articulos;

	@Getter
	@Setter
	List<CostoEstandarCalculado> costosEstandar;

	@Getter
	@Setter
	List<ProcesoCalculado> procesos;

	@Setter
	Map<Integer, IClasificacion> clasificaciones;

	@Getter
	@Setter
	Double totalRecusos = null;

	@Getter
	@Setter
	Double totalActividades = null;

	@Getter
	@Setter
	Double totalOC = null;

	@Getter
	@Setter
	boolean calculado = false;

	public CopaCalculator() {
		this.clearCalc();
	}

	public Double getMontoTotalRecursos() throws CopaEngineException {
		if (totalRecusos == null) {
			this.calcular();
			totalRecusos = 0d;
			for (RecursoCalculado rc : this.getRecursos()) {
				totalRecusos += rc.getMontoTotal();
			}
		}
		return totalRecusos;
	}

	public Double getMontoTotalActividades() throws CopaEngineException {
		if (totalActividades == null) {
			this.calcular();
			totalActividades = 0d;
			for (ActividadCalculada<?> ac : this.getActividades()) {
				totalActividades += ac.getMontoUltimaFase();
			}
		}
		return totalActividades;
	}

	public Double getMontoTotalObjetosDeCosto() throws CopaEngineException {
		if (totalOC == null) {
			this.calcular();
			totalOC = 0d;
			for (ObjetoDeCostoCalculado oc : this.getObjetosDeCosto()) {
				totalOC += oc.getMontoTotal();
			}
		}
		return totalOC;
	}

	public Double getMontoTotalAsignado(IAsignacion<?, ?> asignable) throws CopaEngineException {
		Double ret = 0d;

		for (IClasificacion cd : this.getClasificaciones().values()) {
			ret += getMontoAsignado(asignable, cd);
		}

		return ret;
	}

	/**
	 * Asignación de un origen en un destino
	 *
	 * @param asignable
	 * @param clasificacion
	 * @return
	 * @throws CopaEngineException
	 */
	public Double getMontoAsignado(IAsignacion<?, ?> asignable, IClasificacion clasificacion)
			throws CopaEngineException {

		Double montoOrigen = null;

		Double totalInductorOrigen = null;

		if (asignable.getOrigen() instanceof IRecurso) {
			RecursoCalculado rCalc = this.searchRecursoCalculado((IRecurso) asignable.getOrigen());
			montoOrigen = rCalc.getMonto(clasificacion);
			totalInductorOrigen = rCalc.getTotalInductor();
		} else if (asignable.getOrigen() instanceof IActividad) {
			ActividadCalculada<?> aCalc = this.searchActividadCalculada((IActividad) asignable.getOrigen());
			montoOrigen = aCalc.getMonto(clasificacion);
			totalInductorOrigen = aCalc.getTotalInductor();
		} else if (asignable.getOrigen() instanceof IObjetoDeCosto) {
			ObjetoDeCostoCalculado ocCalc = this.searchObjetoDeCostoCalculado((IObjetoDeCosto) asignable.getOrigen());
			montoOrigen = ocCalc.getMonto(clasificacion);
			totalInductorOrigen = ocCalc.getTotalInductor();
		} else if (asignable.getOrigen() instanceof ICostoEstandar) {
			CostoEstandarCalculado ceCalc = this.searchCostoEstandarCalculado((ICostoEstandar) asignable.getOrigen());
			montoOrigen = ceCalc.getMonto(clasificacion);
			totalInductorOrigen = ceCalc.getTotalInductor();
		}

		if (montoOrigen == null) {
			throw new RuntimeException("el monto del origen de " + asignable.getClass().getName() + " es nulo!");
		}

		if (totalInductorOrigen == null) {
			throw new RuntimeException(
					"el total del inductor del origen de " + asignable.getClass().getName() + " es nulo!");
		}

		/*
		 * if (totalInductorOrigen == 0d) { throw new
		 * RuntimeException("el total del inductor del origen de axa " +
		 * asignable.getOrigen().getNombre() + "->" + asignable.getDestino().getNombre()
		 * + " es 0!"); }
		 */
		Double ret = montoOrigen / totalInductorOrigen;

		switch (asignable.getOrigen().getTipoDistribucion()) {
		case UNIFORME:
			break;
		case DRIVER:
			ret = ret * asignable.getComponenteDriver().getValor();
			break;
		case PORCENTAJE:
		case VALOR:
			if (asignable.getValorParticular() == null) {
				throw new RuntimeException("el valor de origen es nulo!");
			}
			ret = ret * asignable.getValorParticular();
			break;
		}

		return ret;
	}

	public EstadoDistribucion getEstadoDistribucion(InducibleCalculado<?> inducible) throws CopaEngineException {
		int compare = inducible.getMontoTotal().compareTo(inducible.getMontoInducidoTotal());

		if (compare > 0) {
			return EstadoDistribucion.INCOMPLETA;
		} else if (compare < 0) {
			return EstadoDistribucion.SOBREINDUCIDA;
		}
		return EstadoDistribucion.COMPLETA;

	}

	@Override
	public void clearCalc() {
		for (Calculable calc : this.getCalculables()) {
			calc.clearCalc();
		}
		recursos = new ArrayList<>();
		actividades = new ArrayList<>();
		objetosDeCosto = new ArrayList<>();
		articulos = new ArrayList<>();
		costosEstandar = new ArrayList<>();
		procesos = new ArrayList<>();
		calculado = false;
	}

	@Override
	public void calcular() throws CopaEngineException {
		if (!calculado) {
			for (Calculable calc : this.getCalculables()) {
				calc.calcular();
			}
			calculado = true;
		}
	}

	public RecursoCalculado searchRecursoCalculado(Integer codigo) {
		for (RecursoCalculado r : this.recursos) {
			if (r.getDataObject().getCodigo().equals(codigo)) {
				return r;
			}
		}
		return null;
	}

	public RecursoCalculado searchRecursoCalculado(String nombre) {
		for (RecursoCalculado r : this.recursos) {
			if (r.getDataObject().getNombre().equals(nombre)) {
				return r;
			}
		}
		return null;
	}

	public RecursoCalculado searchRecursoCalculado(IRecurso oRecurso) {
		for (RecursoCalculado r : this.recursos) {
			if (r.getDataObject().equals(oRecurso)) {
				return r;
			}
		}

		return this.addRecurso(oRecurso);
	}

	public ProcesoCalculado searchProcesoCalculado(Integer codigo) {
		for (ProcesoCalculado r : this.procesos) {
			if (r.getDataObject().getCodigo().equals(codigo)) {
				return r;
			}
		}
		return null;
	}

	public ProcesoCalculado searchProcesoCalculado(String nombre) {
		for (ProcesoCalculado r : this.procesos) {
			if (r.getDataObject().getNombre().equals(nombre)) {
				return r;
			}
		}
		return null;
	}

	public ProcesoCalculado searchProcesoCalculado(IProceso oProceso) {
		for (ProcesoCalculado r : this.procesos) {
			if (r.getDataObject().equals(oProceso)) {
				return r;
			}
		}

		return this.addProceso(oProceso);
	}

	public ArticuloCalculado searchArticuloCalculado(Integer codigo) {
		for (ArticuloCalculado r : this.articulos) {
			if (r.getDataObject().getCodigo().equals(codigo)) {
				return r;
			}
		}
		return null;
	}

	public ArticuloCalculado searchArticuloCalculado(String nombre) {
		for (ArticuloCalculado r : this.articulos) {
			if (r.getDataObject().getNombre().equals(nombre)) {
				return r;
			}
		}
		return null;
	}

	public ArticuloCalculado searchArticuloCalculado(IArticulo oArticulo) {
		for (ArticuloCalculado r : this.articulos) {
			if (r.getDataObject().equals(oArticulo)) {
				return r;
			}
		}

		return this.addArticulo(oArticulo);
	}

	public CostoEstandarCalculado searchCostoEstandarCalculado(Integer codigo) {
		for (CostoEstandarCalculado r : this.costosEstandar) {
			if (r.getDataObject().getCodigo().equals(codigo)) {
				return r;
			}
		}
		return null;
	}

	public CostoEstandarCalculado searchCostoEstandarCalculado(String nombre) {
		for (CostoEstandarCalculado r : this.costosEstandar) {
			if (r.getDataObject().getNombre().equals(nombre)) {
				return r;
			}
		}
		return null;
	}

	public CostoEstandarCalculado searchCostoEstandarCalculado(ICostoEstandar oCostoEstandar) {
		for (CostoEstandarCalculado r : this.costosEstandar) {
			if (r.getDataObject().equals(oCostoEstandar)) {
				return r;
			}
		}

		return this.addCostoEstandar(oCostoEstandar);
	}

	public ActividadCalculada<?> searchActividadCalculada(Integer codigo) {
		for (ActividadCalculada<?> a : this.actividades) {
			if (a.getDataObject().getCodigo().equals(codigo)) {
				return a;
			}
		}
		return null;
	}

	public ActividadCalculada<?> searchActividadCalculada(String nombre) {
		for (ActividadCalculada<?> a : this.actividades) {
			if (a.getDataObject().getNombre().equals(nombre)) {
				return a;
			}
		}
		return null;
	}

	public ActividadCalculada<?> searchActividadCalculada(IActividad oActividad) {
		for (ActividadCalculada<?> a : this.actividades) {
			if (a.getDataObject().equals(oActividad)) {
				return a;
			}
		}

		return addActividad(oActividad);
	}

	public ObjetoDeCostoCalculado searchObjetoDeCostoCalculado(Integer codigo) {
		for (ObjetoDeCostoCalculado oc : this.objetosDeCosto) {
			if (oc.getDataObject().getCodigo().equals(codigo)) {
				return oc;
			}
		}
		return null;
	}

	public ObjetoDeCostoCalculado searchObjetoDeCostoCalculado(String nombre) {
		for (ObjetoDeCostoCalculado oc : this.objetosDeCosto) {
			if (oc.getDataObject().getNombre().equals(nombre)) {
				return oc;
			}
		}
		return null;
	}

	public ObjetoDeCostoCalculado searchObjetoDeCostoCalculado(IObjetoDeCosto objetoDeCosto) {
		for (ObjetoDeCostoCalculado oc : this.objetosDeCosto) {
			if (oc.getDataObject().equals(objetoDeCosto)) {
				return oc;
			}
		}

		return addObjetoDeCosto(objetoDeCosto);
	}

	public RecursoCalculado addRecurso(IRecurso r) {
		RecursoCalculado rc = new RecursoCalculado(this, r);

		this.recursos.add(rc);

		this.getCalculables().add(rc);

		return rc;
	}

	public void removeRecurso(IRecurso r) {

		RecursoCalculado rc = this.searchRecursoCalculado(r.getCodigo());

		if (rc != null) {
			this.getCalculables().remove(rc);
			this.recursos.remove(rc);
		}
	}

	public ProcesoCalculado addProceso(IProceso r) {
		ProcesoCalculado rc = new ProcesoCalculado(this, r);

		this.procesos.add(rc);

		this.getCalculables().add(rc);

		return rc;
	}

	public void removeProceso(IProceso r) {

		ProcesoCalculado rc = this.searchProcesoCalculado(r.getCodigo());

		if (rc != null) {
			this.getCalculables().remove(rc);
			this.procesos.remove(rc);
		}
	}

	public CostoEstandarCalculado addCostoEstandar(ICostoEstandar r) {
		CostoEstandarCalculado rc = new CostoEstandarCalculado(this, r);

		this.costosEstandar.add(rc);

		this.getCalculables().add(rc);

		return rc;
	}

	public void removeCostoEstandar(ICostoEstandar r) {

		CostoEstandarCalculado rc = this.searchCostoEstandarCalculado(r.getCodigo());

		if (rc != null) {
			this.getCalculables().remove(rc);
			this.costosEstandar.remove(rc);
		}
	}

	public ActividadCalculada<?> addActividad(IActividad a) {

		boolean esta = false;

		for (ProcesoCalculado pc : this.getProcesos()) {
			if (pc.getDataObject().equals(a.getProceso())) {
				esta = true;
				break;
			}
		}

		if (!esta) {
			throw new RuntimeException(
					"El proceso " + a.getProceso().getNombre() + " de la actividad " + a.getNombre() + " no existe!");
		}

		ActividadCalculada<?> ac = new ActividadCalculada<>(this, a);

		this.actividades.add(ac);
		this.getCalculables().add(ac);

		return ac;
	}

	public void removeActividad(IActividad a) {

		ActividadCalculada<?> ac = this.searchActividadCalculada(a.getCodigo());

		if (ac != null) {
			this.getCalculables().remove(ac);
			this.actividades.remove(ac);
		}
	}

	public ArticuloCalculado addArticulo(IArticulo a) {

		ArticuloCalculado ac = new ArticuloCalculado(this, a);

		this.articulos.add(ac);
		this.getCalculables().add(ac);

		return ac;
	}

	public void removeArticulo(IArticulo a) {

		ArticuloCalculado ac = this.searchArticuloCalculado(a.getCodigo());

		if (ac != null) {
			this.getCalculables().remove(ac);
			this.articulos.remove(ac);
		}
	}

	public ObjetoDeCostoCalculado addObjetoDeCosto(IObjetoDeCosto oc) {

		ObjetoDeCostoCalculado occ = new ObjetoDeCostoCalculado(this, oc);

		this.objetosDeCosto.add(occ);
		this.getCalculables().add(occ);

		return occ;
	}

	public void removeObjetoDeCosto(IObjetoDeCosto oc) {

		ObjetoDeCostoCalculado occ = this.searchObjetoDeCostoCalculado(oc.getCodigo());

		if (occ != null) {
			this.getCalculables().remove(occ);
			this.objetosDeCosto.remove(occ);
		}
	}

	public List<Calculable> getCalculables() {
		if (calculables == null) {
			calculables = new ArrayList<>();
		}
		return calculables;
	}

	public void setCalculables(List<Calculable> calculables) {
		this.calculables = calculables;
	}

	public IRecursoEnActividad asignar(IRecursoEnActividad rxa, IRecurso r, IActividad a) {
		return asignar(rxa, r, a, null, null);
	}

	public IRecursoEnActividad asignar(IRecursoEnActividad rxa, IRecurso r, IActividad a, IComponenteDriver cd) {
		return asignar(rxa, r, a, cd, null);
	}

	public IRecursoEnActividad asignar(IRecursoEnActividad rxa, IRecurso r, IActividad a, Double valor) {
		return asignar(rxa, r, a, null, valor);
	}

	public IRecursoEnActividad asignar(IRecursoEnActividad rxa, IRecurso r, IActividad a, IComponenteDriver cd,
			Double valorParricular) {
		rxa.setActividad(a);
		rxa.setRecurso(r);
		rxa.setComponenteDriver(cd);
		rxa.setValorParticular(valorParricular);

		a.getRecursosInducidos().add(rxa);

		r.getAsignacionesActividades().add(rxa);

		return rxa;
	}

	public ICostoEstandarEnArticulo asignar(ICostoEstandarEnArticulo rxa, ICostoEstandar ce, IArticulo a) {
		return asignar(rxa, ce, a, null, null);
	}

	public ICostoEstandarEnArticulo asignar(ICostoEstandarEnArticulo rxa, ICostoEstandar ce, IArticulo a,
			IComponenteDriver cd) {
		return asignar(rxa, ce, a, cd, null);
	}

	public ICostoEstandarEnArticulo asignar(ICostoEstandarEnArticulo rxa, ICostoEstandar ce, IArticulo a,
			Double valor) {
		return asignar(rxa, ce, a, null, valor);
	}

	public ICostoEstandarEnArticulo asignar(ICostoEstandarEnArticulo rxa, ICostoEstandar ce, IArticulo a,
			IComponenteDriver cd, Double valorParricular) {
		rxa.setDestino(a);
		rxa.setOrigen(ce);
		rxa.setComponenteDriver(cd);
		rxa.setValorParticular(valorParricular);

		a.getCostosEstandarInducidos().add(rxa);

		ce.getAsignacionesArticulos().add(rxa);

		return rxa;
	}

	public IObjetoDeCostoEnArticulo asignar(IObjetoDeCostoEnArticulo ocxa, IObjetoDeCosto oc, IArticulo a) {
		return asignar(ocxa, oc, a, null, null);
	}

	public IObjetoDeCostoEnArticulo asignar(IObjetoDeCostoEnArticulo ocxa, IObjetoDeCosto oc, IArticulo a,
			IComponenteDriver cd) {
		return asignar(ocxa, oc, a, cd, null);
	}

	public IObjetoDeCostoEnArticulo asignar(IObjetoDeCostoEnArticulo ocxa, IObjetoDeCosto oc, IArticulo a,
			Double valor) {
		return asignar(ocxa, oc, a, null, valor);
	}

	public IObjetoDeCostoEnArticulo asignar(IObjetoDeCostoEnArticulo ocxa, IObjetoDeCosto oc, IArticulo a,
			IComponenteDriver cd, Double valorParricular) {
		ocxa.setDestino(a);
		ocxa.setOrigen(oc);
		ocxa.setComponenteDriver(cd);
		ocxa.setValorParticular(valorParricular);

		a.getObjetosDeCostoInducidos().add(ocxa);

		oc.getAsignacionesArticulos().add(ocxa);

		return ocxa;
	}

	public IActividadEnActividad asignar(IActividadEnActividad axa, IActividad a1, IActividad a2) {
		return this.asignar(axa, a1, a2, null, null);
	}

	public IActividadEnActividad asignar(IActividadEnActividad axa, IActividad a1, IActividad a2,
			IComponenteDriver cd) {
		return this.asignar(axa, a1, a2, cd, null);
	}

	public IActividadEnActividad asignar(IActividadEnActividad axa, IActividad a1, IActividad a2, Double valor) {
		return this.asignar(axa, a1, a2, null, valor);
	}

	public IActividadEnActividad asignar(IActividadEnActividad axa, IActividad a1, IActividad a2, IComponenteDriver cd,
			Double valor) {

		if (a1 == null || a2 == null) {
			throw new RuntimeException("una de las actividades es nula");
		}

		axa.setOrigen(a1);
		axa.setDestino(a2);
		axa.setComponenteDriver(cd);
		axa.setValorParticular(valor);
		a2.getActividadesInducidas().add(axa);
		a1.getAsignacionesActividades().add(axa);

		return axa;
	}

	public IActividadEnOC asignar(IActividadEnOC axoc, IActividad a, IObjetoDeCosto oc) {
		return asignar(axoc, a, oc, null, null);
	}

	public IActividadEnOC asignar(IActividadEnOC axoc, IActividad a, IObjetoDeCosto oc, IComponenteDriver cd) {
		return this.asignar(axoc, a, oc, cd, null);
	}

	public IActividadEnOC asignar(IActividadEnOC axoc, IActividad a, IObjetoDeCosto oc, Double valor) {
		return this.asignar(axoc, a, oc, null, valor);
	}

	public IActividadEnOC asignar(IActividadEnOC axoc, IActividad a, IObjetoDeCosto oc, IComponenteDriver cd,
			Double valor) {

		axoc.setObjetoDeCosto(oc);
		axoc.setActividad(a);
		axoc.setComponenteDriver(cd);
		axoc.setValorParticular(valor);
		oc.getActividadesInducidas().add(axoc);
		a.getAsignacionesOC().add(axoc);

		return axoc;
	}

	public Map<Integer, IClasificacion> getClasificaciones() {
		if (clasificaciones == null) {
			clasificaciones = new HashMap<>();
		}
		return clasificaciones;
	}

}
