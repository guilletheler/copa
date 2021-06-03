/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IActividadEnOC;
import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.IObjetoDeCosto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author guille
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadEnOCImpl implements IActividadEnOC {

	IActividad actividad;

	IObjetoDeCosto objetoDeCosto;

	IComponenteDriver componenteDriver;

	Double valorParticular;

	@Override
	public IActividad getOrigen() {
		return actividad;
	}

	@Override
	public IObjetoDeCosto getDestino() {
		return objetoDeCosto;
	}

}
