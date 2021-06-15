/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.temporal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Escenario;

import lombok.Data;

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
	private Escenario escenario;

	Double valor;
}
