package com.gt.copa.service.calcLoader;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.IDriver;
import com.gt.copa.calc.engine.ActividadCalculada;
import com.gt.copa.calc.engine.ArticuloCalculado;
import com.gt.copa.calc.engine.CopaCalculator;
import com.gt.copa.calc.engine.CostoEstandarCalculado;
import com.gt.copa.calc.engine.ObjetoDeCostoCalculado;
import com.gt.copa.calc.engine.RecursoCalculado;
import com.gt.copa.calc.impl.ActividadEnActividadImpl;
import com.gt.copa.calc.impl.ActividadEnOCImpl;
import com.gt.copa.calc.impl.ActividadImpl;
import com.gt.copa.calc.impl.ArticuloImpl;
import com.gt.copa.calc.impl.ClasificacionImpl;
import com.gt.copa.calc.impl.ComponenteDriverImpl;
import com.gt.copa.calc.impl.CostoEstandarEnArticuloImpl;
import com.gt.copa.calc.impl.CostoEstandarImpl;
import com.gt.copa.calc.impl.DatoImpl;
import com.gt.copa.calc.impl.DriverImpl;
import com.gt.copa.calc.impl.ObjetoDeCostoEnArticuloImpl;
import com.gt.copa.calc.impl.ObjetoDeCostoImpl;
import com.gt.copa.calc.impl.ProcesoImpl;
import com.gt.copa.calc.impl.RecursoEnActividadImpl;
import com.gt.copa.calc.impl.RecursoImpl;
import com.gt.copa.infra.CopaStatus;
import com.gt.copa.model.atemporal.Articulo;
import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.Driver;
import com.gt.copa.model.atemporal.Proceso;
import com.gt.copa.model.periodico.ActividadEnActividad;
import com.gt.copa.model.periodico.ActividadEnObjetoDeCosto;
import com.gt.copa.model.periodico.ActividadPeriodica;
import com.gt.copa.model.periodico.ComponenteDriverPeriodico;
import com.gt.copa.model.periodico.CostoEstandarEnArticulo;
import com.gt.copa.model.periodico.CostoEstandarPeriodico;
import com.gt.copa.model.periodico.ObjetoDeCostoEnArticulo;
import com.gt.copa.model.periodico.ObjetoDeCostoPeriodico;
import com.gt.copa.model.periodico.RecursoEnActividad;
import com.gt.copa.model.periodico.RecursoPeriodico;
import com.gt.copa.model.temporal.TipoPeriodo;
import com.gt.copa.model.temporal.ValorDato;
import com.gt.copa.repo.atemporal.ArticuloRepo;
import com.gt.copa.repo.atemporal.ClasificacionDatoRepo;
import com.gt.copa.repo.atemporal.DriverRepo;
import com.gt.copa.repo.atemporal.ProcesoRepo;
import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.repo.periodico.ActividadEnActividadRepo;
import com.gt.copa.repo.periodico.ActividadEnObjetoDeCostoRepo;
import com.gt.copa.repo.periodico.ActividadPeriodicaRepo;
import com.gt.copa.repo.periodico.ComponenteDriverPeriodicoRepo;
import com.gt.copa.repo.periodico.CostoEstandarEnArticuloRepo;
import com.gt.copa.repo.periodico.CostoEstandarPeriodicoRepo;
import com.gt.copa.repo.periodico.ObjetoDeCostoEnArticuloRepo;
import com.gt.copa.repo.periodico.ObjetoDeCostoPeriodicoRepo;
import com.gt.copa.repo.periodico.RecursoEnActividadRepo;
import com.gt.copa.repo.periodico.RecursoPeriodicoRepo;
import com.gt.copa.repo.temporal.ValorDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Log
@Service
public class CalculatorBuilderService {

	@Autowired
	ClasificacionDatoRepo clasificacionDatoRepo;

	@Autowired
	ArticuloRepo articuloRepo;

	@Autowired
	RecursoRepo recursoRepo;

