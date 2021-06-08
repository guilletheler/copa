/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.temporal;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;

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
@Entity
@Table(name = "valoresdatos")
public class ValorDato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Dato dato;

	@Temporal(javax.persistence.TemporalType.DATE)
	private Date fecha;
	
	@ManyToOne
	Empresa empresa;

	@ManyToOne
	private Escenario escenario;

	@Getter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany
	@JoinTable(name = "clasificaciones_valoresdatos", joinColumns = {
			@JoinColumn(name = "valoresdatos_valordatoid") }, inverseJoinColumns = {
					@JoinColumn(name = "clasificaciones_clasificacionid") })
	private Set<ClasificacionDato> clasificaciones;

	Double valor;

	public Set<ClasificacionDato> getClasificaciones() {
		if (clasificaciones == null) {
			clasificaciones = new HashSet<>();
		}
		return clasificaciones;
	}

}
