package com.gt.copa.calc.api;

import java.util.List;

public interface IActividad extends IInducible {

	IProceso getProceso();

	List<IRecursoEnActividad> getRecursosInducidos();

	/**
	 * Otras actividades inducidas en esta
	 */
	List<IActividadEnActividad> getActividadesInducidas();

	List<IActividadEnOC> getAsignacionesOC();

	/**
	 * Esta actividad inducida en otras
	 */
	List<IActividadEnActividad> getAsignacionesActividades();

	TipoDistribucion getTipoDistribucion();

	Boolean getPrimaria();
}
