package com.gt.copa.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/CopaCalculatorView.fxml")
public class CopaCalculatorController implements ModificadorDatos {

    @Getter
    @FXML
    private ScrollPane nodeView;

    @FXML
    private Pane copaCalcPane;

    @Getter
    boolean dataModificada;

    private Line curLine;

    @FXML
    public void initialize() {

    }

    public void show() {
        nodeView.setVisible(true);
    }

    private void redraw() {
        // copaCalcPane.getChildren().clear();
        
        Circle circle = new Circle(100);
        circle.setLayoutX(100);
        circle.setLayoutY(100);
        circle.setFill(Color.BLUE);

        copaCalcPane.getChildren().add(circle);

        copaCalcPane.setOnMousePressed(event -> {
            if (!event.isPrimaryButtonDown()) {
                return;
            }

            curLine = new Line(
                event.getX(), event.getY(), 
                event.getX(), event.getY()
            );
            copaCalcPane.getChildren().add(curLine);
        });

        copaCalcPane.setOnMouseDragged(event -> {
            if (!event.isPrimaryButtonDown()) {
                return;
            }

            if (curLine == null) {
                return;
            }

            curLine.setEndX(event.getX());
            curLine.setEndY(event.getY());

            double mx = Math.max(curLine.getStartX(), curLine.getEndX());
            double my = Math.max(curLine.getStartY(), curLine.getEndY());

            if (mx > copaCalcPane.getMinWidth()) {
                copaCalcPane.setMinWidth(mx);
            }

            if (my > copaCalcPane.getMinHeight()) {
                copaCalcPane.setMinHeight(my);
            }
        });


    }

    public void hide() {
        nodeView.setVisible(false);
    }

    public void toggleView() {
        if (nodeView.isVisible()) {
            this.hide();
        } else {
            this.show();
        }
    }

    @Override
    public void persist() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadData() {
        // TODO Auto-generated method stub
        this.redraw();
    }
}
