/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.api;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author guillermot
 */
public enum TratamientoMuestra {
    PROMEDIO(1, "Promedio"),
    SUMA(2, "Suma");
    
	@Getter
	@Setter
    String descripcion;
	
	@Getter
	@Setter
    Integer id;

    private TratamientoMuestra(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }
    
    /**
     * busca el tipo de distribución según una id
     * @param id
     * @return 
     */
    public static TratamientoMuestra getById(int id) {
        for (TratamientoMuestra td : TratamientoMuestra.values()) {
            if (td.getId() == id) {
                return td;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return descripcion;
    }
        
}
