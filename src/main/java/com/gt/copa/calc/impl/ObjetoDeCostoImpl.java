/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.ArrayList;
import java.util.List;

import com.gt.copa.calc.api.IActividadEnOC;
import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.IObjetoDeCosto;
import com.gt.copa.calc.api.IObjetoDeCostoEnArticulo;
import com.gt.copa.calc.api.TipoDistribucion;

import lombok.Data;

/**
 *
 * @author guille
 */
@Data
public class ObjetoDeCostoImpl implements IObjetoDeCosto {

	Integer codigo;

	String nombre;

	List<IActividadEnOC> actividadesInducidas;

	List<IObjetoDeCostoEnArticulo> asignacionesArticulos;

	Double monto = null;

	/**
	 * Se utiliza para calcular el valor unitario
	 */
	IComponenteDriver componenteDriver;

	Double valorParticular;

	/**
	 * Se utiliza para repartir el OC en el articulo
	 */
	TipoDistribucion tipoDistribucion;

	public ObjetoDeCostoImpl() {
	}

	public ObjetoDeCostoImpl(Integer codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public ObjetoDeCostoImpl(Integer codigo, String nombre, IComponenteDriver componenteDriver, Double valorParticular,
			TipoDistribucion tipoDistribucion) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.componenteDriver = componenteDriver;
		this.valorParticular = valorParticular;
		this.tipoDistribucion = tipoDistribucion;
	}

	public List<IActividadEnOC> getActividadesInducidas() {
		if (actividadesInducidas == null) {
			actividadesInducidas = new ArrayList<>();
		}
		return actividadesInducidas;
	}

	public List<IObjetoDeCostoEnArticulo> getAsignacionesArticulos() {
		if (asignacionesArticulos == null) {
			asignacionesArticulos = new ArrayList<>();
		}
		return asignacionesArticulos;
	}

}
