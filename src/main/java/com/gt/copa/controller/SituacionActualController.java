package com.gt.copa.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.components.ClasificacionDatoConverter;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.EmpresaConverter;
import com.gt.copa.components.EscenarioConverter;
import com.gt.copa.components.PeriodoConverter;
import com.gt.copa.components.TipoClasificacionDatoConverter;
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

import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.VBox;
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
    private CheckComboBox<ClasificacionDato> lstClasificaciones;

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

    @Autowired
    EmpresaConverter empresaConverter;

    @Autowired
    EscenarioConverter escenarioConverter;

    @Autowired
    PeriodoConverter periodoConverter;

    @Autowired
    TipoClasificacionDatoConverter tipoClasificacionDatoConverter;

    @Autowired
    ClasificacionDatoConverter clasificacionDatoConverter;

    @FXML
    public void initialize() {
        cmbEmpresa.setConverter(empresaConverter);
        lstClasificaciones.setConverter(clasificacionDatoConverter);
        cmbPeriodo.setConverter(periodoConverter);
        cmbTipoClasificacion.setConverter(tipoClasificacionDatoConverter);
        cmbEscenario.setConverter(escenarioConverter);
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

        cmbEmpresa.setCellFactory(ComboBoxListCell.forListView(empresaConverter, empresas));

        cmbEmpresa.setItems(empresas);
    }

    private void configCmbEscenario() {
        ObservableList<Escenario> escenarios = FXCollections.observableArrayList(
                StreamSupport.stream(escenarioRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        escenarios.add(0, null);

        cmbEscenario.setCellFactory(ComboBoxListCell.forListView(escenarioConverter, escenarios));

        cmbEscenario.setItems(escenarios);
    }

    private void configCmbPeriodo() {
        ObservableList<Periodo> periodos = FXCollections.observableArrayList(
                StreamSupport.stream(periodoRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        periodos.add(0, null);

        cmbPeriodo.setCellFactory(ComboBoxListCell.forListView(periodoConverter, periodos));

        cmbPeriodo.setItems(periodos);
    }

    private void configCmbTipoClasificacion() {
        ObservableList<TipoClasificacionDato> tiposClasificacion = FXCollections.observableArrayList(StreamSupport
                .stream(tipoClasificacionDatoRepo.findAll().spliterator(), false).collect(Collectors.toList()));

        tiposClasificacion.add(0, null);

        cmbTipoClasificacion.setCellFactory(ComboBoxListCell.forListView(tipoClasificacionDatoConverter, tiposClasificacion));

        cmbTipoClasificacion.setItems(tiposClasificacion);
    }

    void configLstClasificaciones() {

        List<ClasificacionDato> clasifDatos = StreamSupport.stream(clasificacionDatoRepo.findAll().spliterator(), false).collect(Collectors.toList());

        lstClasificaciones.getItems().clear();

        lstClasificaciones.getItems().addAll(clasifDatos);

    }

    @FXML
    void btnAplicarClick(ActionEvent event) {

        currentStatus.getCopaStatus().setEmpresa(cmbEmpresa.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().setEscenario(cmbEscenario.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().setPeriodo(cmbPeriodo.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().setTipoClasificacion(cmbTipoClasificacion.getSelectionModel().getSelectedItem());
        currentStatus.getCopaStatus().getFiltroClasificaciones().clear();
        currentStatus.getCopaStatus().getFiltroClasificaciones()
                .addAll(lstClasificaciones.getCheckModel().getCheckedItems());
    }

}
