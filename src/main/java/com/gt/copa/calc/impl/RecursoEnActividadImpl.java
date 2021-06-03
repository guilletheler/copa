/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.IRecurso;
import com.gt.copa.calc.api.IRecursoEnActividad;

import lombok.Data;

/**
 *
 * @author guille
 */
@Data
public class RecursoEnActividadImpl implements IRecursoEnActividad {

	IRecurso recurso;

	IActividad actividad;

	IComponenteDriver componenteDriver;

	Double valorParticular;

	public RecursoEnActividadImpl() {
	}

	public RecursoEnActividadImpl(IRecurso recurso, IActividad actividad, IComponenteDriver componenteDriver,
			Double valorParticular) {
		this.recurso = recurso;
		this.actividad = actividad;
		this.componenteDriver = componenteDriver;
		this.valorParticular = valorParticular;
	}

	@Override
	public IRecurso getOrigen() {
		return recurso;
	}

	@Override
	public IActividad getDestino() {
		return actividad;
	}

	
}
