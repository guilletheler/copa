package com.gt.copa.calc.api;

import java.util.Date;
import java.util.Set;

public interface IDato extends ICodigoNombre {

	String getReferencia();

	Date getFecha();

	Double getMonto();

	Set<IClasificacion> getClasificaciones();

}