	@Autowired
	ProcesoRepo procesoRepo;

	@Autowired
	DriverRepo driverRepo;

	@Autowired
	ValorDatoRepo valorDatoRepo;

	@Autowired
	RecursoPeriodicoRepo recursoPeriodicoRepo;

	@Autowired
	ActividadPeriodicaRepo actividadPeriodicaRepo;

	@Autowired
	ComponenteDriverPeriodicoRepo componenteDriverPeriodicoRepo;

	@Autowired
	ObjetoDeCostoEnArticuloRepo objetoDeCostoEnArticuloRepo;

	@Autowired
	CostoEstandarPeriodicoRepo costoEstandarPeriodicoRepo;

	@Autowired
	RecursoEnActividadRepo recursoEnActividadRepo;

	@Autowired
	ActividadEnActividadRepo actividadEnActividadRepo;

	@Autowired
	ActividadEnObjetoDeCostoRepo actividadEnObjetoDeCostoRepo;

	@Autowired
	ObjetoDeCostoPeriodicoRepo objetoDeCostoPeriodicoRepo;

	@Autowired
	CostoEstandarEnArticuloRepo costoEstandarEnArticuloRepo;

	public CopaCalculator buildCalculator(CopaStatus filtros) {

		CopaCalculator ret = new CopaCalculator();

		Map<Integer, IDriver> drivers = this.cargarDrivers(filtros);

		Map<Integer, IComponenteDriver> componentesDrivers = this.cargarComponentesDriver(filtros, drivers);

		this.cargarClasificaciones(ret, filtros);

		this.cargarRecursos(ret, filtros);

		this.cargarProcesosActividades(ret, filtros);

		this.cargarObjetosDeCosto(ret, filtros, componentesDrivers);

		this.cargarArticulos(ret, filtros);

		this.cargarCostosEstandar(ret, filtros);

		this.cargarRxA(ret, filtros, componentesDrivers);

		this.cargarAxA(ret, filtros, componentesDrivers);

		this.cargarAxOC(ret, filtros, componentesDrivers);

		this.cargarOCxA(ret, filtros, componentesDrivers);

		this.cargarCExA(ret, filtros, componentesDrivers);

		return ret;
	}

	private void cargarClasificaciones(CopaCalculator calc, CopaStatus filtros) {

		StreamSupport.stream(clasificacionDatoRepo.findAll().spliterator(), false)
				.forEach(cd -> calc.getClasificaciones().put(cd.getCodigo(),
						ClasificacionImpl.builder().codigo(cd.getCodigo()).nombre(cd.getNombre()).build()));

	}

