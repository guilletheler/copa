package com.gt.copa.controller.temporal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.gt.copa.components.ComponenteDriverConverter;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.DatoConverter;
import com.gt.copa.components.EscenarioConverter;
import com.gt.copa.components.TipoDistribucionConverter;
import com.gt.copa.controller.ModificadorDatos;
import com.gt.copa.infra.DatePickerTableCell;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.model.temporal.ValorDato;
import com.gt.copa.service.atemporal.ComponenteDriverService;
import com.gt.copa.service.atemporal.DatoService;
import com.gt.copa.service.atemporal.EscenarioService;
import com.gt.copa.service.temporal.ValorDatoService;

import org.apache.commons.collections4.IteratorUtils;
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
import lombok.Setter;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/temporal/ValorDatoCrudView.fxml")
public class ValorDatoCrudController implements ModificadorDatos {

    @Autowired
    ValorDatoService valorDatoService;

    @Autowired
    DatoService datoService;

    @Autowired
    DatoConverter datoConverter;

    @Autowired
    ComponenteDriverConverter componenteDriverConverter;

    @Autowired
    ComponenteDriverService componenteDriverService;

    @Autowired
    EscenarioConverter escenarioConverter;

    @Autowired
    DatoConverter datoConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Autowired
    EscenarioService escenarioService;

    @Getter
    @Setter
    FxWeaver fxWeaver;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private SearchableComboBox<Escenario> scmbFiltroEscenario;

    @FXML
    private SearchableComboBox<Dato> scmbFiltroDato;

    @FXML
    private SearchableComboBox<Periodo> scmbFiltroPeriodo;

    @FXML
    private TableView<ValorDato> tblValoresDatos;

    @FXML
    private TableColumn<ValorDato, Integer> colId;

    @FXML
    private TableColumn<ValorDato, Dato> colDato;

    @FXML
    private TableColumn<ValorDato, Date> colFecha;

    @FXML
    private TableColumn<ValorDato, Double> colValor;

    List<ValorDato> paraGuardar;
    List<ValorDato> paraEliminar;

    private List<ValorDato> rawItems;

    @Getter
    boolean dataModificada;

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
            dataModificada = true;
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

        colEscenario.setCellValueFactory(new PropertyValueFactory<ValorDato, Escenario>("escenario"));

        colEscenario.setOnEditCommit((CellEditEvent<ValorDato, Escenario> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setEscenario(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colDato.setCellValueFactory(
                new PropertyValueFactory<ValorDato, Dato>("dato"));

        colDato.setOnEditCommit((CellEditEvent<ValorDato, Dato> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setDato(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colFecha.setCellFactory(DatePickerTableCell.dateCellFactory("dd/MM/yyyy"));
        colFecha.setOnEditCommit((CellEditEvent<ValorDato, Date> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFecha(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colValor.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ValorDato, Double>, ObservableValue<Double>>() {

                    @Override
                    public ObservableValue<Double> call(TableColumn.CellDataFeatures<ValorDato, Double> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValor());
                    }
                });
        colValor.setCellFactory(EditingTextCell.doubleCellFactory());
        colValor.setOnEditCommit((CellEditEvent<ValorDato, Double> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setValor(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ValorDato valorDato) {
        if (!paraGuardar.contains(valorDato)) {
            paraGuardar.add(valorDato);
        }
        dataModificada = true;
    }

    public void loadData() {

        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        ObservableList<Escenario> escenarios = FXCollections.observableArrayList(
                IteratorUtils.toList(escenarioService.getRepo().findAll().iterator()));

        escenarios.add(0, null);

        loadScmbEscenarios(escenarios);

        Callback<TableColumn<ValorDato, Escenario>, TableCell<ValorDato, Escenario>> escenarioCellFactory = ComboBoxTableCell
                .forTableColumn(escenarioConverter, escenarios);

        colEscenario.setCellFactory(escenarioCellFactory);

        ObservableList<Dato> datos = FXCollections.observableArrayList(
                IteratorUtils.toList(datoService.getRepo().findAll().iterator()));

        datos.add(0, null);

        loadScmbDatos(datos);

        Callback<TableColumn<ValorDato, Dato>, TableCell<ValorDato, Dato>> datoCellFactory = ComboBoxTableCell
                .forTableColumn(datoConverter, datos);

        colDato.setCellFactory(datoCellFactory);

        rawItems = FXCollections.observableArrayList(valorDatoService.getValorDatoRepo()
                .findByEscenarioAndFechaGreaterThanEqualAndFechaLessThanEqual(
                        escenario, periodo.getInicio(), periodo.getFin()));

        showFiltredElements();
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();

        dataModificada = false;
    }

    private void showFiltredElements() {
        ObservableList<ValorDato> filtredItems;
        if (scmbFiltroEscenario.getSelectionModel().getSelectedItem() == null
                && scmbFiltroDato.getSelectionModel().getSelectedItem() == null
                && scmbFiltroPeriodo.getSelectionModel().getSelectedItem() == null) {
            filtredItems = FXCollections.observableArrayList(rawItems.stream().collect(Collectors.toList()));
        } else {
            filtredItems = FXCollections.observableArrayList(
                    rawItems.stream().filter(vd -> testInclude(vd)).collect(Collectors.toList()));
        }
        tblValoresDatos.setItems(filtredItems);
    }

    private boolean testInclude(ValorDato vd) {
        boolean ret = true;
        ret = ret && (scmbFiltroEscenario.getSelectionModel().getSelectedItem() == null
                || scmbFiltroEscenario.getSelectionModel().getSelectedItem().equals(vd.getEscenario()));

        ret = ret && (scmbFiltroDato.getSelectionModel().getSelectedItem() == null
                || scmbFiltroDato.getSelectionModel().getSelectedItem().equals(vd.getDato()));

        ret = ret && (scmbFiltroPeriodo.getSelectionModel().getSelectedItem() == null
                || scmbFiltroPeriodo.getSelectionModel().getSelectedItem().inPeriodo(vd.getFecha()));

        return ret;
    }

    private void loadScmbEscenarios(ObservableList<Escenario> escenarios) {

        scmbFiltroEscenario.setCellFactory(ComboBoxListCell.forListView(escenarioConverter, escenarios));

        scmbFiltroEscenario.setItems(escenarios);

        scmbFiltroEscenario.setConverter(escenarioConverter);

        scmbFiltroEscenario.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
        });
    }

    private void loadScmbDatos(ObservableList<Dato> datos) {

        scmbFiltroDato.setCellFactory(ComboBoxListCell.forListView(datoConverter, datos));

        scmbFiltroDato.setItems(datos);

        scmbFiltroDato.setConverter(datoConverter);

        scmbFiltroDato.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
        });
    }

    public void persist() {

        paraGuardar.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> valorDatoService.getValorDatoRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ValorDato dto) {
        // Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando valor de dato " + dto.toString());
        valorDatoService.getValorDatoRepo().save(dto);
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
