/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.periodico;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.ComponenteDriver;
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
@Table(name = "actividadesenactividades")
public class ActividadEnActividad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @TableGenerator(table = "hibernate_sequences", name = "actividadenactividad_gen", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", allocationSize = 1, pkColumnValue = "actividadesenactividades")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "actividadenactividad_gen")
    private Integer id;

    @ManyToOne
    Actividad origen;
    
    @ManyToOne
    Actividad destino;
    
    @ManyToOne
    Periodo periodo;
    
    @ManyToOne
    Escenario escenario;
    
    @ManyToOne
    ComponenteDriver componenteDriver;
    
    Double valorParticular;
       
}
