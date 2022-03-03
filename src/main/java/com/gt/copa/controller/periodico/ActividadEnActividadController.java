package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import com.gt.copa.model.periodico.ActividadEnActividad;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.periodico.ValorAsignado;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ActividadRepo;
import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;
import com.gt.copa.service.periodico.ActividadEnActividadService;

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
@FxmlView("/com/gt/copa/view/periodico/ActividadEnActividadView.fxml")
public class ActividadEnActividadController {

    @Autowired
    ActividadEnActividadService actividadEnActividadService;

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
    private SearchableComboBox<Actividad> scmbFiltroOrigen;

    @FXML
    private SearchableComboBox<Actividad> scmbFiltroDestino;

    @FXML
    private TableView<ActividadEnActividad> tblAsignaciones;

    @FXML
    private TableColumn<ActividadEnActividad, Integer> colId;

    @FXML
    private TableColumn<ActividadEnActividad, Actividad> colOrigen;

    @FXML
    private TableColumn<ActividadEnActividad, Actividad> colDestino;

    @FXML
    private TableColumn<ActividadEnActividad, ComponenteDriver> colComponenteDriver;

    @FXML
    private TableColumn<ActividadEnActividad, Double> colValorParticular;

    List<ActividadEnActividad> paraGuardar;
    List<ActividadEnActividad> paraEliminar;

    private List<ActividadEnActividad> rawItems;

