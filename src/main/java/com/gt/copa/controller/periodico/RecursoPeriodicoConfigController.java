package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.calc.api.TratamientoMuestra;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.TipoDistribucionConverter;
import com.gt.copa.components.TratamientoMuestraConverter;
import com.gt.copa.controller.ModificadorDatos;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.RecursoPeriodico;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ProcesoRepo;
import com.gt.copa.service.periodico.RecursoPeriodicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
@FxmlView("/com/gt/copa/view/periodico/RecursoPeriodicoConfigView.fxml")
public class RecursoPeriodicoConfigController implements ModificadorDatos {

    @Autowired
    RecursoPeriodicoService recursoPeriodicoService;

    @Autowired
    ProcesoRepo procesoRepo;

    @Autowired
    TipoDistribucionConverter tipoDistribucionConverter;

    @Autowired
    TratamientoMuestraConverter tratamientoMuestraConverter;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<RecursoPeriodico> tblRecursosPeriodicas;

    @FXML
    private TableColumn<RecursoPeriodico, Integer> colId;

    @FXML
    private TableColumn<RecursoPeriodico, String> colRecurso;

    @FXML
    private TableColumn<RecursoPeriodico, TipoDistribucion> colTipoDistribucion;

    @FXML
    private TableColumn<RecursoPeriodico, TratamientoMuestra> colTratamientoMuestra;

    @FXML
    private TableColumn<RecursoPeriodico, Integer> colTamanioMuestra;

    @FXML
    private TableColumn<RecursoPeriodico, Boolean> colPromedioNoVacio;

    List<RecursoPeriodico> paraGuardar;

    @Getter
    boolean dataModificada;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblRecursosPeriodicas.getSelectionModel().getSelectedItems() != null) {
            tblRecursosPeriodicas.getSelectionModel().getSelectedItems().forEach(ap -> ap.setTipoDistribucion(null));
            paraGuardar.addAll(tblRecursosPeriodicas.getSelectionModel().getSelectedItems());
            dataModificada = true;
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<RecursoPeriodico, Integer>("id"));

        colRecurso.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<RecursoPeriodico, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<RecursoPeriodico, String> param) {
                        return new SimpleStringProperty(param.getValue().getRecurso().getNombre());
                    }
                });

        colTipoDistribucion
                .setCellValueFactory(new PropertyValueFactory<RecursoPeriodico, TipoDistribucion>("tipoDistribucion"));

        colTipoDistribucion.setOnEditCommit((CellEditEvent<RecursoPeriodico, TipoDistribucion> t) -> {
            ((RecursoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setTipoDistribucion(t.getNewValue());
            modificado(((RecursoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
        ObservableList<TipoDistribucion> tiposDistribucionValues = FXCollections
                .observableArrayList(TipoDistribucion.values());
        tiposDistribucionValues.add(0, null);

        Callback<TableColumn<RecursoPeriodico, TipoDistribucion>, TableCell<RecursoPeriodico, TipoDistribucion>> tipoDistribucionCellFactory = ComboBoxTableCell
                .forTableColumn(tipoDistribucionConverter,
                        tiposDistribucionValues);

        colTipoDistribucion.setCellFactory(tipoDistribucionCellFactory);

        colTratamientoMuestra.setCellValueFactory(
                new PropertyValueFactory<RecursoPeriodico, TratamientoMuestra>("tratamientoMuestra"));

        colTratamientoMuestra.setOnEditCommit((CellEditEvent<RecursoPeriodico, TratamientoMuestra> t) -> {
            ((RecursoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setTratamientoMuestra(t.getNewValue());
            modificado(((RecursoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        Callback<TableColumn<RecursoPeriodico, TratamientoMuestra>, TableCell<RecursoPeriodico, TratamientoMuestra>> tratamientoMuestraCellFactory = ComboBoxTableCell
                .forTableColumn(tratamientoMuestraConverter,
                        FXCollections.observableArrayList(TratamientoMuestra.values()));

        colTratamientoMuestra.setCellFactory(tratamientoMuestraCellFactory);

        colTamanioMuestra.setCellValueFactory(new PropertyValueFactory<>("tamanioMuestra"));
        colTamanioMuestra.setCellFactory(EditingTextCell.integerCellFactory());
        colTamanioMuestra.setOnEditCommit((CellEditEvent<RecursoPeriodico, Integer> t) -> {
            ((RecursoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setTamanioMuestra(t.getNewValue());
            modificado(((RecursoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colPromedioNoVacio.setCellValueFactory(new PropertyValueFactory<RecursoPeriodico, Boolean>("promedioNoVacio"));

        colPromedioNoVacio.setCellFactory(
                new Callback<TableColumn<RecursoPeriodico, Boolean>, TableCell<RecursoPeriodico, Boolean>>() {

                    @Override
                    public TableCell<RecursoPeriodico, Boolean> call(TableColumn<RecursoPeriodico, Boolean> param) {
                        CheckBoxTableCell<RecursoPeriodico, Boolean> ctCell = new CheckBoxTableCell<>();
                        ctCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
                            @Override
                            public ObservableValue<Boolean> call(Integer index) {
                                RecursoPeriodico actividad = tblRecursosPeriodicas.getItems().get(index);
                                BooleanProperty selected = new SimpleBooleanProperty(actividad.getPromedioNoVacio());
                                selected.addListener(new ChangeListener<Boolean>() {
                                    @Override
                                    public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected,
                                            Boolean isSelected) {
                                        actividad.setPromedioNoVacio(isSelected);
                                        modificado(actividad);
                                    }
                                });
                                return selected;
                            }
                        });
                        return ctCell;
                    }

                });
    }

    private void modificado(RecursoPeriodico recurso) {
        if (!paraGuardar.contains(recurso)) {
            paraGuardar.add(recurso);
            dataModificada = true;
        }
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();
        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        tblRecursosPeriodicas.setItems(
                FXCollections.observableArrayList(recursoPeriodicoService.findOrCreate(empresa, escenario, periodo)));
        paraGuardar = new ArrayList<>();
        dataModificada = false;
    }

    public void persist() {
        try {

            List<RecursoPeriodico> realParaGuardar = paraGuardar.stream().filter(ap -> ap.getTipoDistribucion() != null)
                    .collect(Collectors.toList());
            List<RecursoPeriodico> paraEliminar = paraGuardar.stream().filter(ap -> ap.getTipoDistribucion() == null)
                    .collect(Collectors.toList());

            realParaGuardar.forEach(dto -> guardar(dto));
            paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                    .forEach(id -> recursoPeriodicoService.getRepo().deleteById(id));
            this.loadData();
        } catch (ConstraintViolationException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error de validación");

            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
                sb.append(cv.getPropertyPath() + " " + cv.getMessage());
                sb.append("\n");
            }

            alert.setContentText(sb.toString());
            alert.showAndWait();
        } catch (ValidationException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error de validación");

            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        } catch (PersistenceException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al guardar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al guardar", e);
        }
    }

    private void guardar(RecursoPeriodico dto) {
        // Logger.getLogger(getClass().getName()).log(Level.INFO, "guardando recurso " + dto.toString());
        recursoPeriodicoService.getRepo().save(dto);
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
