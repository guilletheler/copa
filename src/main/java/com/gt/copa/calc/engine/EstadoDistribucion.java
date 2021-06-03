/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

/**
 *
 * @author guille
 */
public enum EstadoDistribucion {
    /**
     * Distribución por porcentaje
     */
    INCOMPLETA(1, "Incompleta"),
    /**
     * Distribución por unidad
     */
    COMPLETA(2, "Completa"),
    /**
     * Distribución por driver
     */
    SOBREINDUCIDA(3, "Sobre Inducida");
    
    int id;
    String descripcion;

    /**
     * constructor
     *
     * @param id
     * @param desc
     */
    private EstadoDistribucion(int id, String desc) {
        this.id = id;
        this.descripcion = desc;
    }

    /**
     * id del tipo de distribución
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * id del tipo de distribución
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * descripción del tipo de distribución
     *
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * descripción del tipo de distribución
     *
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * busca el tipo de distribución según una id
     *
     * @param id
     * @return
     */
    public static EstadoDistribucion getById(int id) {
        for (EstadoDistribucion td : EstadoDistribucion.values()) {
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
