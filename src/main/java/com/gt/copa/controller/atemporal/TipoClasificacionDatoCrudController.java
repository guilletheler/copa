package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.TipoClasificacionDato;
import com.gt.copa.repo.atemporal.TipoClasificacionDatoRepo;

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
@FxmlView("/com/gt/copa/view/atemporal/TipoClasificacionDatoCrudView.fxml")
public class TipoClasificacionDatoCrudController {

    @Autowired
    TipoClasificacionDatoRepo tipoClasificacionDatoRepo;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<TipoClasificacionDato> tblTipoClasificacionDatos;

    @FXML
    private TableColumn<TipoClasificacionDato, Integer> colId;

    @FXML
    private TableColumn<TipoClasificacionDato, Integer> colCodigo;

    @FXML
    private TableColumn<TipoClasificacionDato, String> colNombre;

    List<TipoClasificacionDato> paraGuardar;
    List<TipoClasificacionDato> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblTipoClasificacionDatos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblTipoClasificacionDatos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblTipoClasificacionDatos.getSelectionModel().getSelectedItems());
            tblTipoClasificacionDatos.getItems().removeAll(tblTipoClasificacionDatos.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        TipoClasificacionDato TipoClasificacionDato = new TipoClasificacionDato();

        TipoClasificacionDato.setNombre("nuevo");

        int codigo = tblTipoClasificacionDatos.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        TipoClasificacionDato.setCodigo(codigo);

        tblTipoClasificacionDatos.getItems().add(TipoClasificacionDato);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<TipoClasificacionDato, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<TipoClasificacionDato, Integer> t) -> {
            ((TipoClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((TipoClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<TipoClasificacionDato, String> t) -> {
            ((TipoClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((TipoClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(TipoClasificacionDato tipoClasificacionDato) {
        if (!paraGuardar.contains(tipoClasificacionDato)) {
            paraGuardar.add(tipoClasificacionDato);
        }
    }

    public void loadData() {

        tblTipoClasificacionDatos.setItems(FXCollections.observableArrayList(
                StreamSupport.stream(tipoClasificacionDatoRepo.findAll().spliterator(), false).collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> tipoClasificacionDatoRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> tipoClasificacionDatoRepo.deleteById(id));
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
