package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.gt.copa.components.ActividadConverter;
import com.gt.copa.components.ComponenteDriverConverter;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.RecursoConverter;
import com.gt.copa.components.TipoDistribucionConverter;
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
import javafx.event.EventHandler;
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

    private List<RecursoEnActividad> rawItems;

    @FXML
    void btnNuevoClick(ActionEvent event) {

        RecursoEnActividad rxa = new RecursoEnActividad();

        rxa.setRecurso(scmbFiltroRecurso.getSelectionModel().getSelectedItem());
        rxa.setActividad(scmbFiltroActividad.getSelectionModel().getSelectedItem());
        rxa.setValorAsignado(new ValorAsignado());
        rxa.setConfiguracionPeriodo(ConfiguracionPeriodo.builder().periodo(currentStatus.getCopaStatus().getPeriodo())
                .escenario(currentStatus.getCopaStatus().getEscenario()).build());

        rawItems.add(rxa);
        tblAsignaciones.getItems().add(rxa);
        
    }

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblAsignaciones.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblAsignaciones.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblAsignaciones.getSelectionModel().getSelectedItems());
            rawItems.removeAll(tblAsignaciones.getSelectionModel().getSelectedItems());
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
                .observableArrayList(recursoRepo.findByEmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        recursos.add(0, null);

        ObservableList<Actividad> actividades = FXCollections
                .observableArrayList(actividadRepo.findByProceso_EmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        actividades.add(0, null);

        loadScmbRecursos(recursos);
        loadScmbActividades(actividades);

        ObservableList<ComponenteDriver> componentesDrivers = FXCollections
                .observableArrayList(componenteDriverService.findByEmpresa(empresa));
        componentesDrivers.add(0, null);

        Callback<TableColumn<RecursoEnActividad, ComponenteDriver>, TableCell<RecursoEnActividad, ComponenteDriver>> componenteDriverCellFactory = ComboBoxTableCell
                .forTableColumn(componenteDriverConverter, componentesDrivers);

        colComponenteDriver.setCellFactory(componenteDriverCellFactory);

        Callback<TableColumn<RecursoEnActividad, Recurso>, TableCell<RecursoEnActividad, Recurso>> recursoCellFactory = ComboBoxTableCell
        .forTableColumn(recursoConverter,recursos);

        // Callback<TableColumn<RecursoEnActividad, Recurso>, TableCell<RecursoEnActividad, Recurso>> recursoCellFactory = SearchableComboBoxTableCell
        // .searchableComboCellFactory(recursos, recursoConverter);

        colRecurso.setCellFactory(recursoCellFactory);

        Callback<TableColumn<RecursoEnActividad, Actividad>, TableCell<RecursoEnActividad, Actividad>> actividadCellFactory = ComboBoxTableCell
        .forTableColumn(actividadConverter,actividades);

        // Callback<TableColumn<RecursoEnActividad, Actividad>, TableCell<RecursoEnActividad, Actividad>> actividadCellFactory = SearchableComboBoxTableCell
        // .searchableComboCellFactory(actividades, actividadConverter);

        colActividad.setCellFactory(actividadCellFactory);

        rawItems = recursoEnActividadService.getRepo()
        .findByRecurso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(empresa,
                escenario, periodo);

        showFiltredElements();
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    private void showFiltredElements() {
        ObservableList<RecursoEnActividad> filtredItems;
        if(scmbFiltroRecurso.getSelectionModel().getSelectedItem() == null && scmbFiltroActividad.getSelectionModel().getSelectedItem() == null) {
            filtredItems = FXCollections.observableArrayList(rawItems.stream().collect(Collectors.toList()));
        } else {
            filtredItems = FXCollections.observableArrayList(
                rawItems.stream()
                .filter(rxa -> testInclude(rxa))
                .collect(Collectors.toList())
            );
        }
        tblAsignaciones.setItems(filtredItems);
    }

    private boolean testInclude(RecursoEnActividad rxa) {
        boolean ret = true;
        ret = ret && (scmbFiltroRecurso.getSelectionModel().getSelectedItem() == null || scmbFiltroRecurso.getSelectionModel().getSelectedItem().equals(rxa.getRecurso()));
        ret = ret && (scmbFiltroActividad.getSelectionModel().getSelectedItem() == null || scmbFiltroActividad.getSelectionModel().getSelectedItem().equals(rxa.getActividad()));
        return ret;
    }

    private void loadScmbRecursos(ObservableList<Recurso> recursos) {

        scmbFiltroRecurso.setCellFactory(ComboBoxListCell.forListView(recursoConverter, recursos));

        scmbFiltroRecurso.setItems(recursos);

        scmbFiltroRecurso.setConverter(recursoConverter);

        scmbFiltroRecurso.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
         });
    }

    private void loadScmbActividades(ObservableList<Actividad> actividades) {

        scmbFiltroActividad.setCellFactory(ComboBoxListCell.forListView(actividadConverter, actividades));

        scmbFiltroActividad.setItems(actividades);
        
        scmbFiltroActividad.setConverter(actividadConverter);

        scmbFiltroActividad.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
         });
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
