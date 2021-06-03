/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.ArrayList;
import java.util.List;

import com.gt.copa.calc.api.IActividad;
import com.gt.copa.calc.api.IActividadEnActividad;
import com.gt.copa.calc.api.IActividadEnOC;
import com.gt.copa.calc.api.IProceso;
import com.gt.copa.calc.api.IRecursoEnActividad;
import com.gt.copa.calc.api.TipoDistribucion;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author guille
 */
@Data
public class ActividadImpl implements IActividad {

	Integer codigo;

	String nombre;

	IProceso proceso;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(value = AccessLevel.NONE)
	List<IRecursoEnActividad> recursosInducidos;

	/**
	 * Otras actividades inducidas en esta
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(value = AccessLevel.NONE)
	List<IActividadEnActividad> actividadesInducidas;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(value = AccessLevel.NONE)
	List<IActividadEnOC> asignacionesOC;

	/**
	 * Esta actividad inducida en otras
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(value = AccessLevel.NONE)
	List<IActividadEnActividad> asignacionesActividades;

	TipoDistribucion tipoDistribucion;

	Boolean primaria = Boolean.TRUE;

	public ActividadImpl() {
		this.primaria = true;
	}

	public ActividadImpl(Integer codigo, String nombre, IProceso proceso, TipoDistribucion tipoDistribucion,
			Boolean primaria) {
		this();
		this.codigo = codigo;
		this.nombre = nombre;
		this.proceso = proceso;
		if (proceso != null) {
			proceso.getActividades().add(this);
		}
		this.tipoDistribucion = tipoDistribucion;
		this.primaria = primaria;
	}

	public List<IRecursoEnActividad> getRecursosInducidos() {
		if (recursosInducidos == null) {
			recursosInducidos = new ArrayList<>();
		}
		return recursosInducidos;
	}

	public List<IActividadEnOC> getAsignacionesOC() {
		if (asignacionesOC == null) {
			asignacionesOC = new ArrayList<>();
		}
		return asignacionesOC;
	}

	/**
	 * Las actividades que figuran en la lista están asignadas a esta actividad
	 * 
	 * @return
	 */
	public List<IActividadEnActividad> getActividadesInducidas() {
		if (actividadesInducidas == null) {
			actividadesInducidas = new ArrayList<>();
		}
		return actividadesInducidas;
	}

	/**
	 * lista de asignaciones de esta actividad en otra actividades
	 * 
	 * @return
	 */
	public List<IActividadEnActividad> getAsignacionesActividades() {
		if (asignacionesActividades == null) {
			asignacionesActividades = new ArrayList<>();
		}
		return asignacionesActividades;
	}

}
