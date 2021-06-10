package com.gt.copa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.repo.atemporal.EscenarioRepo;

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
@FxmlView("/com/gt/copa/view/EscenarioCrudView.fxml")
public class EscenarioCrudController {

    @Autowired
    EscenarioRepo EscenarioRepo;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Escenario> tblEscenarios;

    @FXML
    private TableColumn<Escenario, Integer> colId;

    @FXML
    private TableColumn<Escenario, Integer> colCodigo;

    @FXML
    private TableColumn<Escenario, String> colNombre;

    List<Escenario> paraGuardar;
    List<Escenario> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblEscenarios.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblEscenarios.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblEscenarios.getSelectionModel().getSelectedItems());
            tblEscenarios.getItems().removeAll(tblEscenarios.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Escenario Escenario = new Escenario();

        Escenario.setNombre("nuevo");

        int codigo = tblEscenarios.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        Escenario.setCodigo(codigo);

        tblEscenarios.getItems().add(Escenario);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Escenario, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Escenario, Integer> t) -> {
            ((Escenario) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Escenario) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Escenario, String> t) -> {
            ((Escenario) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Escenario) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(Escenario Escenario) {
        if (!paraGuardar.contains(Escenario)) {
            paraGuardar.add(Escenario);
        }
    }

    public void loadData() {

        tblEscenarios.setItems(FXCollections.observableArrayList(
                StreamSupport.stream(EscenarioRepo.findAll().spliterator(), false).collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> EscenarioRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> EscenarioRepo.deleteById(id));
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
