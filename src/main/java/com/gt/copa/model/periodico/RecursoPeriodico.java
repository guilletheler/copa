/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.periodico;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.calc.api.TratamientoMuestra;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.atemporal.Recurso;
import com.gt.copa.model.temporal.Periodo;

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
	@TableGenerator(table = "hibernate_sequences", name = "recursoperiodico_gen", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value", allocationSize = 1, pkColumnValue = "recursosperiodicos")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "recursoperiodico_gen")
	private Integer id;

	@ManyToOne
	Recurso recurso;

	@ManyToOne
	Periodo periodo;

	/**
	 * Cantidad de períodos que se utilizan para calcular la suma o promedios
	 */
	Integer tamanioMuestra;

	@Enumerated(EnumType.STRING)
	TratamientoMuestra tratamientoMuestra;

	@Enumerated(EnumType.STRING)
	TipoDistribucion tipoDistribucion;

	Boolean promedioNoVacio;

	@ManyToOne
	Escenario escenario;

}
