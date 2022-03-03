package com.gt.copa.controller;

import com.gt.copa.CopaApplication;
import com.gt.copa.component.CurrentStatus;
import com.gt.copa.controller.atemporal.ActividadCrudController;
import com.gt.copa.controller.atemporal.ClasificacionDatoCrudController;
import com.gt.copa.controller.atemporal.ComponenteDriverCrudController;
import com.gt.copa.controller.atemporal.DatoCrudController;
import com.gt.copa.controller.atemporal.DriverCrudController;
import com.gt.copa.controller.atemporal.EmpresaCrudController;
import com.gt.copa.controller.atemporal.EscenarioCrudController;
import com.gt.copa.controller.atemporal.ObjetoDeCostoCrudController;
import com.gt.copa.controller.atemporal.ProcesoCrudController;
import com.gt.copa.controller.atemporal.RecursoCrudController;
import com.gt.copa.controller.atemporal.TipoClasificacionDatoCrudController;
import com.gt.copa.controller.periodico.ActividadEnActividadController;
import com.gt.copa.controller.periodico.ActividadEnObjetoDeCostoController;
import com.gt.copa.controller.periodico.ActividadPeriodicaConfigController;
import com.gt.copa.controller.periodico.ComponenteDriverPeriodicoController;
import com.gt.copa.controller.periodico.ObjetoDeCostoPeriodicoConfigController;
import com.gt.copa.controller.periodico.RecursoEnActividadController;
import com.gt.copa.controller.periodico.RecursoPeriodicoConfigController;
import com.gt.copa.controller.temporal.PeriodoCrudController;
import com.gt.copa.controller.temporal.ValorDatoCrudController;
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
	ActividadCrudController actividadCrudController;

	@Autowired
	ObjetoDeCostoCrudController objetoDeCostoCrudController;

	@Autowired
	DriverCrudController driverCrudController;

	@Autowired
	ComponenteDriverCrudController componenteDriverCrudController;

	@Autowired
	ActividadPeriodicaConfigController actividadPeriodicaConfigController;

	@Autowired
	RecursoPeriodicoConfigController recursoPeriodicoConfigController;

	@Autowired
	ObjetoDeCostoPeriodicoConfigController objetoDeCostoPeriodicoConfigController;

	@Autowired
	RecursoEnActividadController recursoEnActividadController;

	@Autowired
	ActividadEnActividadController actividadEnActividadController;

	@Autowired
	ActividadEnObjetoDeCostoController actividadEnObjetoDeCostoController;

	@Autowired
	ComponenteDriverPeriodicoController componenteDriverPeriodicoController;

	@Autowired
	ValorDatoCrudController valorDatoCrudController;

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
		fxWeaver.load(ActividadCrudController.class);
		fxWeaver.load(ObjetoDeCostoCrudController.class);
		fxWeaver.load(DriverCrudController.class);
		fxWeaver.load(ComponenteDriverCrudController.class);
		fxWeaver.load(ActividadPeriodicaConfigController.class);
		fxWeaver.load(RecursoPeriodicoConfigController.class);
		fxWeaver.load(ObjetoDeCostoPeriodicoConfigController.class);
		fxWeaver.load(RecursoEnActividadController.class);
		fxWeaver.load(ActividadEnActividadController.class);
		fxWeaver.load(ActividadEnObjetoDeCostoController.class);
		fxWeaver.load(ComponenteDriverPeriodicoController.class);
		fxWeaver.load(ValorDatoCrudController.class);
		valorDatoCrudController.setFxWeaver(fxWeaver);
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
	void mnuActividadesClick(ActionEvent event) {
		
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para administrar las actividades debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		
		actividadCrudController.loadData();
		this.mainView.setCenter(actividadCrudController.getNodeView());
	}
	
	@FXML
	void mnuObjetosDeCostoClick(ActionEvent event) {
		
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para administrar los objetos de costo debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}

		objetoDeCostoCrudController.loadData();
		this.mainView.setCenter(objetoDeCostoCrudController.getNodeView());
	}
	
	@FXML
	void mnuDriversClick(ActionEvent event) {
		
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para administrar los drivers debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
	
		driverCrudController.loadData();
		this.mainView.setCenter(driverCrudController.getNodeView());
	}
	
	@FXML
	void mnuComponentesDriverClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para administrar los componentes de driver debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
	
		componenteDriverCrudController.loadData();
		this.mainView.setCenter(componenteDriverCrudController.getNodeView());
	}
	
	@FXML
	void mnuConfigActividadPeriodicaClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar las actividades debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar las actividades debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar las actividades debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		actividadPeriodicaConfigController.loadData();
		this.mainView.setCenter(actividadPeriodicaConfigController.getNodeView());
	}
	
	@FXML
	void mnuConfigRecursoPeriodicoClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar los recursos debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar los recursos debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar los recursos debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		recursoPeriodicoConfigController.loadData();
		this.mainView.setCenter(recursoPeriodicoConfigController.getNodeView());
	}
	
	@FXML
	void mnuConfigObjetoDeCostoPeriodicoClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar los recursos debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar los recursos debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para configurar los recursos debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		objetoDeCostoPeriodicoConfigController.loadData();
		this.mainView.setCenter(objetoDeCostoPeriodicoConfigController.getNodeView());
	}
	
	@FXML
	void mnuRecursoEnActividadClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar recursos debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar recursos debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar recursos debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		recursoEnActividadController.loadData();
		this.mainView.setCenter(recursoEnActividadController.getNodeView());
	}
	
	@FXML
	void mnuActividadEnActividadClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar actividades debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar actividades debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar actividades debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		actividadEnActividadController.loadData();
		this.mainView.setCenter(actividadEnActividadController.getNodeView());
	}
	
	@FXML
	void mnuActividadEnObjetoDeCostoClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar actividades debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar actividades debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar actividades debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		actividadEnObjetoDeCostoController.loadData();
		this.mainView.setCenter(actividadEnObjetoDeCostoController.getNodeView());
	}
	
	@FXML
	void mnuComponenteDriverPeriodicoClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar valores de componentes de driver debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar valores de componentes de driver debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para asignar valores de componentes de driver debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		componenteDriverPeriodicoController.loadData();
		this.mainView.setCenter(componenteDriverPeriodicoController.getNodeView());
	}

	@FXML
	void mnuValorDatoClick(ActionEvent event) {
		if(currentStatus.getCopaStatus().getEmpresa() == null) {
			ConfirmDialogController.message(fxWeaver, "Para cargar valores de datos debe\nseleccionar una empresa en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getEscenario() == null) {
			ConfirmDialogController.message(fxWeaver, "Para cargar valores de datos debe\nseleccionar un escenario en\nSituacion Actual");
			return;
		}
		if(currentStatus.getCopaStatus().getPeriodo() == null) {
			ConfirmDialogController.message(fxWeaver, "Para cargar valores de datos debe\nseleccionar un período en\nSituacion Actual");
			return;
		}
	
		valorDatoCrudController.loadData();
		this.mainView.setCenter(valorDatoCrudController.getNodeView());
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
