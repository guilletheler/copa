package com.gt.copa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.gt.copa.CopaApplication;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.config.CopaConfig;
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
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/MainView.fxml")
public class MainViewController {

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
	CopaCalculatorController copaCalculatorController;

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

	@Autowired
	CopaConfig copaConfig;

	@FXML
	private BorderPane mainView;

	@Getter
	private final FxWeaver fxWeaver;

	public MainViewController(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	List<FxControllerAndView<? extends ModificadorDatos, Node>> viewControllers = new ArrayList<>();

	@FXML
	void initialize() {

		periodoService.checkYear();
		escenarioService.checkDefaults();
		tipoClasificacionDatoService.checkDefaults();

		fxWeaver.load(ConfigPaneController.class);
		viewControllers.add(fxWeaver.load(EmpresaCrudController.class));
		viewControllers.add(fxWeaver.load(EscenarioCrudController.class));
		viewControllers.add(fxWeaver.load(PeriodoCrudController.class));
		viewControllers.add(fxWeaver.load(TipoClasificacionDatoCrudController.class));
		viewControllers.add(fxWeaver.load(ClasificacionDatoCrudController.class));
		viewControllers.add(fxWeaver.load(SituacionActualController.class));
		viewControllers.add(fxWeaver.load(RecursoCrudController.class));
		viewControllers.add(fxWeaver.load(DatoCrudController.class));
		viewControllers.add(fxWeaver.load(ProcesoCrudController.class));
		viewControllers.add(fxWeaver.load(ActividadCrudController.class));
		viewControllers.add(fxWeaver.load(ObjetoDeCostoCrudController.class));
		viewControllers.add(fxWeaver.load(DriverCrudController.class));
		viewControllers.add(fxWeaver.load(ComponenteDriverCrudController.class));
		viewControllers.add(fxWeaver.load(ActividadPeriodicaConfigController.class));
		viewControllers.add(fxWeaver.load(RecursoPeriodicoConfigController.class));
		viewControllers.add(fxWeaver.load(ObjetoDeCostoPeriodicoConfigController.class));
		viewControllers.add(fxWeaver.load(RecursoEnActividadController.class));
		viewControllers.add(fxWeaver.load(ActividadEnActividadController.class));
		viewControllers.add(fxWeaver.load(ActividadEnObjetoDeCostoController.class));
		viewControllers.add(fxWeaver.load(ComponenteDriverPeriodicoController.class));
		viewControllers.add(fxWeaver.load(ValorDatoCrudController.class));
		viewControllers.add(fxWeaver.load(CopaCalculatorController.class));
	}

	@FXML
	void mnuConfigurarClick(ActionEvent event) {

		this.mainView.setCenter(configPaneController.getNodeView());
	}

	@FXML
	void mnuEmpresasClick(ActionEvent event) {

		if (confirmarGuardarDatos()) {
			empresaCrudController.loadData();
			this.mainView.setCenter(empresaCrudController.getNodeView());
		}
	}

	@FXML
	void mnuEscenariosClick(ActionEvent event) {

		if (confirmarGuardarDatos()) {
			escenarioCrudController.loadData();
			this.mainView.setCenter(escenarioCrudController.getNodeView());
		}
	}

	@FXML
	void mnuPeriodosClick(ActionEvent event) {

		if (confirmarGuardarDatos()) {
			periodoCrudController.loadData();
			this.mainView.setCenter(periodoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuTipoClasificacionClick(ActionEvent event) {

		if (confirmarGuardarDatos()) {
			tipoClasificacionDatoCrudController.loadData();
			this.mainView.setCenter(tipoClasificacionDatoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuClasificacionClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			clasificacionDatoCrudController.loadData();
			this.mainView.setCenter(clasificacionDatoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuSituacionActualClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			situacionActualController.loadData();
			this.mainView.setCenter(situacionActualController.getNodeView());
		}
	}

	@FXML
	void mnuRecursosClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para administrar los recursos debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}

			recursoCrudController.loadData();
			this.mainView.setCenter(recursoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuDatosClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para administrar los datos debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}

			datoCrudController.loadData();
			this.mainView.setCenter(datoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuProcesosClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para administrar los procesos debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}

			procesoCrudController.loadData();
			this.mainView.setCenter(procesoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuActividadesClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para administrar las actividades debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}

			actividadCrudController.loadData();
			this.mainView.setCenter(actividadCrudController.getNodeView());
		}
	}

	@FXML
	void mnuObjetosDeCostoClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para administrar los objetos de costo debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}

			objetoDeCostoCrudController.loadData();
			this.mainView.setCenter(objetoDeCostoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuDriversClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para administrar los drivers debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}

			driverCrudController.loadData();
			this.mainView.setCenter(driverCrudController.getNodeView());
		}
	}

