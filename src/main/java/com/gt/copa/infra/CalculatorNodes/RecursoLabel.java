package com.gt.copa.infra.CalculatorNodes;

import com.gt.copa.model.atemporal.Recurso;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class RecursoLabel extends Label {

    Pane pane;

    Recurso recurso;

    public RecursoLabel(Pane pane, Recurso recurso, int width) {
        super(recurso == null ? "" : recurso.getNombre());
        this.pane = pane;

        this.setOnMouseEntered(event -> {
            this.setStyle("-fx-font-size: 14; -fx-border-color: black; -fx-border-width: 1px; -fx-font-style: bold;");
        });
        
        this.setOnMouseExited(event -> {
            this.setStyle("-fx-font-size: 12; -fx-border-width: 0px; -fx-font-style: normal;");
        });
        
        this.setOnDragDetected(arg0 -> {
            this.setStyle("-fx-font-size: 14; -fx-border-color: blue; -fx-border-width: 1px; -fx-font-style: bold;");
        });
        
        // this.setOnDragDone(arg0 -> {
        //     this.setStyle("-fx-font-size: 12; -fx-border-width: 0px; -fx-font-style: normal;");
        // });
        
        this.setWidth(width);
        this.setMinWidth(width);
        this.setMaxWidth(width);
        
        this.setStyle("-fx-background-color: white; -fx-font-size: 12; -fx-font-style: normal;");
    }

}
