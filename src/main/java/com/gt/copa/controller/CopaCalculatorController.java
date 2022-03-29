package com.gt.copa.controller;

import java.util.List;

import com.gt.copa.components.CurrentStatus;
import com.gt.copa.infra.CalculatorNodes.RecursoLabel;
import com.gt.copa.model.periodico.RecursoPeriodico;
import com.gt.copa.service.periodico.RecursoEnActividadService;
import com.gt.copa.service.periodico.RecursoPeriodicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
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

    @Autowired
    RecursoEnActividadService recursoEnActividadService;

    @Autowired
    RecursoPeriodicoService recursoPeriodicoService;

    @Autowired
    CurrentStatus currentStatus;

    @FXML
    public void initialize() {

    }

    public void show() {
        nodeView.setVisible(true);
    }

    private void redraw() {
        copaCalcPane.getChildren().clear();

        // List<RecursoEnActividad> rxa = recursoEnActividadService.getRepo()
        // .findByRecurso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
        // currentStatus.getCopaStatus().getEmpresa(),
        // currentStatus.getCopaStatus().getEscenario(),
        // currentStatus.getCopaStatus().getPeriodo());

        List<RecursoPeriodico> recursos = recursoPeriodicoService.getRepo()
                .findByRecurso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
                        currentStatus.getCopaStatus().getEmpresa(), currentStatus.getCopaStatus().getEscenario(),
                        currentStatus.getCopaStatus().getPeriodo());

        int posX = 0;
        int posY = 0;

        for (RecursoPeriodico recurso : recursos) {
            RecursoLabel rl = new RecursoLabel(copaCalcPane, recurso.getRecurso(), (int) (300));
            rl.setLayoutX(posX);
            rl.setLayoutY(posY);
            rl.setMinHeight(25);
            
            posY += rl.getMinHeight() + 1;
            copaCalcPane.getChildren().add(rl);
        }

        copaCalcPane.setOnMousePressed(event -> {
            if (!event.isPrimaryButtonDown()) {
                return;
            }

            curLine = new Line(
                    event.getX(), event.getY(),
                    event.getX(), event.getY());
            copaCalcPane.getChildren().add(curLine);
        });

        copaCalcPane.setOnMouseReleased(event -> {
            if(curLine != null) {
                copaCalcPane.getChildren().remove(curLine);
                curLine = null;
            }
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