    @FXML
    void btnNuevoClick(ActionEvent event) {

        ActividadEnActividad axa = new ActividadEnActividad();

        axa.setOrigen(scmbFiltroOrigen.getSelectionModel().getSelectedItem());
        axa.setDestino(scmbFiltroDestino.getSelectionModel().getSelectedItem());
        axa.setValorAsignado(new ValorAsignado());
        axa.setConfiguracionPeriodo(ConfiguracionPeriodo.builder().periodo(currentStatus.getCopaStatus().getPeriodo())
                .escenario(currentStatus.getCopaStatus().getEscenario()).build());

        rawItems.add(axa);
        tblAsignaciones.getItems().add(axa);

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

        colId.setCellValueFactory(new PropertyValueFactory<ActividadEnActividad, Integer>("id"));

        colOrigen.setCellValueFactory(new PropertyValueFactory<ActividadEnActividad, Actividad>("origen"));

        colOrigen.setOnEditCommit((CellEditEvent<ActividadEnActividad, Actividad> t) -> {
            ((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setOrigen(t.getNewValue());
            modificado(((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colDestino.setCellValueFactory(new PropertyValueFactory<ActividadEnActividad, Actividad>("destino"));

        colDestino.setOnEditCommit((CellEditEvent<ActividadEnActividad, Actividad> t) -> {
            ((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setDestino(t.getNewValue());
            modificado(((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colComponenteDriver.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ActividadEnActividad, ComponenteDriver>, ObservableValue<ComponenteDriver>>() {

                    @Override
                    public ObservableValue<ComponenteDriver> call(
                            TableColumn.CellDataFeatures<ActividadEnActividad, ComponenteDriver> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getComponenteDriver());
                    }
                });

        colComponenteDriver.setOnEditCommit((CellEditEvent<ActividadEnActividad, ComponenteDriver> t) -> {
            ((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())).getValorAsignado()
                    .setComponenteDriver(t.getNewValue());
            modificado(((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colValorParticular.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ActividadEnActividad, Double>, ObservableValue<Double>>() {

                    @Override
                    public ObservableValue<Double> call(
                            TableColumn.CellDataFeatures<ActividadEnActividad, Double> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getValorParticular());
                    }
                });
        colValorParticular.setCellFactory(EditingTextCell.doubleCellFactory());
        colValorParticular.setOnEditCommit((CellEditEvent<ActividadEnActividad, Double> t) -> {
            ((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())).getValorAsignado()
                    .setValorParticular(t.getNewValue());
            modificado(((ActividadEnActividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ActividadEnActividad recurso) {
        if (!paraGuardar.contains(recurso)) {
            paraGuardar.add(recurso);
        }
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();
        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        ObservableList<Actividad> actividades = FXCollections.observableArrayList(
                actividadRepo.findByProceso_EmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        ObservableList<Actividad> secundarias = FXCollections.observableArrayList(actividadRepo
                .findByProceso_EmpresaAndPrimariaFalseOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        actividades.add(0, null);

        loadScmbOrigen(secundarias);
        loadScmbDestino(actividades);

        ObservableList<ComponenteDriver> componentesDrivers = FXCollections
                .observableArrayList(componenteDriverService.findByEmpresa(empresa));
        componentesDrivers.add(0, null);

        Callback<TableColumn<ActividadEnActividad, ComponenteDriver>, TableCell<ActividadEnActividad, ComponenteDriver>> componenteDriverCellFactory = ComboBoxTableCell
                .forTableColumn(componenteDriverConverter, componentesDrivers);

        colComponenteDriver.setCellFactory(componenteDriverCellFactory);

        Callback<TableColumn<ActividadEnActividad, Actividad>, TableCell<ActividadEnActividad, Actividad>> origenCellFactory = ComboBoxTableCell
                .forTableColumn(actividadConverter, secundarias);

        // Callback<TableColumn<ActividadEnActividad, Recurso>,
        // TableCell<ActividadEnActividad, Recurso>> recursoCellFactory =
        // SearchableComboBoxTableCell
        // .searchableComboCellFactory(recursos, recursoConverter);

        colOrigen.setCellFactory(origenCellFactory);

        Callback<TableColumn<ActividadEnActividad, Actividad>, TableCell<ActividadEnActividad, Actividad>> destinoCellFactory = ComboBoxTableCell
                .forTableColumn(actividadConverter, actividades);

        // Callback<TableColumn<ActividadEnActividad, Actividad>,
        // TableCell<ActividadEnActividad, Actividad>> actividadCellFactory =
        // SearchableComboBoxTableCell
        // .searchableComboCellFactory(actividades, actividadConverter);

        colDestino.setCellFactory(destinoCellFactory);

        rawItems = FXCollections.observableArrayList(actividadEnActividadService.getRepo()
                .findByOrigen_Proceso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(empresa,
                        escenario, periodo));

        showFiltredElements();
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    private void showFiltredElements() {
        ObservableList<ActividadEnActividad> filtredItems;
        if (scmbFiltroOrigen.getSelectionModel().getSelectedItem() == null
                && scmbFiltroDestino.getSelectionModel().getSelectedItem() == null) {
            filtredItems = FXCollections.observableArrayList(rawItems);
        } else {
            filtredItems = FXCollections.observableArrayList(
                    rawItems.stream().filter(rxa -> testInclude(rxa)).collect(Collectors.toList()));
        }
        tblAsignaciones.setItems(filtredItems);
    }

    private boolean testInclude(ActividadEnActividad rxa) {
        boolean ret = true;
        ret = ret && (scmbFiltroOrigen.getSelectionModel().getSelectedItem() == null
                || scmbFiltroOrigen.getSelectionModel().getSelectedItem().equals(rxa.getOrigen()));
        ret = ret && (scmbFiltroDestino.getSelectionModel().getSelectedItem() == null
                || scmbFiltroDestino.getSelectionModel().getSelectedItem().equals(rxa.getDestino()));
        return ret;
    }

    private void loadScmbOrigen(ObservableList<Actividad> actividades) {

        scmbFiltroOrigen.setCellFactory(ComboBoxListCell.forListView(actividadConverter, actividades));

        scmbFiltroOrigen.setItems(actividades);

        scmbFiltroOrigen.setConverter(actividadConverter);

        scmbFiltroOrigen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
        });
    }

    private void loadScmbDestino(ObservableList<Actividad> actividades) {

        scmbFiltroDestino.setCellFactory(ComboBoxListCell.forListView(actividadConverter, actividades));

        scmbFiltroDestino.setItems(actividades);

        scmbFiltroDestino.setConverter(actividadConverter);

        scmbFiltroDestino.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
        });
    }

    public void persist() {

        paraGuardar.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> actividadEnActividadService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ActividadEnActividad dto) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando recurso " + dto.toString());
        actividadEnActividadService.getRepo().save(dto);
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
