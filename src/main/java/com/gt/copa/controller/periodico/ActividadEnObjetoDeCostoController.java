package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gt.copa.components.ActividadConverter;
import com.gt.copa.components.ComponenteDriverConverter;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.ObjetoDeCostoConverter;
import com.gt.copa.components.TipoDistribucionConverter;
import com.gt.copa.controller.ModificadorDatos;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.atemporal.ObjetoDeCosto;
import com.gt.copa.model.periodico.ActividadEnObjetoDeCosto;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.periodico.ValorAsignado;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ActividadRepo;
import com.gt.copa.repo.atemporal.ObjetoDeCostoRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;
import com.gt.copa.service.periodico.ActividadEnObjetoDeCostoService;

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
@FxmlView("/com/gt/copa/view/periodico/ActividadEnObjetoDeCostoView.fxml")
public class ActividadEnObjetoDeCostoController implements ModificadorDatos {

    @Autowired
    ActividadEnObjetoDeCostoService actividadEnObjetoDeCostoService;

    @Autowired
    ActividadRepo actividadRepo;

    @Autowired
    ObjetoDeCostoRepo objetoDeCostoRepo;

    @Autowired
    TipoDistribucionConverter tipoDistribucionConverter;

    @Autowired
    ComponenteDriverConverter componenteDriverConverter;

    @Autowired
    ComponenteDriverService componenteDriverService;

    @Autowired
    ActividadConverter actividadConverter;

    @Autowired
    ObjetoDeCostoConverter objetoDeCostoConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private SearchableComboBox<Actividad> scmbFiltroActividad;

    @FXML
    private SearchableComboBox<ObjetoDeCosto> scmbFiltroObjetoDeCosto;

    @FXML
    private TableView<ActividadEnObjetoDeCosto> tblAsignaciones;

    @FXML
    private TableColumn<ActividadEnObjetoDeCosto, Integer> colId;

    @FXML
    private TableColumn<ActividadEnObjetoDeCosto, Actividad> colActividad;

    @FXML
    private TableColumn<ActividadEnObjetoDeCosto, ObjetoDeCosto> colObjetoDeCosto;

    @FXML
    private TableColumn<ActividadEnObjetoDeCosto, ComponenteDriver> colComponenteDriver;

    @FXML
    private TableColumn<ActividadEnObjetoDeCosto, Double> colValorParticular;

    List<ActividadEnObjetoDeCosto> paraGuardar;
    List<ActividadEnObjetoDeCosto> paraEliminar;

    private List<ActividadEnObjetoDeCosto> rawItems;

    @Getter
    boolean dataModificada;

    @FXML
    void btnNuevoClick(ActionEvent event) {

        ActividadEnObjetoDeCosto rxa = new ActividadEnObjetoDeCosto();

        rxa.setActividad(scmbFiltroActividad.getSelectionModel().getSelectedItem());
        rxa.setObjetoDeCosto(scmbFiltroObjetoDeCosto.getSelectionModel().getSelectedItem());
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
            dataModificada = true;
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ActividadEnObjetoDeCosto, Integer>("id"));

        colActividad.setCellValueFactory(new PropertyValueFactory<ActividadEnObjetoDeCosto, Actividad>("actividad"));

