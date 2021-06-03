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
import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.Escenario;
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
@Table(name = "actividadesperiodicas")
public class ActividadPeriodica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @TableGenerator(table = "hibernate_sequences", name = "actividadperiodica_gen", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", allocationSize = 1, pkColumnValue = "actividadesperiodicas")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "actividadperiodica_gen")
    private Integer id;

    @ManyToOne
    Actividad actividad;

    @ManyToOne
    Periodo periodo;

    @Enumerated(EnumType.STRING)
    TipoDistribucion tipoDistribucion;

    @ManyToOne
    Escenario escenario;

}
