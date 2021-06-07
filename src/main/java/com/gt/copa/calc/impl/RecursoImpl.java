/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.ArrayList;
import java.util.List;

import com.gt.copa.calc.api.IDato;
import com.gt.copa.calc.api.IRecurso;
import com.gt.copa.calc.api.IRecursoEnActividad;
import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.calc.api.TratamientoMuestra;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author guille
 */
@Data
@NoArgsConstructor
public class RecursoImpl implements IRecurso {

	Integer codigo;

	String nombre;

	List<IDato> datos;

	TipoDistribucion tipoDistribucion = TipoDistribucion.UNIFORME;

	TratamientoMuestra tratamientoMuestra = TratamientoMuestra.PROMEDIO;

	List<IRecursoEnActividad> asignacionesActividades;

	Boolean promedioNoVacio = true;

	public RecursoImpl(Integer codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public RecursoImpl(Integer codigo, String nombre, TipoDistribucion tipoDistribucion,
			TratamientoMuestra tratamientoMuestra, Boolean promedioNoVacio) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.tipoDistribucion = tipoDistribucion;
		this.tratamientoMuestra = tratamientoMuestra;
		this.promedioNoVacio = promedioNoVacio;
	}

	public List<IDato> getDatos() {
		if (datos == null) {
			datos = new ArrayList<>();
		}
		return datos;
	}

	public List<IRecursoEnActividad> getAsignacionesActividades() {
		if (asignacionesActividades == null) {
			asignacionesActividades = new ArrayList<>();
		}
		return asignacionesActividades;
	}

}
