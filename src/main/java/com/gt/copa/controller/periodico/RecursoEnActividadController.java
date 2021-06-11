package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gt.copa.component.ActividadConverter;
import com.gt.copa.component.ComponenteDriverConverter;
import com.gt.copa.component.CurrentStatus;
import com.gt.copa.component.RecursoConverter;
import com.gt.copa.component.TipoDistribucionConverter;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.atemporal.Recurso;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.periodico.RecursoEnActividad;
import com.gt.copa.model.periodico.ValorAsignado;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ActividadRepo;
import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;
import com.gt.copa.service.periodico.RecursoEnActividadService;

import org.controlsfx.control.SearchableComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/periodico/RecursoEnActividadView.fxml")
public class RecursoEnActividadController {

    @Autowired
    RecursoEnActividadService recursoEnActividadService;

    @Autowired
    RecursoRepo recursoRepo;

    @Autowired
    ActividadRepo actividadRepo;

    @Autowired
    TipoDistribucionConverter tipoDistribucionConverter;

    @Autowired
    ComponenteDriverConverter componenteDriverConverter;

    @Autowired
    ComponenteDriverService componenteDriverService;

    @Autowired
    RecursoConverter recursoConverter;

    @Autowired
    ActividadConverter actividadConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private SearchableComboBox<Recurso> scmbFiltroRecurso;

    @FXML
    private SearchableComboBox<Actividad> scmbFiltroActividad;

    @FXML
    private TableView<RecursoEnActividad> tblAsignaciones;

    @FXML
    private TableColumn<RecursoEnActividad, Integer> colId;

    @FXML
    private TableColumn<RecursoEnActividad, Recurso> colRecurso;

    @FXML
    private TableColumn<RecursoEnActividad, Actividad> colActividad;

    @FXML
    private TableColumn<RecursoEnActividad, ComponenteDriver> colComponenteDriver;

    @FXML
    private TableColumn<RecursoEnActividad, Double> colValorParticular;

    List<RecursoEnActividad> paraGuardar;
    List<RecursoEnActividad> paraEliminar;

    @FXML
    void btnNuevoClick(ActionEvent event) {

        RecursoEnActividad rxa = new RecursoEnActividad();

        rxa.setRecurso(scmbFiltroRecurso.getSelectionModel().getSelectedItem());
        rxa.setActividad(scmbFiltroActividad.getSelectionModel().getSelectedItem());
        rxa.setValorAsignado(new ValorAsignado());
        rxa.setConfiguracionPeriodo(ConfiguracionPeriodo.builder().periodo(currentStatus.getCopaStatus().getPeriodo())
                .escenario(currentStatus.getCopaStatus().getEscenario()).build());

        tblAsignaciones.getItems().add(rxa);
    }

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblAsignaciones.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblAsignaciones.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblAsignaciones.getSelectionModel().getSelectedItems());
            tblAsignaciones.getItems().removeAll(tblAsignaciones.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<RecursoEnActividad, Integer>("id"));

        colRecurso.setCellValueFactory(new PropertyValueFactory<RecursoEnActividad, Recurso>("recurso"));

        colRecurso.setOnEditCommit((CellEditEvent<RecursoEnActividad, Recurso> t) -> {
            ((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setRecurso(t.getNewValue());
            modificado(((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colActividad.setCellValueFactory(new PropertyValueFactory<RecursoEnActividad, Actividad>("actividad"));

        colActividad.setOnEditCommit((CellEditEvent<RecursoEnActividad, Actividad> t) -> {
            ((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setActividad(t.getNewValue());
            modificado(((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colComponenteDriver.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<RecursoEnActividad, ComponenteDriver>, ObservableValue<ComponenteDriver>>() {

                    @Override
                    public ObservableValue<ComponenteDriver> call(
                            TableColumn.CellDataFeatures<RecursoEnActividad, ComponenteDriver> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getComponenteDriver());
                    }
                });

        colComponenteDriver.setOnEditCommit((CellEditEvent<RecursoEnActividad, ComponenteDriver> t) -> {
            ((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())).getValorAsignado()
                    .setComponenteDriver(t.getNewValue());
            modificado(((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colValorParticular.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<RecursoEnActividad, Double>, ObservableValue<Double>>() {

                    @Override
                    public ObservableValue<Double> call(
                            TableColumn.CellDataFeatures<RecursoEnActividad, Double> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getValorParticular());
                    }
                });
        colValorParticular.setCellFactory(EditingTextCell.doubleCellFactory());
        colValorParticular.setOnEditCommit((CellEditEvent<RecursoEnActividad, Double> t) -> {
            ((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())).getValorAsignado()
                    .setValorParticular(t.getNewValue());
            modificado(((RecursoEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(RecursoEnActividad recurso) {
        if (!paraGuardar.contains(recurso)) {
            paraGuardar.add(recurso);
        }
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();
        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        ObservableList<Recurso> recursos = FXCollections
                .observableArrayList(recursoRepo.findByEmpresa(currentStatus.getCopaStatus().getEmpresa()));

        recursos.add(0, null);

        ObservableList<Actividad> actividades = FXCollections
                .observableArrayList(actividadRepo.findByProceso_Empresa(currentStatus.getCopaStatus().getEmpresa()));

        actividades.add(0, null);

        loadScmbRecursos(recursos);
        loadScmbActividades(actividades);

        ObservableList<ComponenteDriver> componentesDrivers = FXCollections
                .observableArrayList(componenteDriverService.findByEmpresa(empresa));
        componentesDrivers.add(0, null);

        Callback<TableColumn<RecursoEnActividad, ComponenteDriver>, TableCell<RecursoEnActividad, ComponenteDriver>> componenteDriverCellFactory = ComboBoxTableCell
                .forTableColumn(componenteDriverConverter, componentesDrivers);

        colComponenteDriver.setCellFactory(componenteDriverCellFactory);

        tblAsignaciones.setItems(FXCollections.observableArrayList(recursoEnActividadService.getRepo()
                .findByRecurso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(empresa,
                        escenario, periodo)));
        paraGuardar = new ArrayList<>();
    }

    private void loadScmbRecursos(ObservableList<Recurso> recursos) {

        scmbFiltroRecurso.setCellFactory(ComboBoxListCell.forListView(recursoConverter, recursos));

        scmbFiltroRecurso.setItems(recursos);
    }

    private void loadScmbActividades(ObservableList<Actividad> actividades) {

        scmbFiltroActividad.setCellFactory(ComboBoxListCell.forListView(actividadConverter, actividades));

        scmbFiltroActividad.setItems(actividades);
    }

    public void persist() {

        paraGuardar.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> recursoEnActividadService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(RecursoEnActividad dto) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando recurso " + dto.toString());
        recursoEnActividadService.getRepo().save(dto);
    }

    public void show() {
        nodeView.setVisible(true);
    }

    public void hide() {
        nodeView.setVisible(false);
    }

    public void toggleView() {
        if (nodeView.isVisible()) {
            this.hide();
        } else {
            this.show();
        }
    }
}
