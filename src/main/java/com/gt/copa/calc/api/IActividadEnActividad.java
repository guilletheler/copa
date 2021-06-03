package com.gt.copa.calc.api;

public interface IActividadEnActividad extends IAsignacion<IActividad, IActividad> {
	
	IActividad getOrigen();
	void setOrigen(IActividad origen);

	IActividad getDestino();
	void setDestino(IActividad destino);

	IComponenteDriver getComponenteDriver();
	void setComponenteDriver(IComponenteDriver  componenteDriver);

	Double getValorParticular();
	void setValorParticular(Double valorParticular);
}
