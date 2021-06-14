package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gt.copa.component.CurrentStatus;
import com.gt.copa.component.DriverConverter;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Driver;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.repo.atemporal.DriverRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/atemporal/ComponenteDriverCrudView.fxml")
public class ComponenteDriverCrudController {

    @Autowired
    ComponenteDriverService componanteDriverService;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    DriverConverter driverConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<ComponenteDriver> tblComponenteDrivers;

    @FXML
    private TableColumn<ComponenteDriver, Integer> colId;

    @FXML
    private TableColumn<ComponenteDriver, Integer> colCodigo;

    @FXML
    private TableColumn<ComponenteDriver, String> colNombre;

    @FXML
    private TableColumn<ComponenteDriver, Driver> colDriver;

    List<ComponenteDriver> paraGuardar;
    List<ComponenteDriver> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblComponenteDrivers.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblComponenteDrivers.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblComponenteDrivers.getSelectionModel().getSelectedItems());
            tblComponenteDrivers.getItems().removeAll(tblComponenteDrivers.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        ComponenteDriver componanteDriver = new ComponenteDriver();

        componanteDriver.setNombre("nuevo");

        int codigo = tblComponenteDrivers.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        componanteDriver.setCodigo(codigo);

        tblComponenteDrivers.getItems().add(componanteDriver);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ComponenteDriver, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<ComponenteDriver, Integer> t) -> {
            ((ComponenteDriver) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setCodigo(t.getNewValue());
            modificado(((ComponenteDriver) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<ComponenteDriver, String> t) -> {
            ((ComponenteDriver) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setNombre(t.getNewValue());
            modificado(((ComponenteDriver) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colDriver.setCellValueFactory(new PropertyValueFactory<>("driver"));

        colDriver.setOnEditCommit((CellEditEvent<ComponenteDriver, Driver> t) -> {
            ((ComponenteDriver) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setDriver(t.getNewValue());
            modificado(((ComponenteDriver) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

    }

    private void modificado(ComponenteDriver componanteDriver) {
        if (!paraGuardar.contains(componanteDriver)) {
            paraGuardar.add(componanteDriver);
        }
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();

        Callback<TableColumn<ComponenteDriver, Driver>, TableCell<ComponenteDriver, Driver>> driverCellFactory = ComboBoxTableCell
                .forTableColumn(driverConverter, FXCollections.observableArrayList(driverRepo.findByEmpresaOrderByNombre(empresa)));

        colDriver.setCellFactory(driverCellFactory);

        tblComponenteDrivers
                .setItems(FXCollections.observableArrayList(componanteDriverService.findByEmpresa(empresa)));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> componanteDriverService.getRepo().save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> componanteDriverService.getRepo().deleteById(id));
        this.loadData();
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
