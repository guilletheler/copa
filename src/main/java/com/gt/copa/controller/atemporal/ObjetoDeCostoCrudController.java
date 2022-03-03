package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.components.CurrentStatus;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.ObjetoDeCosto;
import com.gt.copa.repo.atemporal.ObjetoDeCostoRepo;

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
@FxmlView("/com/gt/copa/view/atemporal/ObjetoDeCostoCrudView.fxml")
public class ObjetoDeCostoCrudController {

    @Autowired
    ObjetoDeCostoRepo objetoDeCostoRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<ObjetoDeCosto> tblObjetoDeCostos;

    @FXML
    private TableColumn<ObjetoDeCosto, Integer> colId;

    @FXML
    private TableColumn<ObjetoDeCosto, Integer> colCodigo;

    @FXML
    private TableColumn<ObjetoDeCosto, String> colNombre;

    List<ObjetoDeCosto> paraGuardar;
    List<ObjetoDeCosto> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblObjetoDeCostos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblObjetoDeCostos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblObjetoDeCostos.getSelectionModel().getSelectedItems());
            tblObjetoDeCostos.getItems().removeAll(tblObjetoDeCostos.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        ObjetoDeCosto objetoDeCosto = new ObjetoDeCosto();

        objetoDeCosto.setNombre("nuevo");
        objetoDeCosto.setEmpresa(currentStatus.getCopaStatus().getEmpresa());

        int codigo = tblObjetoDeCostos.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        objetoDeCosto.setCodigo(codigo);

        tblObjetoDeCostos.getItems().add(objetoDeCosto);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ObjetoDeCosto, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<ObjetoDeCosto, Integer> t) -> {
            ((ObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((ObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<ObjetoDeCosto, String> t) -> {
            ((ObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((ObjetoDeCosto) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ObjetoDeCosto objetoDeCosto) {
        if (!paraGuardar.contains(objetoDeCosto)) {
            paraGuardar.add(objetoDeCosto);
        }
    }

    public void loadData() {

        tblObjetoDeCostos.setItems(FXCollections.observableArrayList(StreamSupport
                .stream(objetoDeCostoRepo.findByEmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()).spliterator(), false)
                .collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> objetoDeCostoRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> objetoDeCostoRepo.deleteById(id));
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
