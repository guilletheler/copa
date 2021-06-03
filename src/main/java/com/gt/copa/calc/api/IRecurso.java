package com.gt.copa.calc.api;

import java.util.List;

public interface IRecurso extends IInducible {

	Integer getCodigo();

	String getNombre();

	List<IDato> getDatos();

	TipoDistribucion getTipoDistribucion();

	TratamientoMuestra getTratamientoMuestra();

	List<IRecursoEnActividad> getAsignacionesActividades();

	Boolean getPromedioNoVacio();
}