	private void cargarRecursos(CopaCalculator copaCalc, CopaStatus filtros) {

		List<RecursoPeriodico> list = recursoPeriodicoRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(filtros.getEmpresa(),
				filtros.getEscenario(), filtros.getPeriodo());

		for (RecursoPeriodico r : list) {

			RecursoImpl recurso = new RecursoImpl(r.getRecurso().getCodigo(), r.getRecurso().getNombre(),
					r.getTipoDistribucion(), r.getTratamientoMuestra(), r.getPromedioNoVacio());

			Calendar inicio = Calendar.getInstance();

			int meses = (-1) * (getMesesPeriodo(r.getTamanioMuestra(), r.getConfiguracionPeriodo().getPeriodo().getTipoPeriodo()) - 1);

			inicio.setTime(r.getConfiguracionPeriodo().getPeriodo().getFin());
			inicio.set(Calendar.DAY_OF_MONTH, 1);
			inicio.add(Calendar.MONTH, meses);

			List<ValorDato> datos = getValoresDatos(filtros, r, inicio);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			if (datos.isEmpty()) {
				log.info("Cargando recursos: No hay datos para el recurso " + r.getRecurso().getNombre()
						+ " en el escenario " + r.getConfiguracionPeriodo().getEscenario().getNombre() + " en el periodo "
						+ r.getConfiguracionPeriodo().getPeriodo().getNombre() + " (" + sdf.format(inicio.getTime()) + "-"
						+ sdf.format(r.getConfiguracionPeriodo().getPeriodo().getFin()) + ")"
						+ Arrays.toString(filtros.getFiltroClasificaciones().toArray()));
			} else {
				log.info("Cargando recursos: Se encontraron " + datos.size() + " " + r.getRecurso().getNombre()
						+ " en el escenario " + r.getConfiguracionPeriodo().getEscenario().getNombre() + " en el periodo "
						+ r.getConfiguracionPeriodo().getPeriodo().getNombre() + " (" + sdf.format(inicio.getTime()) + "-"
						+ sdf.format(r.getConfiguracionPeriodo().getPeriodo().getFin()) + ")"
						+ Arrays.toString(filtros.getFiltroClasificaciones().toArray()));
			}

			// en teoria un dato por periodo
			for (ValorDato dato : datos) {

				Set<ClasificacionImpl> clasif = new HashSet<>();

				for (ClasificacionDato cd : dato.getClasificaciones()) {
					clasif.add(new ClasificacionImpl(cd.getCodigo(), cd.getNombre()));
				}

				for (ClasificacionDato cd : dato.getDato().getClasificaciones()) {
					clasif.add(new ClasificacionImpl(cd.getCodigo(), cd.getNombre()));
				}

				recurso.getDatos().add(new DatoImpl(dato.getDato().getCodigo(), dato.getDato().getNombre(),
						dato.getFecha(), dato.getValor(), clasif));
			}
			copaCalc.addRecurso(recurso);

		}
	}

	private List<ValorDato> getValoresDatos(CopaStatus filtros, RecursoPeriodico r, Calendar inicio) {
		List<ValorDato> datos = valorDatoRepo.findByDato_RecursoAndEscenarioAndFechaBetween(r, filtros.getEscenario(),
				inicio.getTime(), r.getConfiguracionPeriodo().getPeriodo().getFin());

		return datos.stream()
				.filter(vd -> vd.getClasificaciones().stream().anyMatch(cla -> matchClasificacion(filtros, cla)))
				.collect(Collectors.toList());
	}

	private boolean matchClasificacion(CopaStatus filtros, ClasificacionDato cla) {
		return filtros.getFiltroDatos().stream()
				.anyMatch(str -> cla.getNombre().toUpperCase().contains(str.toUpperCase()));
	}

	private int getMesesPeriodo(int meses, TipoPeriodo tipoPeriodo) {
		switch (tipoPeriodo) {
			case MENSUAL:
				break;
			case BIMESTRAL:
				meses = meses * 2;
				break;
			case TRIMESTRAL:
				meses = meses * 3;
				break;
			case SEMESTRAL:
				meses = meses * 6;
				break;
			case ANUAL:
				meses = meses * 12;
				break;
		}

		return meses;
	}

	private void cargarProcesosActividades(CopaCalculator copaCalc, CopaStatus filtros) {

		List<Proceso> lst = procesoRepo.findByEmpresa(filtros.getEmpresa());

		for (Proceso p : lst) {
			ProcesoImpl proceso = new ProcesoImpl(p.getCodigo(), p.getNombre());

			copaCalc.addProceso(proceso);

			List<ActividadPeriodica> lsta = actividadPeriodicaRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
					filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

			for (ActividadPeriodica a : lsta) {
				ActividadImpl actividad = new ActividadImpl(a.getActividad().getCodigo(), a.getActividad().getNombre(),
						proceso, a.getTipoDistribucion(), a.getActividad().getPrimaria());

				copaCalc.addActividad(actividad);
			}
		}
	}

	private Map<Integer, IDriver> cargarDrivers(CopaStatus filtros) {

		Map<Integer, IDriver> driversMap = new HashMap<>();

		List<Driver> drivers = driverRepo.findByEmpresa(filtros.getEmpresa());

		drivers.forEach(d -> driversMap.put(d.getCodigo(),
				DriverImpl.builder().codigo(d.getCodigo()).nombre(d.getNombre()).build()));

		return driversMap;
	}

