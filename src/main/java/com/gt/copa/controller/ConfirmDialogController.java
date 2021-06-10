package com.gt.copa.controller;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/ConfirmDialogView.fxml")
public class ConfirmDialogController {

    private Stage stage;

    @Getter
    @Setter
    Boolean algo = false;
    
    @FXML
    BorderPane confirmDialog;
    
    @Getter
    @FXML
    Button leftButton;

    @Getter
    @FXML
    Button rightButton;
    
    @Getter
    @FXML
    Label lblTitle;
    
    @Getter
    @FXML
    Label lblMessage;

    @FXML
    void btnNoClick(ActionEvent event) {
        algo = false;
        stage.close();
    }

    @FXML
    void btnSiClick(ActionEvent event) {
        algo = true;
        stage.close();
    }
    
    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(confirmDialog));

        stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
    }

    public boolean show() {
        algo = false;
        stage.showAndWait();
        return algo;
    }

    public static boolean question(FxWeaver fxWeaver, String question) {
        return question(fxWeaver, null, question, false);
    }

    public static boolean question(FxWeaver fxWeaver, String title, String question) {
        return question(fxWeaver, title, question, false);
    }
    
    public static boolean question(FxWeaver fxWeaver, String question, boolean rightFocus) {
        return question(fxWeaver, null, question, rightFocus);
    }

    public static boolean question(FxWeaver fxWeaver, String title, String question, boolean rightFocus) {

        FxControllerAndView<ConfirmDialogController, BorderPane> tiledDialog = fxWeaver.load(ConfirmDialogController.class);

        if(title != null) {
		    tiledDialog.getController().getLblTitle().setText(title);
        }
		tiledDialog.getController().getLblMessage().setText(question);
        if(rightFocus) {
            tiledDialog.getController().getRightButton().requestFocus();
        }

		return(tiledDialog.getController().show());	
    }

    public static void message(FxWeaver fxWeaver, String message) {
        message(fxWeaver, null, message);
    }

    public static void message(FxWeaver fxWeaver, String title, String message) {

        FxControllerAndView<ConfirmDialogController, BorderPane> tiledDialog = fxWeaver.load(ConfirmDialogController.class);

        if(title != null) {
		    tiledDialog.getController().getLblTitle().setText(title);
        }
		tiledDialog.getController().getLblMessage().setText(message);
        tiledDialog.getController().getRightButton().setText("Cerrar");
        tiledDialog.getController().getRightButton().requestFocus();
        tiledDialog.getController().getLeftButton().setVisible(false);

		tiledDialog.getController().show();	
    }
}
