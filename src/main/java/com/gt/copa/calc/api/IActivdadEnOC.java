package com.gt.copa.calc.api;

public interface IActivdadEnOC extends IAsignacion<IActividad, IObjetoDeCosto> {

	IActividad getActividad();

	IObjetoDeCosto getObjetoDeCosto();

	IComponenteDriver getComponenteDriver();

	Double getValorParticular();
}
