/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.model.periodico;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.ObjetoDeCosto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author guille
 */
@Data
@EqualsAndHashCode
@Entity
@Table(name = "actividadesenobjetosdecostos")
public class ActividadEnObjetoDeCosto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    ObjetoDeCosto objetoDeCosto;
    
    @ManyToOne
    Actividad actividad;
    
    @Embedded
    ValorAsignado valorAsignado;

    @Embedded
    ConfiguracionPeriodo configuracionPeriodo;
}
