package com.gt.copa.calc.api;

import java.util.List;
import java.util.Map;

public interface ICostoEstandar extends IInducible {
	
	Map<IClasificacion, Double> getPreciosUnitarios();

    TipoDistribucion getTipoDistribucion();

    List<ICostoEstandarEnArticulo> getAsignacionesArticulos();
}
