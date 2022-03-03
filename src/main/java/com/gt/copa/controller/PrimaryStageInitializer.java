package com.gt.copa.controller;

import java.io.InputStream;

import com.gt.copa.StageReadyEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.rgielen.fxweaver.core.FxWeaver;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

	@Autowired
	Environment env;

	@Autowired
	MainViewController mainViewController;

	private final FxWeaver fxWeaver;

	@Autowired
	public PrimaryStageInitializer(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		Stage stage = event.stage;
		Parent root = fxWeaver.loadView(MainViewController.class);
		if (root == null) {
			throw new RuntimeException("No se puede leer el controlador " + MainViewController.class.getName());
		}
		Scene scene = new Scene(root);
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.setTitle("CoPA");
		InputStream imgStream = MainViewController.class.getResourceAsStream("/com/gt/copa/view/copa.png");
		Image image = new Image(imgStream);
		stage.getIcons().add(image);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				try {
					arg0.consume();
					if (PrimaryStageInitializer.this.mainViewController.confirmarCerrar()) {
						fxWeaver.shutdown();
					}
					// SpringbootJavaFxApplication.this.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		stage.show();
	}
}