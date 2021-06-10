/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.periodico;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.calc.api.TratamientoMuestra;
import com.gt.copa.model.atemporal.Recurso;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author guille
 */
@Data
@EqualsAndHashCode
@Entity
@Table(name = "recursosperiodicos")
public class RecursoPeriodico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	Recurso recurso;

	/**
	 * Cantidad de períodos que se utilizan para calcular la suma o promedios
	 */
	Integer tamanioMuestra;

	@Enumerated(EnumType.STRING)
	TratamientoMuestra tratamientoMuestra;

	@Enumerated(EnumType.STRING)
	TipoDistribucion tipoDistribucion;

	Boolean promedioNoVacio;

	@Embedded
	ConfiguracionPeriodo configuracionPeriodo;
}
