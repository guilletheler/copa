package com.gt.copa.controller;

import com.gt.copa.CopaApplication;
import com.gt.copa.component.CurrentStatus;
import com.gt.copa.service.atemporal.EscenarioService;
import com.gt.copa.service.atemporal.TipoClasificacionDatoService;
import com.gt.copa.service.temporal.PeriodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
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
	RecursoCrudController recursoCrudController;

	@Autowired
	ProcesoCrudController procesoCrudController;

	@Autowired
	DatoCrudController datoCrudController;

	@Autowired
	EscenarioService escenarioService;

	@Autowired
	PeriodoService periodoService;

	@Autowired
	CurrentStatus currentStatus;

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
		fxWeaver.load(RecursoCrudController.class);
		fxWeaver.load(DatoCrudController.class);
		fxWeaver.load(ProcesoCrudController.class);
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
	void mnuRecursosClick(ActionEvent event) {

		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para administrar los recursos debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}

		recursoCrudController.loadData();
		this.mainView.setCenter(recursoCrudController.getNodeView());
	}

	@FXML
	void mnuDatosClick(ActionEvent event) {

		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para administrar los datos debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}

		datoCrudController.loadData();
		this.mainView.setCenter(datoCrudController.getNodeView());
	}

	@FXML
	void mnuProcesosClick(ActionEvent event) {

		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para administrar los procesos debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}

		procesoCrudController.loadData();
		this.mainView.setCenter(procesoCrudController.getNodeView());
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

		System.out.println(ConfirmDialogController.question(fxWeaver, "Dialogo de prueba\n\n¿Desea continuar?", true));	
		ConfirmDialogController.message(fxWeaver, "Informe", "Este es un mensaje");
	}

}