	private Map<Integer, IComponenteDriver> cargarComponentesDriver(CopaStatus filtros, Map<Integer, IDriver> drivers) {

		List<ComponenteDriverPeriodico> componentesDrivers = componenteDriverPeriodicoRepo
				.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(filtros.getEmpresa(), filtros.getEscenario(),
						filtros.getPeriodo());

		Map<Integer, IComponenteDriver> componentesDriversMap = new HashMap<>();

		componentesDrivers.forEach(vcd -> componentesDriversMap.put(vcd.getComponenteDriver().getCodigo(),
				ComponenteDriverImpl.builder().codigo(vcd.getComponenteDriver().getCodigo())
						.nombre(vcd.getComponenteDriver().getNombre())
						.driver(drivers.get(vcd.getComponenteDriver().getDriver().getCodigo())).valor(vcd.getValor())
						.build()));

		return componentesDriversMap;
	}

	private void cargarObjetosDeCosto(CopaCalculator calc, CopaStatus filtros,
			Map<Integer, IComponenteDriver> componentesDrivers) {

		List<ObjetoDeCostoPeriodico> lst = objetoDeCostoPeriodicoRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
				filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

		for (ObjetoDeCostoPeriodico oc : lst) {

			if (oc == null) {
				log.warning("Advertencia: CopaCalculator - OC nulo");
				continue;
			}

			IComponenteDriver cd = null;

			if (oc.getComponenteDriver() == null) {
				log.warning("Advertencia: CopaCalculator - Componente deriver nulo para OC "
						+ oc.getObjetoDeCosto().getNombre());
			} else {
				cd = componentesDrivers.get(oc.getComponenteDriver().getCodigo());
			}

			if (oc.getTipoDistribucion() == null) {
				log.warning("Advertencia: CopaCalculator - Tipo de distribucion nulo para OC "
						+ oc.getObjetoDeCosto().getNombre());
				continue;
			}

			ObjetoDeCostoImpl objetoDeCosto = new ObjetoDeCostoImpl(oc.getObjetoDeCosto().getCodigo(),
					oc.getObjetoDeCosto().getNombre(), cd, oc.getValorParticular(), oc.getTipoDistribucion());
			calc.addObjetoDeCosto(objetoDeCosto);
		}
	}

	private void cargarArticulos(CopaCalculator calc, CopaStatus filtros) {
		List<Articulo> articulos = articuloRepo.findByEmpresa(filtros.getEmpresa());

		articulos.forEach(art -> calc.getArticulos().add(new ArticuloCalculado(calc,
				ArticuloImpl.builder().codigo(art.getCodigo()).nombre(art.getNombre()).build())));

	}

	private void cargarCostosEstandar(CopaCalculator copaCalc, CopaStatus filtros) {

		List<CostoEstandarPeriodico> lstCE = costoEstandarPeriodicoRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
				filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

		for (CostoEstandarPeriodico ce : lstCE) {

			Map<IClasificacion, Double> montos = new HashMap<>();

			for (ClasificacionDato cd : ce.getClasificaciones()) {
				if (filtros.getFiltroClasificaciones().contains(cd)) {
					if (montos.containsKey(copaCalc.getClasificaciones().get(cd.getCodigo()))) {
						// throw new RuntimeException("Hay más de un valor de costo estandar en el
						// priodo y clasificacion");
						log.warning(
								"Generando CopaCalc: Hay más de un valor de costo estandar en el priodo y clasificacion "
										+ cd.getTipoClasificacion().getNombre());
					} else {
						montos.put(copaCalc.getClasificaciones().get(cd.getCodigo()), ce.getValor());
					}
				}
			}

			for (ClasificacionDato cd : ce.getCostoEstandar().getClasificaciones()) {
				if (filtros.getTipoClasificacion().getClasificaciones().contains(cd)) {
					if (montos.containsKey(copaCalc.getClasificaciones().get(cd.getCodigo()))) {
						// throw new RuntimeException("Hay más de un valor de costo estandar en el
						// priodo y clasificacion");
						log.warning(
								"Generando CopaCalc: Hay más de un valor de costo estandar en el periodo y clasificacion "
										+ cd.getTipoClasificacion().getNombre());
					} else {
						montos.put(copaCalc.getClasificaciones().get(cd.getCodigo()), ce.getValor());
					}
				}
			}

			CostoEstandarImpl costoEstandar = new CostoEstandarImpl(ce.getCostoEstandar().getCodigo(),
					ce.getCostoEstandar().getNombre(), montos, ce.getTipoDistribucion());

			copaCalc.addCostoEstandar(costoEstandar);
		}
	}

