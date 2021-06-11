package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.gt.copa.component.CurrentStatus;
import com.gt.copa.component.ProcesoConverter;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Proceso;
import com.gt.copa.repo.atemporal.ProcesoRepo;
import com.gt.copa.service.atemporal.ActividadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/atemporal/ActividadCrudView.fxml")
public class ActividadCrudController {

    @Autowired
    ActividadService actividadService;

    @Autowired
    ProcesoRepo procesoRepo;

    @Autowired
    ProcesoConverter procesoConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Actividad> tblActividades;

    @FXML
    private TableColumn<Actividad, Integer> colId;

    @FXML
    private TableColumn<Actividad, Integer> colCodigo;

    @FXML
    private TableColumn<Actividad, String> colNombre;

    @FXML
    private TableColumn<Actividad, Proceso> colProceso;

    @FXML
    private TableColumn<Actividad, Boolean> colPrimaria;

    List<Actividad> paraGuardar;
    List<Actividad> paraEliminar;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblActividades.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblActividades.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblActividades.getSelectionModel().getSelectedItems());
            tblActividades.getItems().removeAll(tblActividades.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Actividad actividad = new Actividad();

        actividad.setNombre("nuevo");

        int codigo = tblActividades.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        actividad.setCodigo(codigo);
        actividad.setPrimaria(true);

        tblActividades.getItems().add(actividad);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Actividad, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<Actividad, Integer>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Actividad, Integer> t) -> {
            ((Actividad) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Actividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<Actividad, String>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Actividad, String> t) -> {
            ((Actividad) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Actividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colProceso.setCellValueFactory(new PropertyValueFactory<Actividad, Proceso>("proceso"));

        colProceso.setOnEditCommit((CellEditEvent<Actividad, Proceso> t) -> {
            ((Actividad) t.getTableView().getItems().get(t.getTablePosition().getRow())).setProceso(t.getNewValue());
            modificado(((Actividad) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colPrimaria.setCellValueFactory(new PropertyValueFactory<Actividad, Boolean>("primaria"));

        colPrimaria.setCellFactory(new Callback<TableColumn<Actividad, Boolean>, TableCell<Actividad, Boolean>>() {

            @Override
            public TableCell<Actividad, Boolean> call(TableColumn<Actividad, Boolean> param) {
                CheckBoxTableCell<Actividad, Boolean> ctCell = new CheckBoxTableCell<>();
                ctCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(Integer index) {
                        Actividad actividad = tblActividades.getItems().get(index);
                        BooleanProperty selected = new SimpleBooleanProperty(actividad.getPrimaria());
                        selected.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected,
                                    Boolean isSelected) {
                                actividad.setPrimaria(isSelected);
                                modificado(actividad);
                            }
                        });
                        return selected;
                    }
                });
                return ctCell;
            }

        });
        // colPrimaria.setOnEditCommit((CellEditEvent<Actividad, Boolean> t) -> {
        // Logger.getLogger(getClass().getName()).log(Level.INFO, "seteando primaria " +
        // t.getNewValue());

        // ((Actividad)
        // t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrimaria(t.getNewValue());
        // modificado(((Actividad)
        // t.getTableView().getItems().get(t.getTablePosition().getRow())));
        // });

    }

    private void modificado(Actividad actividad) {
        if (!paraGuardar.contains(actividad)) {
            paraGuardar.add(actividad);
        }
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();

        Callback<TableColumn<Actividad, Proceso>, TableCell<Actividad, Proceso>> procesoCellFactory = ComboBoxTableCell
                .forTableColumn(procesoConverter,
                        FXCollections.observableArrayList(procesoRepo.findByEmpresa(empresa)));

        colProceso.setCellFactory(procesoCellFactory);

        tblActividades.setItems(FXCollections.observableArrayList(actividadService.findByEmpresa(empresa)));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
    }

    public void persist() {
        paraGuardar.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> actividadService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(Actividad dto) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando actividad " + dto.toString());
        actividadService.getRepo().save(dto);
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
