package com.gt.copa.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/MainView.fxml")
public class MainController {

	@Autowired
	ConfigPaneController configPaneController;

	@FXML
	private BorderPane mainView;

	private final FxWeaver fxWeaver;

	public MainController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	void initialize() {

	}

	@FXML
	void miPruebaClick(ActionEvent event) {
		openOtherWindows();
	}

	@FXML
	void configurar(ActionEvent event) {
		configPaneController.show();
	}

	public void openOtherWindows() {
		Stage stage = new Stage();
		Scene scene = new Scene(fxWeaver.loadView(PruebaController.class));
		stage.setScene(scene);
		stage.setTitle("Prueba");
		InputStream imgStream = MainController.class.getResourceAsStream("/com/gt/copa/view/copa.png");
		Image image = new Image(imgStream);
		stage.getIcons().add(image);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		stage.show();
	}
}
