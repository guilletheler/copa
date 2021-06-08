package com.gt.copa.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/ConfigPaneView.fxml")
public class ConfigPaneController {

    @FXML
    DatePicker dpFecha;

    @Getter
    @FXML
    VBox nodeView;

    @FXML
    public void initialize() {
        
    }

}
