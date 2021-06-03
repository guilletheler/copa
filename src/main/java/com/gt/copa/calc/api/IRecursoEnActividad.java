package com.gt.copa.calc.api;

public interface IRecursoEnActividad extends IAsignacion<IRecurso, IActividad> {

	IRecurso getRecurso();
	void setRecurso(IRecurso recurso);

	IActividad getActividad();
	void setActividad(IActividad actividad);

	IComponenteDriver getComponenteDriver();
	void setComponenteDriver(IComponenteDriver componenteDriver);

	Double getValorParticular();
	void setValorParticular(Double valorParticular);
}
