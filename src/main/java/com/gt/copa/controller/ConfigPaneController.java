package com.gt.copa.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/ConfigPaneView.fxml")
public class ConfigPaneController {
    
    @FXML
    DatePicker dpFecha;
    
    @FXML
    VBox configView;

    @FXML
    public void initialize() {
        // dpFecha.setValue(LocalDate.now());
    }

    public void show() {
        if(configView.isVisible()) {
            configView.setVisible(false);

        } else {
        configView.setVisible(true);
        }
    }

    public void hide() {
        configView.setVisible(false);
    }
}
