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
    Integer retVal = null;

    @FXML
    BorderPane confirmDialog;

    @Getter
    @FXML
    Button leftButton;

    @Getter
    @FXML
    Button centreButton;

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
        retVal = 0;
        stage.close();
    }

    @FXML
    void btnCancelClick(ActionEvent event) {
        retVal = 1;
        stage.close();
    }

    @FXML
    void btnSiClick(ActionEvent event) {
        retVal = 2;
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

    public int show() {
        retVal = null;
        stage.showAndWait();
        return retVal;
    }

    public static Integer question(FxWeaver fxWeaver, String question) {
        return question(fxWeaver, null, question, false, 0);
    }

    public static Integer question(FxWeaver fxWeaver, String title, String question) {
        return question(fxWeaver, title, question, false, 0);
    }

    public static Integer question(FxWeaver fxWeaver, String question, boolean showCancelButton,
            int buttonFocus) {
        return question(fxWeaver, null, question, showCancelButton, buttonFocus);
    }

    public static Integer question(FxWeaver fxWeaver, String title, String question, boolean showCancelButton) {
        return question(fxWeaver, null, question, showCancelButton, 0);
    }

    public static Integer question(FxWeaver fxWeaver, String title, String question, boolean showCancelButton,
            int buttonFocus) {

        FxControllerAndView<ConfirmDialogController, BorderPane> tiledDialog = fxWeaver
                .load(ConfirmDialogController.class);

        if (title != null) {
            tiledDialog.getController().getLblTitle().setText(title);
        }
        tiledDialog.getController().getLblMessage().setText(question);

        tiledDialog.getController().getCentreButton().setVisible(showCancelButton);

        switch (buttonFocus) {
            case 0:
                tiledDialog.getController().getLeftButton().requestFocus();
            case 1:
                if (tiledDialog.getController().getCentreButton().isVisible()) {
                    tiledDialog.getController().getCentreButton().requestFocus();
                }
            case 2:
                tiledDialog.getController().getRightButton().requestFocus();
            default:
        }

        return (tiledDialog.getController().show());
    }

    public static void message(FxWeaver fxWeaver, String message) {
        message(fxWeaver, null, message);
    }

    public static void message(FxWeaver fxWeaver, String title, String message) {

        FxControllerAndView<ConfirmDialogController, BorderPane> tiledDialog = fxWeaver
                .load(ConfirmDialogController.class);

        if (title != null) {
            tiledDialog.getController().getLblTitle().setText(title);
        }
        tiledDialog.getController().getLblMessage().setText(message);
        tiledDialog.getController().getRightButton().setText("Cerrar");
        tiledDialog.getController().getRightButton().requestFocus();
        tiledDialog.getController().getLeftButton().setVisible(false);

        tiledDialog.getController().show();
    }
}
