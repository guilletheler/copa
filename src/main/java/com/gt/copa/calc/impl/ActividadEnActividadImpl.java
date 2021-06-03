/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IActividadEnActividad;
import com.gt.copa.calc.api.IComponenteDriver;

import lombok.Data;

/**
 *
 * @author guille
 */
@Data
public class ActividadEnActividadImpl implements IActividadEnActividad {

	IActividad origen;

	IActividad destino;

	IComponenteDriver componenteDriver;

	Double valorParticular;

	public ActividadEnActividadImpl() {
	}

	public ActividadEnActividadImpl(IActividad origen, IActividad destino, IComponenteDriver componenteDriver,
			Double valorParticular) {
		this.origen = origen;
		this.destino = destino;
		this.componenteDriver = componenteDriver;
		this.valorParticular = valorParticular;
	}

}
