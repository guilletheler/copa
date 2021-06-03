/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.api;

import lombok.Getter;
import lombok.Setter;

/**
 * tipo de distribución aplicable a los recursos y actividades. <br/>
 * Los valores pueden ser:
 * <li>{@link #VALOR}</li>
 * <li>{@link #UNIFORME}</li>
 * <li>{@link #DRIVER}</li>
 * <li>{@link #PORCENTAJE}</li>
 * 
 * @author guillermot
 */
public enum TipoDistribucion {

	/**
	 * Distribución por porcentaje
	 */
	VALOR(1, "Valor"),
	/**
	 * Distribución por unidad
	 */
	UNIFORME(2, "Uniforme"),
	/**
	 * Distribución por driver
	 */
	DRIVER(3, "Driver"),
	/**
	 * Distribución por porcentaje
	 */
	PORCENTAJE(4, "Porcentaje");

	@Getter
	@Setter
	int id;

	@Getter
	@Setter
	String descripcion;

	/**
	 * constructor
	 *
	 * @param id
	 * @param desc
	 */
	private TipoDistribucion(int id, String desc) {
		this.id = id;
		this.descripcion = desc;
	}

	/**
	 * busca el tipo de distribución según una id
	 *
	 * @param id
	 * @return
	 */
	public static TipoDistribucion getById(int id) {
		for (TipoDistribucion td : TipoDistribucion.values()) {
			if (td.getId() == id) {
				return td;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return descripcion;
	}

}