	@FXML
	void mnuComponentesDriverClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para administrar los componentes de driver debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}

			componenteDriverCrudController.loadData();
			this.mainView.setCenter(componenteDriverCrudController.getNodeView());
		}
	}

	@FXML
	void mnuConfigActividadPeriodicaClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar las actividades debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar las actividades debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar las actividades debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			actividadPeriodicaConfigController.loadData();
			this.mainView.setCenter(actividadPeriodicaConfigController.getNodeView());
		}
	}

	@FXML
	void mnuConfigRecursoPeriodicoClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar los recursos debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar los recursos debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar los recursos debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			recursoPeriodicoConfigController.loadData();
			this.mainView.setCenter(recursoPeriodicoConfigController.getNodeView());
		}
	}

	@FXML
	void mnuConfigObjetoDeCostoPeriodicoClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar los recursos debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar los recursos debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para configurar los recursos debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			objetoDeCostoPeriodicoConfigController.loadData();
			this.mainView.setCenter(objetoDeCostoPeriodicoConfigController.getNodeView());
		}
	}

	@FXML
	void mnuRecursoEnActividadClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar recursos debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar recursos debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar recursos debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			recursoEnActividadController.loadData();
			this.mainView.setCenter(recursoEnActividadController.getNodeView());
		}
	}

	@FXML
	void mnuActividadEnActividadClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar actividades debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar actividades debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar actividades debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			actividadEnActividadController.loadData();
			this.mainView.setCenter(actividadEnActividadController.getNodeView());
		}
	}

	@FXML
	void mnuActividadEnObjetoDeCostoClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar actividades debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar actividades debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar actividades debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			actividadEnObjetoDeCostoController.loadData();
			this.mainView.setCenter(actividadEnObjetoDeCostoController.getNodeView());
		}
	}

	@FXML
	void mnuComponenteDriverPeriodicoClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar valores de componentes de driver debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar valores de componentes de driver debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar valores de componentes de driver debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			componenteDriverPeriodicoController.loadData();
			this.mainView.setCenter(componenteDriverPeriodicoController.getNodeView());
		}
	}

	@FXML
	void mnuValorDatoClick(ActionEvent event) {

		if (confirmarGuardarDatos()) {
			if (currentStatus.getCopaStatus().getEmpresa() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar valores de datos debe\nseleccionar una empresa en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getEscenario() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar valores de datos debe\nseleccionar un escenario en\nSituacion Actual");
				return;
			}
			if (currentStatus.getCopaStatus().getPeriodo() == null) {
				ConfirmDialogController.message(fxWeaver,
						"Para asignar valores de datos debe\nseleccionar un período en\nSituacion Actual");
				return;
			}

			valorDatoCrudController.loadData();
			this.mainView.setCenter(valorDatoCrudController.getNodeView());
		}
	}

	@FXML
	void mnuCalculoClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			copaCalculatorController.loadData();
			this.mainView.setCenter(copaCalculatorController.getNodeView());
		}
	}

	@FXML
	void reiniciarClick(ActionEvent event) {
		if (confirmarGuardarDatos()) {
			restartApp();
		}
	}

	public void restartApp() {
		CopaApplication.restart();
	}

	@FXML
	void miPruebaClick(ActionEvent event) {
		openOtherWindows();
	}

	public boolean confirmarCerrar() {
		if (confirmarGuardarDatos()) {
			return ConfirmDialogController.question(fxWeaver, "Cerrar", "¿Desea salir de CoPA?") == 2;
		}
		return false;
	}

	public boolean confirmarGuardarDatos() {

		boolean continuar = true;

		ModificadorDatos currentModificadorDatos = this.getCurrentModificadorDatos();

		if (currentModificadorDatos != null && currentModificadorDatos.isDataModificada()) {

			if (copaConfig.getAutosave() != null && copaConfig.getAutosave()) {
				currentModificadorDatos.persist();
			} else {

				switch (ConfirmDialogController.question(fxWeaver, "Atención",
						"Los datos no están guardados,\n ¿Desea guardar los datos?", true, 2)) {
					case 0:
						// NO
						continuar = true;
						break;
					case 1:
						// Cancelar
						continuar = false;
						break;
					case 2:
						// SI
						currentModificadorDatos.persist();
						continuar = true;
						break;
				}
			}
		}

		return continuar;
	}

	public void openOtherWindows() {

		System.out.println(
				ConfirmDialogController.question(fxWeaver, "Dialogo de prueba\n\n¿Desea continuar?", false, 2) == 2);
		ConfirmDialogController.message(fxWeaver, "Informe", "Este es un mensaje");
	}

	public ModificadorDatos getCurrentModificadorDatos() {

		ModificadorDatos md = null;

		if (this.mainView != null && this.mainView.getCenter() != null) {
			md = viewControllers.stream()
					.filter(vc -> Objects.equals(vc.getView().orElse(null), this.mainView.getCenter()))
					.map(vc -> vc.getController())
					.findFirst().orElse(null);
		}

		return md;
	}
}
