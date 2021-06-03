/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import com.gt.copa.calc.api.IArticulo;
import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.ICostoEstandar;
import com.gt.copa.calc.api.ICostoEstandarEnArticulo;

import lombok.Data;

/**
 *
 * @author guillermo
 */
@Data
public class CostoEstandarEnArticuloImpl implements ICostoEstandarEnArticulo {

	ICostoEstandar origen;

	IArticulo destino;

	IComponenteDriver componenteDriver;

	Double valorParticular;

	public CostoEstandarEnArticuloImpl() {
	}

	public CostoEstandarEnArticuloImpl(ICostoEstandar origen, IArticulo destino, IComponenteDriver componenteDriver,
			Double valorParticular) {
		this.origen = origen;
		this.destino = destino;
		this.componenteDriver = componenteDriver;
		this.valorParticular = valorParticular;
	}

}
