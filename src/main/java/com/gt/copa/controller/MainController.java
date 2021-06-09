package com.gt.copa.controller;

import com.gt.copa.CopaApplication;
import com.gt.copa.service.atemporal.EscenarioService;
import com.gt.copa.service.atemporal.TipoClasificacionDatoService;
import com.gt.copa.service.temporal.PeriodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
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

		fxWeaver.load(ConfigPaneController.class);
		fxWeaver.load(EmpresaCrudController.class);
		fxWeaver.load(EscenarioCrudController.class);
		fxWeaver.load(PeriodoCrudController.class);
		fxWeaver.load(TipoClasificacionDatoCrudController.class);
		fxWeaver.load(ClasificacionDatoCrudController.class);
		fxWeaver.load(SituacionActualController.class);
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
		FxControllerAndView<PruebaController, BorderPane> tiledDialog = fxWeaver.load(PruebaController.class);

		System.out.println(tiledDialog.getController().show());	
	}

}
