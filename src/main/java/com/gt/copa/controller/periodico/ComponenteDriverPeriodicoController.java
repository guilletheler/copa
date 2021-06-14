package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.gt.copa.component.ComponenteDriverConverter;
import com.gt.copa.component.CurrentStatus;
import com.gt.copa.component.DriverConverter;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Driver;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ComponenteDriverPeriodico;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.DriverRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;
import com.gt.copa.service.periodico.ComponenteDriverPeriodicoService;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/periodico/ComponenteDriverPeriodicoView.fxml")
public class ComponenteDriverPeriodicoController {

    @Autowired
    ComponenteDriverPeriodicoService componenteDriverPeriodicoService;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    DriverConverter driverConverter;

    @Autowired
    ComponenteDriverConverter componenteDriverConverter;

    @Autowired
    ComponenteDriverService componenteDriverService;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private SearchableComboBox<Driver> scmbFiltroDriver;

    @FXML
    private TableView<ComponenteDriverPeriodico> tblComponentesDriver;

    @FXML
    private TableColumn<ComponenteDriverPeriodico, Integer> colId;

    @FXML
    private TableColumn<ComponenteDriverPeriodico, String> colDriver;

    @FXML
    private TableColumn<ComponenteDriverPeriodico, String> colComponente;

    @FXML
    private TableColumn<ComponenteDriverPeriodico, Double> colValor;

    List<ComponenteDriverPeriodico> paraGuardar;

    private ObservableList<ComponenteDriverPeriodico> rawItems;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblComponentesDriver.getSelectionModel().getSelectedItems() != null) {
            for (ComponenteDriverPeriodico cdp : tblComponentesDriver.getSelectionModel().getSelectedItems()) {
                cdp.setValor(null);
            }
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ComponenteDriverPeriodico, Integer>("id"));

        colDriver.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ComponenteDriverPeriodico, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ComponenteDriverPeriodico, String> param) {

                        return new SimpleObjectProperty<>(
                                param.getValue().getComponenteDriver().getDriver().getNombre());
                    }
                });

        colComponente.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ComponenteDriverPeriodico, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ComponenteDriverPeriodico, String> param) {

                        return new SimpleObjectProperty<>(param.getValue().getComponenteDriver().getNombre());
                    }
                });

        colValor.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ComponenteDriverPeriodico, Double>, ObservableValue<Double>>() {

                    @Override
                    public ObservableValue<Double> call(
                            TableColumn.CellDataFeatures<ComponenteDriverPeriodico, Double> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValor());
                    }
                });
        colValor.setCellFactory(EditingTextCell.doubleCellFactory());
        colValor.setOnEditCommit((CellEditEvent<ComponenteDriverPeriodico, Double> t) -> {
            ((ComponenteDriverPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setValor(t.getNewValue());
            modificado(((ComponenteDriverPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ComponenteDriverPeriodico recurso) {
        if (!paraGuardar.contains(recurso)) {
            paraGuardar.add(recurso);
        }
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();
        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        ObservableList<Driver> drivers = FXCollections
                .observableArrayList(driverRepo.findByEmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()));

        drivers.add(0, null);

        loadScmbDrivers(drivers);

        rawItems = FXCollections
                .observableArrayList(componenteDriverPeriodicoService.findOrCreate(empresa, escenario, periodo));

        showFiltredElements();
        paraGuardar = new ArrayList<>();
    }

    private void showFiltredElements() {
        ObservableList<ComponenteDriverPeriodico> filtredItems;
        if (scmbFiltroDriver.getSelectionModel().getSelectedItem() == null) {
            filtredItems = rawItems;
        } else {
            filtredItems = FXCollections.observableArrayList(
                    rawItems.stream().filter(rxa -> testInclude(rxa)).collect(Collectors.toList()));
        }
        tblComponentesDriver.setItems(filtredItems);
    }

    private boolean testInclude(ComponenteDriverPeriodico cdp) {
        boolean ret = true;
        ret = ret && (scmbFiltroDriver.getSelectionModel().getSelectedItem() == null || scmbFiltroDriver
                .getSelectionModel().getSelectedItem().equals(cdp.getComponenteDriver().getDriver()));
        return ret;
    }

    private void loadScmbDrivers(ObservableList<Driver> drivers) {

        scmbFiltroDriver.setCellFactory(ComboBoxListCell.forListView(driverConverter, drivers));

        scmbFiltroDriver.setItems(drivers);

        scmbFiltroDriver.setConverter(driverConverter);

        scmbFiltroDriver.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                showFiltredElements();
            }
        });
    }

    public void persist() {

        List<ComponenteDriverPeriodico> paraGuardarTmp = paraGuardar.stream().filter(cdp -> cdp.getValor() != null)
                .collect(Collectors.toList());
        List<ComponenteDriverPeriodico> paraEliminar = paraGuardar.stream().filter(cdp -> cdp.getValor() == null)
                .collect(Collectors.toList());

        paraGuardarTmp.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> componenteDriverPeriodicoService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ComponenteDriverPeriodico dto) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando cdp " + dto.toString());
        componenteDriverPeriodicoService.getRepo().save(dto);
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
