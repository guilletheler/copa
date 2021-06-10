package com.gt.copa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.repo.atemporal.EmpresaRepo;

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
@FxmlView("/com/gt/copa/view/EmpresaCrudView.fxml")
public class EmpresaCrudController {

    @Autowired
    EmpresaRepo EmpresaRepo;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Empresa> tblEmpresas;

    @FXML
    private TableColumn<Empresa, Integer> colId;

    @FXML
    private TableColumn<Empresa, Integer> colCodigo;

    @FXML
    private TableColumn<Empresa, String> colNombre;

    List<Empresa> paraGuardar;
    List<Empresa> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblEmpresas.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblEmpresas.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblEmpresas.getSelectionModel().getSelectedItems());
            tblEmpresas.getItems().removeAll(tblEmpresas.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Empresa Empresa = new Empresa();

        Empresa.setNombre("nuevo");

        int codigo = tblEmpresas.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        Empresa.setCodigo(codigo);

        tblEmpresas.getItems().add(Empresa);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Empresa, Integer> t) -> {
            ((Empresa) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Empresa) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Empresa, String> t) -> {
            ((Empresa) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Empresa) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(Empresa Empresa) {
        if (!paraGuardar.contains(Empresa)) {
            paraGuardar.add(Empresa);
        }
    }

    public void loadData() {

        tblEmpresas.setItems(FXCollections.observableArrayList(
                StreamSupport.stream(EmpresaRepo.findAll().spliterator(), false).collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> EmpresaRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> EmpresaRepo.deleteById(id));
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
