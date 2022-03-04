package com.gt.copa.controller.atemporal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.components.ClasificacionDatoConverter;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.RecursoConverter;
import com.gt.copa.controller.ModificadorDatos;
import com.gt.copa.infra.CheckComboBoxTableCell;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Recurso;
import com.gt.copa.repo.atemporal.ClasificacionDatoRepo;
import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.service.atemporal.DatoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/atemporal/DatoCrudView.fxml")
public class DatoCrudController implements ModificadorDatos {

    @Autowired
    DatoService datoService;

    @Autowired
    RecursoRepo recursoRepo;

    @Autowired
    ClasificacionDatoRepo clasificacionDatoRepo;

    @Autowired
    RecursoConverter recursoConverter;

    @Autowired
    ClasificacionDatoConverter clasificacionDatoConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<Dato> tblDatos;

    @FXML
    private TableColumn<Dato, Integer> colId;

    @FXML
    private TableColumn<Dato, Integer> colCodigo;

    @FXML
    private TableColumn<Dato, String> colNombre;

    @FXML
    private TableColumn<Dato, Recurso> colRecurso;

    @FXML
    private TableColumn<Dato, Collection<ClasificacionDato>> colClasificaciones;

    List<Dato> paraGuardar;
    List<Dato> paraEliminar;

    @Getter
    boolean dataModificada;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblDatos.getSelectionModel().getSelectedItems() != null) {
            paraEliminar.addAll(tblDatos.getSelectionModel().getSelectedItems());
            paraGuardar.removeAll(tblDatos.getSelectionModel().getSelectedItems());
            tblDatos.getItems().removeAll(tblDatos.getSelectionModel().getSelectedItems());
            dataModificada = true;
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    void btnNuevoClick(ActionEvent event) {

        Dato dato = new Dato();

        dato.setNombre("nuevo");

        int codigo = tblDatos.getItems().stream().map(sd -> sd.getCodigo())
                .collect(Collectors.summarizingInt(Integer::intValue)).getMax() + 1;
        if (codigo < 0) {
            codigo = 1;
        }

        dato.setCodigo(codigo);

        tblDatos.getItems().add(dato);
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<Dato, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(EditingTextCell.integerCellFactory());
        colCodigo.setOnEditCommit((CellEditEvent<Dato, Integer> t) -> {
            ((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
            modificado(((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(EditingTextCell.stringCellFactory());
        colNombre.setOnEditCommit((CellEditEvent<Dato, String> t) -> {
            ((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
            modificado(((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colRecurso.setCellValueFactory(new PropertyValueFactory<>("recurso"));

        colRecurso.setOnEditCommit((CellEditEvent<Dato, Recurso> t) -> {
            ((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRecurso(t.getNewValue());
            modificado(((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colClasificaciones.setCellValueFactory(new PropertyValueFactory<>("clasificaciones"));

        colClasificaciones.setOnEditCommit((CellEditEvent<Dato, Collection<ClasificacionDato>> t) -> {
            ((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())).getClasificaciones().clear();
            ((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())).getClasificaciones()
                    .addAll(t.getNewValue());
            modificado(((Dato) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(Dato dato) {
        if (!paraGuardar.contains(dato)) {
            paraGuardar.add(dato);
        }
        dataModificada = true;
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();

        Callback<TableColumn<Dato, Recurso>, TableCell<Dato, Recurso>> recursoCellFactory = ComboBoxTableCell
                .forTableColumn(recursoConverter,
                        FXCollections.observableArrayList(recursoRepo.findByEmpresaOrderByNombre(empresa)));

        colRecurso.setCellFactory(recursoCellFactory);

        colClasificaciones.setCellFactory(CheckComboBoxTableCell.checkComboCellFactory(
                StreamSupport.stream(clasificacionDatoRepo.findAll().spliterator(), false).collect(Collectors.toList()),
                clasificacionDatoConverter));

        tblDatos.setItems(FXCollections
                .observableArrayList(datoService.findByEmpresa(empresa)));
        paraGuardar = new ArrayList<>();
        paraEliminar = new ArrayList<>();
        dataModificada = false;
    }

    public void persist() {
        paraGuardar.forEach(dto -> datoService.getRepo().save(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> datoService.getRepo().deleteById(id));
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
