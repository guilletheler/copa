package com.gt.copa.calc.api;

import java.util.List;

public interface IArticulo extends ICodigoNombre {
	List<ICostoEstandarEnArticulo> getCostosEstandarInducidos();

	List<IObjetoDeCostoEnArticulo> getObjetosDeCostoInducidos();
}
