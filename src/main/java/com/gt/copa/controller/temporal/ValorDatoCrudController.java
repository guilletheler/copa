package com.gt.copa.controller.temporal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.gt.copa.component.ActividadConverter;
import com.gt.copa.component.ComponenteDriverConverter;
import com.gt.copa.component.CurrentStatus;
import com.gt.copa.component.ObjetoDeCostoConverter;
import com.gt.copa.component.TipoDistribucionConverter;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.atemporal.ObjetoDeCosto;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.periodico.ValorAsignado;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.model.temporal.ValorDato;
import com.gt.copa.repo.atemporal.ActividadRepo;
import com.gt.copa.repo.atemporal.ObjetoDeCostoRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;

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
@FxmlView("/com/gt/copa/view/temporal/ValorDatoCrudView.fxml")
public class ValorDatoCrudController {

    @Autowired
    ValorDatoService valorDatoService;

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
    private SearchableComboBox<Dato> scmbFiltroDato;

    @FXML
    private TableView<ValorDato> tblValoresDatos;

    @FXML
    private TableColumn<ValorDato, Integer> colId;

    @FXML
    private TableColumn<ValorDato, Escenario> colEscenario;

    @FXML
    private TableColumn<ValorDato, Dato> colDato;

    @FXML
    private TableColumn<ValorDato, Date> colFecha;

    @FXML
    private TableColumn<ValorDato, Double> colValor;


    List<ValorDato> paraGuardar;
    List<ValorDato> paraEliminar;

    private ObservableList<ValorDato> rawItems;

    @FXML
    void btnNuevoClick(ActionEvent event) {

        ValorDato valorDato = new ValorDato();

        valorDato.setDato(scmbFiltroDato.getSelectionModel().getSelectedItem());
        valorDato.setEscenario(currentStatus.getCopaStatus().getEscenario());
        valorDato.setFecha(currentStatus.getCopaStatus().getPeriodo().getFin());
        valorDato.setValor(null);

        rawItems.add(valorDato);
        tblValoresDatos.getItems().add(valorDato);

    }

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblValoresDatos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblValoresDatos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblValoresDatos.getSelectionModel().getSelectedItems());
            rawItems.removeAll(tblValoresDatos.getSelectionModel().getSelectedItems());
            tblValoresDatos.getItems().removeAll(tblValoresDatos.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnImportarClick(ActionEvent event) {

    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ValorDato, Integer>("id"));

        colActividad.setCellValueFactory(new PropertyValueFactory<ValorDato, Actividad>("actividad"));

        colActividad.setOnEditCommit((CellEditEvent<ValorDato, Actividad> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setActividad(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colObjetoDeCosto.setCellValueFactory(
                new PropertyValueFactory<ValorDato, ObjetoDeCosto>("objetoDeCosto"));

        colObjetoDeCosto.setOnEditCommit((CellEditEvent<ValorDato, ObjetoDeCosto> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setObjetoDeCosto(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colComponenteDriver.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ValorDato, ComponenteDriver>, ObservableValue<ComponenteDriver>>() {

                    @Override
                    public ObservableValue<ComponenteDriver> call(
                            TableColumn.CellDataFeatures<ValorDato, ComponenteDriver> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getComponenteDriver());
                    }
                });

        colComponenteDriver.setOnEditCommit((CellEditEvent<ValorDato, ComponenteDriver> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .getValorAsignado().setComponenteDriver(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colValorParticular.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ValorDato, Double>, ObservableValue<Double>>() {

                    @Override
                    public ObservableValue<Double> call(
                            TableColumn.CellDataFeatures<ValorDato, Double> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getValorParticular());
                    }
                });
        colValorParticular.setCellFactory(EditingTextCell.doubleCellFactory());
        colValorParticular.setOnEditCommit((CellEditEvent<ValorDato, Double> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .getValorAsignado().setValorParticular(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ValorDato actividad) {
        if (!paraGuardar.contains(actividad)) {
            paraGuardar.add(actividad);
        }
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

        Callback<TableColumn<ValorDato, ComponenteDriver>, TableCell<ValorDato, ComponenteDriver>> componenteDriverCellFactory = ComboBoxTableCell
                .forTableColumn(componenteDriverConverter, componentesDrivers);

        colComponenteDriver.setCellFactory(componenteDriverCellFactory);

        Callback<TableColumn<ValorDato, Actividad>, TableCell<ValorDato, Actividad>> actividadCellFactory = ComboBoxTableCell
                .forTableColumn(actividadConverter, actividades);

        // Callback<TableColumn<ValorDato, Actividad>,
        // TableCell<ValorDato, Actividad>> actividadCellFactory =
        // SearchableComboBoxTableCell
        // .searchableComboCellFactory(actividades, actividadConverter);

        colActividad.setCellFactory(actividadCellFactory);

        Callback<TableColumn<ValorDato, ObjetoDeCosto>, TableCell<ValorDato, ObjetoDeCosto>> objetoDeCostoCellFactory = ComboBoxTableCell
                .forTableColumn(objetoDeCostoConverter, objetoDeCostos);

        // Callback<TableColumn<ValorDato, ObjetoDeCosto>,
        // TableCell<ValorDato, ObjetoDeCosto>> objetoDeCostoCellFactory
        // = SearchableComboBoxTableCell
        // .searchableComboCellFactory(objetoDeCostos, objetoDeCostoConverter);

        colObjetoDeCosto.setCellFactory(objetoDeCostoCellFactory);

        rawItems = FXCollections.observableArrayList(ValorDatoService.getRepo()
                .findByActividad_Proceso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
                        empresa, escenario, periodo));

        showFiltredElements();
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    private void showFiltredElements() {
        ObservableList<ValorDato> filtredItems;
        if (scmbFiltroActividad.getSelectionModel().getSelectedItem() == null
                && scmbFiltroObjetoDeCosto.getSelectionModel().getSelectedItem() == null) {
            filtredItems = rawItems;
        } else {
            filtredItems = FXCollections.observableArrayList(
                    rawItems.stream().filter(rxa -> testInclude(rxa)).collect(Collectors.toList()));
        }
        tblValoresDatos.setItems(filtredItems);
    }

    private boolean testInclude(ValorDato rxa) {
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
                .forEach(id -> ValorDatoService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ValorDato dto) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando actividad " + dto.toString());
        ValorDatoService.getRepo().save(dto);
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
