/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.periodico;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.TableGenerator;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.CostoEstandar;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.temporal.Periodo;

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
	@TableGenerator(table = "hibernate_sequences", name = "coestoestandarperiodico_gen", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value", allocationSize = 1, pkColumnValue = "costosestandarperiodicos")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "coestoestandarperiodico_gen")
	private Integer id;

	@ManyToOne
	CostoEstandar costoEstandar;

	@ManyToOne
	Periodo periodo;
	
	@ManyToOne
	Empresa empresa;

	@ManyToOne
	Escenario escenario;

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

}
