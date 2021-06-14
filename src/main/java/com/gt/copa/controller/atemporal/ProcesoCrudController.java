package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.component.CurrentStatus;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Proceso;
import com.gt.copa.repo.atemporal.ProcesoRepo;

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
@FxmlView("/com/gt/copa/view/atemporal/ProcesoCrudView.fxml")
public class ProcesoCrudController {

    @Autowired
    ProcesoRepo procesoRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Proceso> tblProcesos;

    @FXML
    private TableColumn<Proceso, Integer> colId;

    @FXML
    private TableColumn<Proceso, Integer> colCodigo;

    @FXML
    private TableColumn<Proceso, String> colNombre;

    List<Proceso> paraGuardar;
    List<Proceso> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblProcesos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblProcesos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblProcesos.getSelectionModel().getSelectedItems());
            tblProcesos.getItems().removeAll(tblProcesos.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Proceso proceso = new Proceso();

        proceso.setNombre("nuevo");
        proceso.setEmpresa(currentStatus.getCopaStatus().getEmpresa());

        int codigo = tblProcesos.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        proceso.setCodigo(codigo);

        tblProcesos.getItems().add(proceso);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Proceso, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Proceso, Integer> t) -> {
            ((Proceso) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Proceso) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Proceso, String> t) -> {
            ((Proceso) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Proceso) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(Proceso proceso) {
        if (!paraGuardar.contains(proceso)) {
            paraGuardar.add(proceso);
        }
    }

    public void loadData() {

        tblProcesos.setItems(FXCollections.observableArrayList(StreamSupport
                .stream(procesoRepo.findByEmpresaOrderByNombre(currentStatus.getCopaStatus().getEmpresa()).spliterator(), false)
                .collect(Collectors.toList())));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> procesoRepo.save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> procesoRepo.deleteById(id));
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
