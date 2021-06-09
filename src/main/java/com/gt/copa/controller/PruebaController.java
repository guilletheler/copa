package com.gt.copa.controller;

import java.io.InputStream;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/PruebaView.fxml")
public class PruebaController {

    private Stage stage;

    @Getter
    @Setter
    Boolean algo = false;

    @FXML
    BorderPane dialog;

    @FXML
    void btnCerrarClick(ActionEvent event) {
        algo = true;
        stage.close();
    }
    
    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(dialog));

        stage.setTitle("Prueba");
		InputStream imgStream = getClass().getResourceAsStream("/com/gt/copa/view/copa.png");
		Image image = new Image(imgStream);
		stage.getIcons().add(image);
        stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
    }

    public boolean show() {
        algo = false;
        stage.showAndWait();
        return algo;
    }

}
