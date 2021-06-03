package com.gt.copa.calc.api;

public interface IObjetoDeCostoEnArticulo extends IAsignacion<IObjetoDeCosto, IArticulo> {

	IObjetoDeCosto getOrigen();
	void setOrigen(IObjetoDeCosto origen);

	IArticulo getDestino();
	void setDestino(IArticulo articulo);

	IComponenteDriver getComponenteDriver();
	void setComponenteDriver(IComponenteDriver componenteDriver);

	Double getValorParticular();
	void setValorParticular(Double valorParticular);
}
