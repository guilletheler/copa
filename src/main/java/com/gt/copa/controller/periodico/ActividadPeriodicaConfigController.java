package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.ProcesoConverter;
import com.gt.copa.components.TipoDistribucionConverter;
import com.gt.copa.controller.ModificadorDatos;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ActividadPeriodica;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ProcesoRepo;
import com.gt.copa.service.periodico.ActividadPeriodicaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
@FxmlView("/com/gt/copa/view/periodico/ActividadPeriodicaConfigView.fxml")
public class ActividadPeriodicaConfigController implements ModificadorDatos {

    @Autowired
    ActividadPeriodicaService actividadPeriodicaService;

    @Autowired
    ProcesoRepo procesoRepo;

    @Autowired
    ProcesoConverter procesoConverter;

    @Autowired
    TipoDistribucionConverter tipoDistribucionConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<ActividadPeriodica> tblActividadesPeriodicas;

    @FXML
    private TableColumn<ActividadPeriodica, Integer> colId;

    @FXML
    private TableColumn<ActividadPeriodica, String> colActividad;

    @FXML
    private TableColumn<ActividadPeriodica, TipoDistribucion> colTipoDistribucion;

    List<ActividadPeriodica> paraGuardar;

    @Getter
    boolean dataModificada;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblActividadesPeriodicas.getSelectionModel().getSelectedItems() != null) {
            tblActividadesPeriodicas.getSelectionModel().getSelectedItems().forEach(ap -> ap.setTipoDistribucion(null));
            paraGuardar.addAll(tblActividadesPeriodicas.getSelectionModel().getSelectedItems());
            dataModificada = true;
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ActividadPeriodica, Integer>("id"));

        colActividad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ActividadPeriodica, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ActividadPeriodica, String> param) {
                return new SimpleStringProperty(param.getValue().getActividad().getNombre());
            }
        });

        colTipoDistribucion.setCellValueFactory(new PropertyValueFactory<ActividadPeriodica, TipoDistribucion>("tipoDistribucion"));

        colTipoDistribucion.setOnEditCommit((CellEditEvent<ActividadPeriodica, TipoDistribucion> t) -> {
            ((ActividadPeriodica) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTipoDistribucion(t.getNewValue());
            modificado(((ActividadPeriodica) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        ObservableList<TipoDistribucion> tiposDistribucionValues = FXCollections.observableArrayList(TipoDistribucion.values());
        tiposDistribucionValues.add(0, null);

        Callback<TableColumn<ActividadPeriodica, TipoDistribucion>, TableCell<ActividadPeriodica, TipoDistribucion>> tipoDistribucionCellFactory = ComboBoxTableCell
                .forTableColumn(tipoDistribucionConverter,tiposDistribucionValues);

        colTipoDistribucion.setCellFactory(tipoDistribucionCellFactory);
    }

    private void modificado(ActividadPeriodica actividad) {
        if (!paraGuardar.contains(actividad)) {
            paraGuardar.add(actividad);
        }
        dataModificada = true;
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();
        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        tblActividadesPeriodicas.setItems(FXCollections.observableArrayList(actividadPeriodicaService.findOrCreate(empresa, escenario, periodo)));
        paraGuardar = new ArrayList<>();
        dataModificada = false;
    }

    public void persist() {

        List<ActividadPeriodica> realParaGuardar = paraGuardar.stream().filter(ap -> ap.getTipoDistribucion() != null).collect(Collectors.toList());
        List<ActividadPeriodica> paraEliminar = paraGuardar.stream().filter(ap -> ap.getTipoDistribucion() == null).collect(Collectors.toList());

        realParaGuardar.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> actividadPeriodicaService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ActividadPeriodica dto) {
        // Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando actividad " + dto.toString());
        actividadPeriodicaService.getRepo().save(dto);
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
