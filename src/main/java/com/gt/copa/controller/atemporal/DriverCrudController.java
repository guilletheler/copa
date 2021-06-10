package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.component.CurrentStatus;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Driver;
import com.gt.copa.repo.atemporal.DriverRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/atemporal/DriverCrudView.fxml")
public class DriverCrudController {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Driver> tblDrivers;

    @FXML
    private TableColumn<Driver, Integer> colId;

    @FXML
    private TableColumn<Driver, Integer> colCodigo;

    @FXML
    private TableColumn<Driver, String> colNombre;

    List<Driver> paraGuardar;
    List<Driver> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblDrivers.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblDrivers.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblDrivers.getSelectionModel().getSelectedItems());
            tblDrivers.getItems().removeAll(tblDrivers.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Driver driver = new Driver();

        driver.setNombre("nuevo");
        driver.setEmpresa(currentStatus.getCopaStatus().getEmpresa());

        int codigo = tblDrivers.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        driver.setCodigo(codigo);

        tblDrivers.getItems().add(driver);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Driver, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Driver, Integer> t) -> {
            ((Driver) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Driver) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Driver, String> t) -> {
            ((Driver) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Driver) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(Driver driver) {
        if (!paraGuardar.contains(driver)) {
            paraGuardar.add(driver);
        }
    }

    public void loadData() {

        tblDrivers.setItems(FXCollections.observableArrayList(StreamSupport
                .stream(driverRepo.findByEmpresa(currentStatus.getCopaStatus().getEmpresa()).spliterator(), false)
                .collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> driverRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> driverRepo.deleteById(id));
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
