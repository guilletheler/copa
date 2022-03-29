package com.gt.copa.infra.CalculatorNodes;

import com.gt.copa.model.atemporal.Actividad;

import javafx.scene.control.Label;

public class ActividadLabel extends Label {
    
    Actividad actividad; 

    public ActividadLabel(Actividad actividad) {
        super(actividad == null ? "" : actividad.getNombre());
    }
    
}
