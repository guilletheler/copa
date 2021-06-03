/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

import java.util.Map;

import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IInducible;

/**
 *
 * @author guille
 * @param <DO>
 */
public interface InducibleCalculado<DO extends IInducible> {

	DO getDataObject();

	Double getMontoTotal() throws CopaEngineException;

	Double getMonto(IClasificacion clasificacionData) throws CopaEngineException;

	Map<IClasificacion, Double> getMontos() throws CopaEngineException;

	/**
	 * Suma de todos los inductores, o sea, por cuanto se debe dividir
	 * 
	 * @return
	 * @throws CopaEngineException
	 */
	Double getTotalInductor() throws CopaEngineException;

	Map<IClasificacion, Double> getMontosInducidos() throws CopaEngineException;

	Double getMontoInducido(IClasificacion clasificacion) throws CopaEngineException;

	/**
	 * Monto total inducido
	 * 
	 * @return
	 * @throws CopaEngineException
	 */
	Double getMontoInducidoTotal() throws CopaEngineException;

}
