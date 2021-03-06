package com.gt.copa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIUtils {
	public static final String ICON_IMAGE_LOC = "/com/gt/copa/view/copa.png";
	
	public static void setStageIcon(Stage stage) {
		stage.getIcons().add(new Image(MainViewController.class.getResourceAsStream(ICON_IMAGE_LOC)));
	}

	public static Object loadWindow(URL loc, String title, Stage parentStage) {
		Object controller = null;
		try {
			FXMLLoader loader = new FXMLLoader(loc);
			Parent parent = loader.load();
			controller = loader.getController();
			Stage stage = null;
			if (parentStage != null) {
				stage = parentStage;
			} else {
				stage = new Stage(StageStyle.DECORATED);
			}
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
			setStageIcon(stage);
		} catch (IOException ex) {
			Logger.getLogger(UIUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
		return controller;
	}
}
