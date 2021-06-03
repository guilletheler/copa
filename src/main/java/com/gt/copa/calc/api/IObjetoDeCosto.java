package com.gt.copa.calc.api;

import java.util.List;

public interface IObjetoDeCosto extends IInducible {

	/**
	 * Se utiliza para calcular el valor unitario
	 */
	IComponenteDriver getComponenteDriver();

	Double getValorParticular();

	List<IActividadEnOC> getActividadesInducidas();

	List<IObjetoDeCostoEnArticulo> getAsignacionesArticulos();

	Double monto = null;
}
