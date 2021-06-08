package com.gt.copa.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.infra.EditingCell;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.model.temporal.TipoPeriodo;
import com.gt.copa.repo.temporal.PeriodoRepo;

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
@FxmlView("/com/gt/copa/view/PeriodoCrudView.fxml")
public class PeriodoCrudController {

    @Autowired
    PeriodoRepo PeriodoRepo;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Periodo> tblPeriodos;

    @FXML
    private TableColumn<Periodo, Integer> colId;

    @FXML
    private TableColumn<Periodo, Integer> colCodigo;

    @FXML
    private TableColumn<Periodo, String> colNombre;

    @FXML
    private TableColumn<Periodo, TipoPeriodo> colTipo;

    @FXML
    private TableColumn<Periodo, Date> colInicio;

    @FXML
    private TableColumn<Periodo, Date> colFin;

    List<Periodo> paraGuardar;
    List<Periodo> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblPeriodos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblPeriodos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblPeriodos.getSelectionModel().getSelectedItems());
            tblPeriodos.getItems().removeAll(tblPeriodos.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Periodo Periodo = new Periodo();

        Periodo.setNombre("nuevo");

        int codigo = tblPeriodos.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        Periodo.setCodigo(codigo);

        tblPeriodos.getItems().add(Periodo);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Periodo, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Periodo, Integer> t) -> {
            ((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Periodo, String> t) -> {
            ((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoPeriodo"));
        colTipo.setCellFactory(
                ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(TipoPeriodo.values())));

        colTipo.setOnEditCommit((CellEditEvent<Periodo, TipoPeriodo> t) -> {
            ((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTipoPeriodo(t.getNewValue());
            modificado(((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colInicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
        colInicio.setCellFactory(EditingCell.dateCellFactory());
        colInicio.setOnEditCommit((CellEditEvent<Periodo, Date> t) -> {
            ((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setInicio(t.getNewValue());
            modificado(((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colFin.setCellValueFactory(new PropertyValueFactory<>("fin"));
        colFin.setCellFactory(EditingCell.dateCellFactory());
        colFin.setOnEditCommit((CellEditEvent<Periodo, Date> t) -> {
            ((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFin(t.getNewValue());
            modificado(((Periodo) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

    }

    private void modificado(Periodo Periodo) {
        if (!paraGuardar.contains(Periodo)) {
            paraGuardar.add(Periodo);
        }
    }

    public void loadData() {

        tblPeriodos.setItems(FXCollections.observableArrayList(
                StreamSupport.stream(PeriodoRepo.findAll().spliterator(), false).collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> PeriodoRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> PeriodoRepo.deleteById(id));
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
