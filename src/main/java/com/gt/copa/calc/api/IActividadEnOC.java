package com.gt.copa.calc.api;

public interface IActividadEnOC extends IAsignacion<IActividad, IObjetoDeCosto> {

	IActividad getActividad();
	void setActividad(IActividad actividad);

	IObjetoDeCosto getObjetoDeCosto();
	void setObjetoDeCosto(IObjetoDeCosto objetoDeCosto);

	IComponenteDriver getComponenteDriver();
	void setComponenteDriver(IComponenteDriver componenteDriver);

	Double getValorParticular();
	void setValorParticular(Double valorParticular);

}
