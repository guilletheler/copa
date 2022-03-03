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
import com.gt.copa.component.DatoConverter;
import com.gt.copa.component.ObjetoDeCostoConverter;
import com.gt.copa.controller.ConfirmDialogController;
import com.gt.copa.infra.DatePickerTableCell;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.model.temporal.ValorDato;
import com.gt.copa.repo.atemporal.DatoRepo;
import com.gt.copa.repo.atemporal.ObjetoDeCostoRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;
import com.gt.copa.service.temporal.ValorDatoService;

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
public class ValorDatoCrudController {

    @Autowired
    ValorDatoService valorDatoService;

    @Autowired
    DatoRepo datoRepo;

    @Autowired
    ObjetoDeCostoRepo objetoDeCostoRepo;

    @Autowired
    DatoConverter datoConverter;

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
    @Setter
    FxWeaver fxWeaver;

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
    private TableColumn<ValorDato, Dato> colDato;

    @FXML
    private TableColumn<ValorDato, Date> colFecha;

    @FXML
    private TableColumn<ValorDato, Double> colValor;

    List<ValorDato> paraGuardar;
    List<ValorDato> paraEliminar;

    private List<ValorDato> rawItems;

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

        colDato.setCellValueFactory(new PropertyValueFactory<ValorDato, Dato>("dato"));

        colDato.setOnEditCommit((CellEditEvent<ValorDato, Dato> t) -> {
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDato(t.getNewValue());
            modificado(((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colFecha.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ValorDato, Date>, ObservableValue<Date>>() {

                    @Override
                    public ObservableValue<Date> call(TableColumn.CellDataFeatures<ValorDato, Date> param) {

                        return new SimpleObjectProperty<>(param.getValue().getFecha());
                    }
                });
        colFecha.setCellFactory(DatePickerTableCell.dateCellFactory("dd/MM/yyyy"));
        colFecha.setOnEditCommit((CellEditEvent<ValorDato, Date> t) -> {
            if(t.getNewValue().before(currentStatus.getCopaStatus().getPeriodo().getInicio())
            || t.getNewValue().after(currentStatus.getCopaStatus().getPeriodo().getFin())) {
                ConfirmDialogController.message(fxWeaver, "Fecha fuera del período");
                t.getRowValue().setFecha(t.getOldValue());
                
                return;
            }
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
            ((ValorDato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(t.getNewValue());
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

        ObservableList<Dato> datos = FXCollections.observableArrayList(
                datoRepo.findByRecurso_EmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        datos.add(0, null);

        loadScmbDatos(datos);

        Callback<TableColumn<ValorDato, Dato>, TableCell<ValorDato, Dato>> datoCellFactory = ComboBoxTableCell
                .forTableColumn(datoConverter, datos);

        colDato.setCellFactory(datoCellFactory);

        rawItems = FXCollections.observableArrayList(valorDatoService.getRepo()
                .findByDato_Recurso_EmpresaAndEscenarioAndFechaGreaterThanEqualAndFechaLessThanEqual(empresa, escenario,
                        periodo.getInicio(), periodo.getFin()));

        showFiltredElements();
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    private void showFiltredElements() {
        ObservableList<ValorDato> filtredItems;
        if (scmbFiltroDato.getSelectionModel().getSelectedItem() == null) {
            filtredItems = FXCollections.observableArrayList(rawItems);
        } else {
            filtredItems = FXCollections.observableArrayList(
                    rawItems.stream().filter(rxa -> testInclude(rxa)).collect(Collectors.toList()));
        }
        tblValoresDatos.setItems(filtredItems);
    }

    private boolean testInclude(ValorDato vd) {
        boolean ret = true;
        ret = ret && (scmbFiltroDato.getSelectionModel().getSelectedItem() == null
                || scmbFiltroDato.getSelectionModel().getSelectedItem().equals(vd.getDato()));

        return ret;
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
                .forEach(id -> valorDatoService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ValorDato dto) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando actividad " + dto.toString());
        valorDatoService.getRepo().save(dto);
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
