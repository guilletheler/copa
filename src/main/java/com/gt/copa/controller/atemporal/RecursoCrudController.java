package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.component.CurrentStatus;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Recurso;
import com.gt.copa.repo.atemporal.RecursoRepo;

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
@FxmlView("/com/gt/copa/view/atemporal/RecursoCrudView.fxml")
public class RecursoCrudController {

    @Autowired
    RecursoRepo recursoRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Recurso> tblRecursos;

    @FXML
    private TableColumn<Recurso, Integer> colId;

    @FXML
    private TableColumn<Recurso, Integer> colCodigo;

    @FXML
    private TableColumn<Recurso, String> colNombre;

    List<Recurso> paraGuardar;
    List<Recurso> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblRecursos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblRecursos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblRecursos.getSelectionModel().getSelectedItems());
            tblRecursos.getItems().removeAll(tblRecursos.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Recurso recurso = new Recurso();

        recurso.setNombre("nuevo");
        recurso.setEmpresa(currentStatus.getCopaStatus().getEmpresa());

        int codigo = tblRecursos.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        recurso.setCodigo(codigo);

        tblRecursos.getItems().add(recurso);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Recurso, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Recurso, Integer> t) -> {
            ((Recurso) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Recurso) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Recurso, String> t) -> {
            ((Recurso) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Recurso) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(Recurso recurso) {
        if (!paraGuardar.contains(recurso)) {
            paraGuardar.add(recurso);
        }
    }

    public void loadData() {

        tblRecursos.setItems(FXCollections.observableArrayList(StreamSupport
                .stream(recursoRepo.findByEmpresa(currentStatus.getCopaStatus().getEmpresa()).spliterator(), false)
                .collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> recursoRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> recursoRepo.deleteById(id));
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
