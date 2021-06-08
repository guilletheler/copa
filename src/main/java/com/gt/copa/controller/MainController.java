package com.gt.copa.controller;

import java.io.InputStream;

import com.gt.copa.CopaApplication;
import com.gt.copa.service.atemporal.EscenarioService;
import com.gt.copa.service.atemporal.TipoClasificacionDatoService;
import com.gt.copa.service.temporal.PeriodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/MainView.fxml")
public class MainController {

	@Autowired
	ConfigPaneController configPaneController;

	@Autowired
	EmpresaCrudController empresaCrudController;

	@Autowired
	EscenarioCrudController escenarioCrudController;

	@Autowired
	PeriodoCrudController periodoCrudController;

	@Autowired
	TipoClasificacionDatoCrudController tipoClasificacionDatoCrudController;

	@Autowired
	ClasificacionDatoCrudController clasificacionDatoCrudController;

	@Autowired
	SituacionActualController situacionActualController;

	@Autowired
	EscenarioService escenarioService;

	@Autowired
	PeriodoService periodoService;

	@Autowired
	TipoClasificacionDatoService tipoClasificacionDatoService;

	@FXML
	private BorderPane mainView;

	private final FxWeaver fxWeaver;

	public MainController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@FXML
	void initialize() {

		periodoService.checkYear();
		escenarioService.checkDefaults();
		tipoClasificacionDatoService.checkDefaults();

		FxControllerAndView<ConfigPaneController, VBox> configPane = fxWeaver.load(ConfigPaneController.class);
		configPane.getView().orElse(null);
		configPane.getController();

		FxControllerAndView<EmpresaCrudController, VBox> empresaPane = fxWeaver.load(EmpresaCrudController.class);
		empresaPane.getView().orElse(null);
		empresaPane.getController();

		FxControllerAndView<EscenarioCrudController, VBox> escenarioPane = fxWeaver.load(EscenarioCrudController.class);
		escenarioPane.getView().orElse(null);
		escenarioPane.getController();

		FxControllerAndView<PeriodoCrudController, VBox> periodoPane = fxWeaver.load(PeriodoCrudController.class);
		periodoPane.getView().orElse(null);
		periodoPane.getController();

		FxControllerAndView<TipoClasificacionDatoCrudController, VBox> tipoClasificacionPane = fxWeaver.load(TipoClasificacionDatoCrudController.class);
		tipoClasificacionPane.getView().orElse(null);
		tipoClasificacionPane.getController();

		FxControllerAndView<ClasificacionDatoCrudController, VBox> clasificacionPane = fxWeaver.load(ClasificacionDatoCrudController.class);
		clasificacionPane.getView().orElse(null);
		clasificacionPane.getController();

		FxControllerAndView<SituacionActualController, VBox> situacionActualPane = fxWeaver.load(SituacionActualController.class);
		situacionActualPane.getView().orElse(null);
		situacionActualPane.getController();

	}

	@FXML
	void mnuConfigurarClick(ActionEvent event) {

		this.mainView.setCenter(configPaneController.getNodeView());
	}

	@FXML
	void mnuEmpresasClick(ActionEvent event) {

		empresaCrudController.loadData();
		this.mainView.setCenter(empresaCrudController.getNodeView());
	}

	@FXML
	void mnuEscenariosClick(ActionEvent event) {

		escenarioCrudController.loadData();
		this.mainView.setCenter(escenarioCrudController.getNodeView());
	}

	@FXML
	void mnuPeriodosClick(ActionEvent event) {

		periodoCrudController.loadData();
		this.mainView.setCenter(periodoCrudController.getNodeView());
	}

	@FXML
	void mnuTipoClasificacionClick(ActionEvent event) {

		tipoClasificacionDatoCrudController.loadData();
		this.mainView.setCenter(tipoClasificacionDatoCrudController.getNodeView());
	}

	@FXML
	void mnuClasificacionClick(ActionEvent event) {

		clasificacionDatoCrudController.loadData();
		this.mainView.setCenter(clasificacionDatoCrudController.getNodeView());
	}

	@FXML
	void mnuSituacionActualClick(ActionEvent event) {

		situacionActualController.loadData();
		this.mainView.setCenter(situacionActualController.getNodeView());
	}

	@FXML
	void reiniciarClick(ActionEvent event) {

		restartApp();
	}

	public void restartApp() {
		CopaApplication.restart();
	}

	@FXML
	void miPruebaClick(ActionEvent event) {
		openOtherWindows();
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