	private void cargarRxA(CopaCalculator copaCalc, CopaStatus filtros,
			Map<Integer, IComponenteDriver> componentesDrivers) {

		List<RecursoEnActividad> lstrea = recursoEnActividadRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
				filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

		for (RecursoEnActividad rxa : lstrea) {
			RecursoCalculado rcalc = copaCalc.searchRecursoCalculado(rxa.getRecurso().getCodigo());
			ActividadCalculada<?> acalc = copaCalc.searchActividadCalculada(rxa.getActividad().getCodigo());
			IComponenteDriver cd = null;

			if (rxa.getComponenteDriver() != null) {

				cd = componentesDrivers.get(rxa.getComponenteDriver().getCodigo());

				if (cd == null) {
					log.warning("Generando CopaCalc: No se encuentra valor para el componente driver "
							+ rxa.getComponenteDriver().getCodigo());
				}
			}

			if (rcalc == null) {
				log.warning("Generando CopaCalc: Recurso origen nulo");
				continue;
			}

			if (acalc == null) {
				log.warning("Generando CopaCalc: Actividad destino nulo");
				continue;
			}

			copaCalc.asignar(new RecursoEnActividadImpl(), rcalc.getDataObject(), acalc.getDataObject(), cd,
					rxa.getValorParticular());
		}
	}

	private void cargarAxA(CopaCalculator copaCalc, CopaStatus filtros,
			Map<Integer, IComponenteDriver> componentesDrivers) {

		List<ActividadEnActividad> lstaea = actividadEnActividadRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
				filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

		for (ActividadEnActividad axa : lstaea) {
			ActividadCalculada<?> aOrigen = copaCalc.searchActividadCalculada(axa.getOrigen().getCodigo());
			ActividadCalculada<?> aDestino = copaCalc.searchActividadCalculada(axa.getDestino().getCodigo());
			IComponenteDriver cd = null;

			if (axa.getComponenteDriver() != null) {

				cd = componentesDrivers.get(axa.getComponenteDriver().getCodigo());

				if (cd == null) {
					log.warning("Generando CopaCalc: No se encuentra valor para el componente driver "
							+ axa.getComponenteDriver().getCodigo());
				}
			}

			if (aOrigen == null) {
				log.warning("Generando CopaCalc: Actividad origen nulo");
				continue;
			}

			if (aDestino == null) {
				log.warning("Generando CopaCalc: Actividad destino nulo");
				continue;
			}
			copaCalc.asignar(new ActividadEnActividadImpl(), aOrigen.getDataObject(), aDestino.getDataObject(), cd,
					axa.getValorParticular());
		}
	}

