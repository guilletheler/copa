/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.periodico;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.CostoEstandar;

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
@EqualsAndHashCode
@Entity
@Table(name = "costosestandarperiodicos")
public class CostoEstandarPeriodico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	CostoEstandar costoEstandar;

	Double valor;

	@Enumerated(EnumType.STRING)
	TipoDistribucion tipoDistribucion;

	@Getter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany
	@JoinTable(name = "clasificaciones_ceper", joinColumns = {
			@JoinColumn(name = "ceper_ceperid") }, inverseJoinColumns = {
					@JoinColumn(name = "clasificaciones_clasificacionid") })
	private Set<ClasificacionDato> clasificaciones;

	public Set<ClasificacionDato> getClasificaciones() {
		if (clasificaciones == null) {
			clasificaciones = new HashSet<>();
		}
		return clasificaciones;
	}

	@Embedded
	ConfiguracionPeriodo configuracionPeriodo;
}