        colActividad.setOnEditCommit((CellEditEvent<ActividadEnObjetoDeCosto, Actividad> t) -> {
            ((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setActividad(t.getNewValue());
            modificado(((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colObjetoDeCosto.setCellValueFactory(
                new PropertyValueFactory<ActividadEnObjetoDeCosto, ObjetoDeCosto>("objetoDeCosto"));

        colObjetoDeCosto.setOnEditCommit((CellEditEvent<ActividadEnObjetoDeCosto, ObjetoDeCosto> t) -> {
            ((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setObjetoDeCosto(t.getNewValue());
            modificado(((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colComponenteDriver.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ActividadEnObjetoDeCosto, ComponenteDriver>, ObservableValue<ComponenteDriver>>() {

                    @Override
                    public ObservableValue<ComponenteDriver> call(
                            TableColumn.CellDataFeatures<ActividadEnObjetoDeCosto, ComponenteDriver> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getComponenteDriver());
                    }
                });

        colComponenteDriver.setOnEditCommit((CellEditEvent<ActividadEnObjetoDeCosto, ComponenteDriver> t) -> {
            ((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .getValorAsignado().setComponenteDriver(t.getNewValue());
            modificado(((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colValorParticular.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ActividadEnObjetoDeCosto, Double>, ObservableValue<Double>>() {

                    @Override
                    public ObservableValue<Double> call(
                            TableColumn.CellDataFeatures<ActividadEnObjetoDeCosto, Double> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getValorParticular());
                    }
                });
        colValorParticular.setCellFactory(EditingTextCell.doubleCellFactory());
        colValorParticular.setOnEditCommit((CellEditEvent<ActividadEnObjetoDeCosto, Double> t) -> {
            ((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .getValorAsignado().setValorParticular(t.getNewValue());
            modificado(((ActividadEnObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ActividadEnObjetoDeCosto actividad) {
        if (!paraGuardar.contains(actividad)) {
            paraGuardar.add(actividad);
        }
        dataModificada = true;
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();
        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        ObservableList<Actividad> actividades = FXCollections.observableArrayList(
                actividadRepo.findByProceso_EmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        actividades.add(0, null);

        ObservableList<ObjetoDeCosto> objetoDeCostos = FXCollections.observableArrayList(
                objetoDeCostoRepo.findByEmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        objetoDeCostos.add(0, null);

        loadScmbActividades(actividades);
        loadScmbObjetoDeCostos(objetoDeCostos);

        ObservableList<ComponenteDriver> componentesDrivers = FXCollections
                .observableArrayList(componenteDriverService.findByEmpresa(empresa));
        componentesDrivers.add(0, null);

        Callback<TableColumn<ActividadEnObjetoDeCosto, ComponenteDriver>, TableCell<ActividadEnObjetoDeCosto, ComponenteDriver>> componenteDriverCellFactory = ComboBoxTableCell
                .forTableColumn(componenteDriverConverter, componentesDrivers);

        colComponenteDriver.setCellFactory(componenteDriverCellFactory);

        Callback<TableColumn<ActividadEnObjetoDeCosto, Actividad>, TableCell<ActividadEnObjetoDeCosto, Actividad>> actividadCellFactory = ComboBoxTableCell
                .forTableColumn(actividadConverter, actividades);

        // Callback<TableColumn<ActividadEnObjetoDeCosto, Actividad>,
        // TableCell<ActividadEnObjetoDeCosto, Actividad>> actividadCellFactory =
        // SearchableComboBoxTableCell
        // .searchableComboCellFactory(actividades, actividadConverter);

        colActividad.setCellFactory(actividadCellFactory);

        Callback<TableColumn<ActividadEnObjetoDeCosto, ObjetoDeCosto>, TableCell<ActividadEnObjetoDeCosto, ObjetoDeCosto>> objetoDeCostoCellFactory = ComboBoxTableCell
                .forTableColumn(objetoDeCostoConverter, objetoDeCostos);

        // Callback<TableColumn<ActividadEnObjetoDeCosto, ObjetoDeCosto>,
        // TableCell<ActividadEnObjetoDeCosto, ObjetoDeCosto>> objetoDeCostoCellFactory
        // = SearchableComboBoxTableCell
        // .searchableComboCellFactory(objetoDeCostos, objetoDeCostoConverter);

        colObjetoDeCosto.setCellFactory(objetoDeCostoCellFactory);

        rawItems = actividadEnObjetoDeCostoService.getRepo()
                .findByActividad_Proceso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
                        empresa, escenario, periodo);

        showFiltredElements();
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();

        dataModificada = false;
    }

    private void showFiltredElements() {
        ObservableList<ActividadEnObjetoDeCosto> filtredItems;
        if (scmbFiltroActividad.getSelectionModel().getSelectedItem() == null
                && scmbFiltroObjetoDeCosto.getSelectionModel().getSelectedItem() == null) {
            filtredItems = FXCollections.observableArrayList(rawItems.stream().collect(Collectors.toList()));
        } else {
            filtredItems = FXCollections.observableArrayList(
                    rawItems.stream().filter(rxa -> testInclude(rxa)).collect(Collectors.toList()));
        }
        tblAsignaciones.setItems(filtredItems);
    }

    private boolean testInclude(ActividadEnObjetoDeCosto rxa) {
        boolean ret = true;
        ret = ret && (scmbFiltroActividad.getSelectionModel().getSelectedItem() == null
                || scmbFiltroActividad.getSelectionModel().getSelectedItem().equals(rxa.getActividad()));
        ret = ret && (scmbFiltroObjetoDeCosto.getSelectionModel().getSelectedItem() == null
                || scmbFiltroObjetoDeCosto.getSelectionModel().getSelectedItem().equals(rxa.getObjetoDeCosto()));
        return ret;
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

    private void loadScmbObjetoDeCostos(ObservableList<ObjetoDeCosto> objetoDeCostos) {

        scmbFiltroObjetoDeCosto.setCellFactory(ComboBoxListCell.forListView(objetoDeCostoConverter, objetoDeCostos));

        scmbFiltroObjetoDeCosto.setItems(objetoDeCostos);

        scmbFiltroObjetoDeCosto.setConverter(objetoDeCostoConverter);

        scmbFiltroObjetoDeCosto.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
        });
    }

    public void persist() {

        paraGuardar.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> actividadEnObjetoDeCostoService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ActividadEnObjetoDeCosto dto) {
        // Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando actividad " + dto.toString());
        actividadEnObjetoDeCostoService.getRepo().save(dto);
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
