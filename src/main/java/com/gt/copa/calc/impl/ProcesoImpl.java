/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.ArrayList;
import java.util.List;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IProceso;

import lombok.Data;

/**
 *
 * @author guille
 */
@Data
public class ProcesoImpl implements IProceso {

	Integer codigo;

	String nombre;

	List<IActividad> actividades;

	public ProcesoImpl() {
	}

	public ProcesoImpl(Integer codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public List<IActividad> getActividades() {
		if (actividades == null) {
			actividades = new ArrayList<>();
		}
		return actividades;
	}

}
