/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.gt.copa.calc.api.IClasificacion;
import com.gt.copa.calc.api.IDato;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author guille
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatoImpl implements IDato {

    Integer codigo;

    String referencia;

    Date fecha;

    Double monto;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Getter(value = AccessLevel.NONE)
    Set<IClasificacion> clasificaciones;
    
    public DatoImpl(Integer codigo, String referencia, Date fecha, double monto) {
    	this.codigo = codigo;
    	this.referencia = referencia;
    	this.fecha = fecha;
    	this.monto = monto;
    }
    
    public DatoImpl(Integer codigo, String referencia, Date fecha, double monto, IClasificacion... clasificaciones) {
    	this(codigo, referencia, fecha, monto);
    	this.getClasificaciones().addAll(Arrays.asList(clasificaciones));
    }
    
    public DatoImpl(Integer codigo, String referencia, Date fecha, double monto, Collection<? extends IClasificacion> clasificaciones) {
    	this(codigo, referencia, fecha, monto);
    	this.getClasificaciones().addAll(clasificaciones);
    }
    

    @Override
    public String getNombre() {
        return referencia;
    }
    
    @Override
    public void setNombre(String nombre) {
        setReferencia(nombre);
    }


    public Set<IClasificacion> getClasificaciones() {
        if (clasificaciones == null) {
            clasificaciones = new HashSet<>();
        }
        return clasificaciones;
    }

}
