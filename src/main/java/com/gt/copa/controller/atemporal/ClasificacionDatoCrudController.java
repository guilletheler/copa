package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.components.TipoClasificacionDatoConverter;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.TipoClasificacionDato;
import com.gt.copa.repo.atemporal.ClasificacionDatoRepo;
import com.gt.copa.repo.atemporal.TipoClasificacionDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/atemporal/ClasificacionDatoCrudView.fxml")
public class ClasificacionDatoCrudController {

    @Autowired
    ClasificacionDatoRepo clasificacionDatoRepo;

    @Autowired
    TipoClasificacionDatoRepo tipoClasificacionDatoRepo;

    @Autowired
    TipoClasificacionDatoConverter tipoClasificacionDatoConverter;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<ClasificacionDato> tblClasificacionDatos;

    @FXML
    private TableColumn<ClasificacionDato, Integer> colId;

    @FXML
    private TableColumn<ClasificacionDato, Integer> colCodigo;

    @FXML
    private TableColumn<ClasificacionDato, String> colNombre;

    @FXML
    private TableColumn<ClasificacionDato, TipoClasificacionDato> colTipo;

    List<ClasificacionDato> paraGuardar;
    List<ClasificacionDato> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblClasificacionDatos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblClasificacionDatos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblClasificacionDatos.getSelectionModel().getSelectedItems());
            tblClasificacionDatos.getItems().removeAll(tblClasificacionDatos.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        ClasificacionDato ClasificacionDato = new ClasificacionDato();

        ClasificacionDato.setNombre("nuevo");

        int codigo = tblClasificacionDatos.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        ClasificacionDato.setCodigo(codigo);

        tblClasificacionDatos.getItems().add(ClasificacionDato);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ClasificacionDato, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<ClasificacionDato, Integer> t) -> {
            ((ClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setCodigo(t.getNewValue());
            modificado(((ClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<ClasificacionDato, String> t) -> {
            ((ClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setNombre(t.getNewValue());
            modificado(((ClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoClasificacion"));

        colTipo.setOnEditCommit((CellEditEvent<ClasificacionDato, TipoClasificacionDato> t) -> {

            Logger.getLogger(getClass().getName()).log(Level.INFO, "cambiando tipo a " + t.getNewValue().getEtiqueta());
            ((ClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setTipoClasificacion(t.getNewValue());
            modificado(((ClasificacionDato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ClasificacionDato clasificacionDato) {
        if (!paraGuardar.contains(clasificacionDato)) {
            paraGuardar.add(clasificacionDato);
        }
    }

    public void loadData() {

        colTipo.setCellFactory(ComboBoxTableCell.forTableColumn(tipoClasificacionDatoConverter,
                FXCollections.observableArrayList(
                        StreamSupport.stream(tipoClasificacionDatoRepo.findAll().spliterator(), false)
                                .collect(Collectors.toList()))));

        tblClasificacionDatos.setItems(FXCollections.observableArrayList(StreamSupport
                .stream(clasificacionDatoRepo.findAll().spliterator(), false).collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> clasificacionDatoRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> clasificacionDatoRepo.deleteById(id));
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
