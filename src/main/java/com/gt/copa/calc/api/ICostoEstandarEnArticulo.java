package com.gt.copa.calc.api;

public interface ICostoEstandarEnArticulo extends IAsignacion<ICostoEstandar, IArticulo> {

	ICostoEstandar getOrigen();
	
	void setOrigen(ICostoEstandar  origen);

	IArticulo getDestino();
	void setDestino(IArticulo destino);

	IComponenteDriver getComponenteDriver();
	void setComponenteDriver(IComponenteDriver componenteDriver);

	Double getValorParticular();
	void setValorParticular(Double valorParticular);
}
