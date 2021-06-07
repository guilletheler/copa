package com.gt.copa.controller;

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
    VBox nodeView;

    @FXML
    public void initialize() {
        nodeView.setVisible(false);
    }

    public void show() {
        nodeView.setVisible(true);
    }

    public void hide() {
        nodeView.setVisible(false);
    }

    public void toggleView() {
        if(nodeView.isVisible()) {
            this.hide();
        } else {
            this.show();
        }
    }
}