	private void cargarAxOC(CopaCalculator copaCalc, CopaStatus filtros,
			Map<Integer, IComponenteDriver> componentesDrivers) {
		List<ActividadEnObjetoDeCosto> lstaeoc = actividadEnObjetoDeCostoRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
				filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

		for (ActividadEnObjetoDeCosto axoc : lstaeoc) {
			ActividadCalculada<?> aOrigen = copaCalc.searchActividadCalculada(axoc.getActividad().getCodigo());
			ObjetoDeCostoCalculado ocDestino = copaCalc
					.searchObjetoDeCostoCalculado(axoc.getObjetoDeCosto().getCodigo());
			IComponenteDriver cd = null;

			if (axoc.getComponenteDriver() != null) {

				cd = componentesDrivers.get(axoc.getComponenteDriver().getCodigo());

				if (cd == null) {
					log.warning("Generando CopaCalc: No se encuentra valor para el componente driver "
							+ axoc.getComponenteDriver().getCodigo());
				}
			}

			if (aOrigen == null) {
				log.warning("Generando CopaCalc: Actividad origen nulo");
				continue;
			}

			if (ocDestino == null) {
				log.warning("Generando CopaCalc: OC destino nulo");
				continue;
			}
			copaCalc.asignar(new ActividadEnOCImpl(), aOrigen.getDataObject(), ocDestino.getDataObject(), cd,
					axoc.getValorParticular());
		}
	}

	private void cargarOCxA(CopaCalculator copaCalc, CopaStatus filtros,
			Map<Integer, IComponenteDriver> componentesDrivers) {
		List<ObjetoDeCostoEnArticulo> lstocea = objetoDeCostoEnArticuloRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
				filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

		for (ObjetoDeCostoEnArticulo ocxa : lstocea) {
			ObjetoDeCostoCalculado ocOrigen = copaCalc
					.searchObjetoDeCostoCalculado(ocxa.getObjetoDeCosto().getCodigo());
			ArticuloCalculado aDestino = copaCalc.searchArticuloCalculado(ocxa.getArticulo().getCodigo());
			IComponenteDriver cd = null;

			if (ocxa.getComponenteDriver() != null) {

				cd = componentesDrivers.get(ocxa.getComponenteDriver().getCodigo());

				if (cd == null) {
					log.warning("Generando CopaCalc: No se encuentra valor para el componente driver "
							+ ocxa.getComponenteDriver().getCodigo());
				}
			}

			if (ocOrigen == null) {
				log.warning("Generando CopaCalc: OC origen nulo");
				continue;
			}

			if (aDestino == null) {
				log.warning("Generando CopaCalc: Articulo destino nulo");
				continue;
			}
			copaCalc.asignar(new ObjetoDeCostoEnArticuloImpl(), ocOrigen.getDataObject(), aDestino.getDataObject(), cd,
					ocxa.getValorParticular());
		}
	}

	private void cargarCExA(CopaCalculator copaCalc, CopaStatus filtros,
			Map<Integer, IComponenteDriver> componentesDrivers) {
		List<CostoEstandarEnArticulo> lstceea = costoEstandarEnArticuloRepo.findByConfiguracionPeriodo_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
				filtros.getEmpresa(), filtros.getEscenario(), filtros.getPeriodo());

		for (CostoEstandarEnArticulo cexa : lstceea) {
			CostoEstandarCalculado ceOrigen = copaCalc
					.searchCostoEstandarCalculado(cexa.getCostoEstandar().getCodigo());
			ArticuloCalculado aDestino = copaCalc.searchArticuloCalculado(cexa.getArticulo().getCodigo());
			IComponenteDriver cd = null;

			if (cexa.getComponenteDriver() != null) {

				cd = componentesDrivers.get(cexa.getComponenteDriver().getCodigo());

				if (cd == null) {
					log.warning("Generando CopaCalc: No se encuentra valor para el componente driver "
							+ cexa.getComponenteDriver().getCodigo());
				}
			}

			if (ceOrigen == null) {
				log.warning("Generando CopaCalc: Costo Estandar origen nulo");
				continue;
			}

			if (aDestino == null) {
				log.warning("Generando CopaCalc: Articulo destino nulo");
				continue;
			}
			copaCalc.asignar(new CostoEstandarEnArticuloImpl(), ceOrigen.getDataObject(), aDestino.getDataObject(), cd,
					cexa.getValorParticular());
		}
	}

}
