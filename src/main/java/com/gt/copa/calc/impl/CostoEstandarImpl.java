/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.ICostoEstandar;
import com.gt.copa.calc.api.ICostoEstandarEnArticulo;
import com.gt.copa.calc.api.TipoDistribucion;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author guillermo
 */
@Data
public class CostoEstandarImpl implements ICostoEstandar {

	Integer codigo;

	String nombre;

	Map<IClasificacion, Double> preciosUnitarios;

	TipoDistribucion tipoDistribucion;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(value = AccessLevel.NONE)
	List<ICostoEstandarEnArticulo> asignacionesArticulos;

	public CostoEstandarImpl() {
	}

	public CostoEstandarImpl(Integer codigo, String nombre, Map<IClasificacion, Double> preciosUnitarios,
			TipoDistribucion tipoDistribucion) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.preciosUnitarios = preciosUnitarios;
		this.tipoDistribucion = tipoDistribucion;
	}

	public List<ICostoEstandarEnArticulo> getAsignacionesArticulos() {
		if (asignacionesArticulos == null) {
			asignacionesArticulos = new ArrayList<>();
		}
		return asignacionesArticulos;
	}

}
