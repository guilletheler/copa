package com.gt.copa.calc.api;

import java.util.List;

public interface IProceso extends ICodigoNombre {
	Integer getCodigo();

	String getNombre();

	List<IActividad> getActividades();
}
