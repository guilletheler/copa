/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.api;

/**
 *
 * @author guille
 * @param <I>
 * @param <CN>
 */
public interface IAsignacion<I extends IInducible, CN extends ICodigoNombre> {

	I getOrigen();

	CN getDestino();

	IComponenteDriver getComponenteDriver();

	Double getValorParticular();
}
