package com.gt.copa.controller;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.component.CurrentStatus;
import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.atemporal.TipoClasificacionDato;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ClasificacionDatoRepo;
import com.gt.copa.repo.atemporal.EmpresaRepo;
import com.gt.copa.repo.atemporal.EscenarioRepo;
import com.gt.copa.repo.atemporal.TipoClasificacionDatoRepo;
import com.gt.copa.repo.temporal.PeriodoRepo;

import org.controlsfx.control.CheckListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/SituacionActualView.fxml")
public class SituacionActualController {

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private ComboBox<Empresa> cmbEmpresa;

    @FXML
    private ComboBox<Escenario> cmbEscenario;

    @FXML
    private ComboBox<Periodo> cmbPeriodo;

    @FXML
    private ComboBox<TipoClasificacionDato> cmbTipoClasificacion;

    @FXML
    private CheckListView<ClasificacionDato> lstClasificaciones;

    @Autowired
    CurrentStatus currentStatus;

    @Autowired
    EmpresaRepo empresaRepo;

    @Autowired
    EscenarioRepo escenarioRepo;

    @Autowired
    PeriodoRepo periodoRepo;

    @Autowired
    TipoClasificacionDatoRepo tipoClasificacionDatoRepo;

    @Autowired
    ClasificacionDatoRepo clasificacionDatoRepo;

    @FXML
    public void initialize() {

    }

    public void loadData() {
        configCmbEmpresas();
        configCmbEscenario();
        configCmbPeriodo();
        configCmbTipoClasificacion();
        configLstClasificaciones();

        cmbEmpresa.getSelectionModel().select(currentStatus.getCopaStatus().getEmpresa());
        cmbEscenario.getSelectionModel().select(currentStatus.getCopaStatus().getEscenario());
        cmbPeriodo.getSelectionModel().select(currentStatus.getCopaStatus().getPeriodo());
        cmbTipoClasificacion.getSelectionModel().select(currentStatus.getCopaStatus().getTipoClasificacion());
        lstClasificaciones.getCheckModel().clearChecks();
        currentStatus.getCopaStatus().getFiltroClasificaciones()
                .forEach(cla -> lstClasificaciones.getCheckModel().check(cla));
    }

    private void configCmbEmpresas() {
        ObservableList<Empresa> empresas = FXCollections.observableArrayList(
                StreamSupport.stream(empresaRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        empresas.add(0, null);

        StringConverter<Empresa> converter = new StringConverter<Empresa>() {

            @Override
            public String toString(Empresa object) {
                if (object == null) {
                    return "";
                }
                return object.getNombre();
            }

            @Override
            public Empresa fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return empresaRepo.findByNombre(string).orElse(null);
            }
        };

        cmbEmpresa.setConverter(converter);

        cmbEmpresa.setCellFactory(ComboBoxListCell.forListView(converter, empresas));

        cmbEmpresa.setItems(empresas);
    }

    private void configCmbEscenario() {
        ObservableList<Escenario> escenarios = FXCollections.observableArrayList(
                StreamSupport.stream(escenarioRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        escenarios.add(0, null);

        StringConverter<Escenario> converter = new StringConverter<Escenario>() {

            @Override
            public String toString(Escenario object) {
                if (object == null) {
                    return "";
                }
                return object.getNombre();
            }

            @Override
            public Escenario fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return escenarioRepo.findByNombre(string).orElse(null);
            }
        };

        cmbEscenario.setConverter(converter);

        cmbEscenario.setCellFactory(ComboBoxListCell.forListView(converter, escenarios));

        cmbEscenario.setItems(escenarios);
    }

    private void configCmbPeriodo() {
        ObservableList<Periodo> periodos = FXCollections.observableArrayList(
                StreamSupport.stream(periodoRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        periodos.add(0, null);

        StringConverter<Periodo> converter = new StringConverter<Periodo>() {

            @Override
            public String toString(Periodo object) {
                if (object == null) {
                    return "";
                }
                return object.getNombre();
            }

            @Override
            public Periodo fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return periodoRepo.findByNombre(string).orElse(null);
            }
        };

        cmbPeriodo.setConverter(converter);

        cmbPeriodo.setCellFactory(ComboBoxListCell.forListView(converter, periodos));

        cmbPeriodo.setItems(periodos);
    }

    private void configCmbTipoClasificacion() {
        ObservableList<TipoClasificacionDato> tiposClasificacion = FXCollections.observableArrayList(StreamSupport
                .stream(tipoClasificacionDatoRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        tiposClasificacion.add(0, null);

        StringConverter<TipoClasificacionDato> converter = new StringConverter<TipoClasificacionDato>() {

            @Override
            public String toString(TipoClasificacionDato object) {
                if (object == null) {
                    return "";
                }
                return object.getNombre();
            }

            @Override
            public TipoClasificacionDato fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return tipoClasificacionDatoRepo.findByNombre(string).orElse(null);
            }
        };

        cmbTipoClasificacion.setConverter(converter);

        cmbTipoClasificacion.setCellFactory(ComboBoxListCell.forListView(converter, tiposClasificacion));

        cmbTipoClasificacion.setItems(tiposClasificacion);
    }

    void configLstClasificaciones() {

        ObservableList<ClasificacionDato> tiposClasificacion = FXCollections.observableArrayList(StreamSupport
                .stream(clasificacionDatoRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        lstClasificaciones.setItems(tiposClasificacion);

        lstClasificaciones.setCellFactory(
                lv -> new CheckBoxListCell<ClasificacionDato>(lstClasificaciones::getItemBooleanProperty) {
                    @Override
                    public void updateItem(ClasificacionDato clasificacionDato, boolean empty) {
                        super.updateItem(clasificacionDato, empty);
                        setText(clasificacionDato == null ? "" : clasificacionDato.getNombre());
                    }
                });

        // lstClasificaciones.getCheckModel().getCheckedIndices().addListener(new
        // ListChangeListener<Integer>() {
        // @Override
        // public void onChanged(javafx.collections.ListChangeListener.Change<? extends
        // Integer> c) {
        // while (c.next()) {
        // if (c.wasAdded()) {
        // for (int i : c.getAddedSubList()) {
        // System.out.println(lstClasificaciones.getItems().get(i).getNombre() + "
        // selected");
        // }
        // }
        // if (c.wasRemoved()) {
        // for (int i : c.getRemoved()) {
        // System.out.println(lstClasificaciones.getItems().get(i).getNombre() + "
        // deselected");
        // }
        // }
        // }
        // }
        // });

    }

    @FXML
    void btnAplicarClick(ActionEvent event) {

        currentStatus.getCopaStatus().setEmpresa(cmbEmpresa.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().setEscenario(cmbEscenario.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().setPeriodo(cmbPeriodo.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().setTipoClasificacion(cmbTipoClasificacion.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().getFiltroClasificaciones().clear();
        currentStatus.getCopaStatus().getFiltroClasificaciones().addAll(lstClasificaciones.getCheckModel().getCheckedItems());
    }

}
