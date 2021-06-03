/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gt.copa.model.SetDatos;
import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.atemporal.TipoClasificacionDato;
import com.gt.copa.model.temporal.Periodo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 *
 * @author guille
 */
@Data
public class CopaFiltroDatos implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    SetDatos setDatos;
	Escenario escenario;
    Empresa empresa;
    Periodo periodo;

    @Getter(value = AccessLevel.NONE)
    List<ClasificacionDato> filtroClasificaciones;

    /**
     * Tipo de clasificacion que se utiliza para el calculo
     */
    @Getter(value = AccessLevel.NONE)
    TipoClasificacionDato tipoClasificacion;

    @Getter(value = AccessLevel.NONE)
    List<String> filtroRecursos;
    
    @Getter(value = AccessLevel.NONE)
    List<String> filtroDatos;

    public List<ClasificacionDato> getFiltroClasificaciones() {
        if (filtroClasificaciones == null) {
            filtroClasificaciones = new ArrayList<>();
        }
        return filtroClasificaciones;
    }

    public List<String> getFiltroRecursos() {
        if (filtroRecursos == null) {
            filtroRecursos = new ArrayList<>();
        }
        return filtroRecursos;
    }

    public List<String> getFiltroDatos() {
        if (filtroDatos == null) {
            filtroDatos = new ArrayList<>();
        }
        return filtroDatos;
    }
}
