/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.temporal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.gt.copa.model.CodigoNombre;
import com.gt.copa.model.atemporal.Empresa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author guille
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "periodos")
public class Periodo extends CodigoNombre {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(javax.persistence.TemporalType.DATE)
	private Date inicio;

	@Temporal(javax.persistence.TemporalType.DATE)
	private Date fin;

	@Enumerated(EnumType.STRING)
	TipoPeriodo tipoPeriodo;

	@ManyToOne
	Empresa empresa;

}
