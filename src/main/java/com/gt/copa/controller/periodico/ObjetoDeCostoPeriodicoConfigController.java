package com.gt.copa.controller.periodico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.components.ComponenteDriverConverter;
import com.gt.copa.components.CurrentStatus;
import com.gt.copa.components.TipoDistribucionConverter;
import com.gt.copa.controller.ModificadorDatos;
import com.gt.copa.infra.EditingTextCell;
import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ObjetoDeCostoPeriodico;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ProcesoRepo;
import com.gt.copa.service.atemporal.ComponenteDriverService;
import com.gt.copa.service.periodico.ObjetoDeCostoPeriodicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleObjectProperty;
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
@FxmlView("/com/gt/copa/view/periodico/ObjetoDeCostoPeriodicoConfigView.fxml")
public class ObjetoDeCostoPeriodicoConfigController implements ModificadorDatos {

    @Autowired
    ObjetoDeCostoPeriodicoService recursoPeriodicoService;

    @Autowired
    ProcesoRepo procesoRepo;

    @Autowired
    TipoDistribucionConverter tipoDistribucionConverter;

    @Autowired
    ComponenteDriverConverter componenteDriverConverter;

    @Autowired
    ComponenteDriverService componenteDriverService;

    @Autowired
    CurrentStatus currentStatus;

    @Getter
    @FXML
    private VBox nodeView;

    @FXML
    private TableView<ObjetoDeCostoPeriodico> tblObjetoDeCostosPeriodicas;

    @FXML
    private TableColumn<ObjetoDeCostoPeriodico, Integer> colId;

    @FXML
    private TableColumn<ObjetoDeCostoPeriodico, String> colObjetoDeCosto;

    @FXML
    private TableColumn<ObjetoDeCostoPeriodico, TipoDistribucion> colTipoDistribucion;

    @FXML
    private TableColumn<ObjetoDeCostoPeriodico, ComponenteDriver> colComponenteDriver;

    @FXML
    private TableColumn<ObjetoDeCostoPeriodico, Double> colValorParticular;

    @FXML
    private TableColumn<ObjetoDeCostoPeriodico, Boolean> colPromedioNoVacio;

    List<ObjetoDeCostoPeriodico> paraGuardar;

    @Getter
    boolean dataModificada;

    @FXML
    void btnEliminarClick(ActionEvent event) {
        if (tblObjetoDeCostosPeriodicas.getSelectionModel().getSelectedItems() != null) {
            tblObjetoDeCostosPeriodicas.getSelectionModel().getSelectedItems()
                    .forEach(ap -> ap.setTipoDistribucion(null));
            paraGuardar.addAll(tblObjetoDeCostosPeriodicas.getSelectionModel().getSelectedItems());
            dataModificada = true;
        }
    }

    @FXML
    void btnGuardarClick(ActionEvent event) {
        this.persist();
    }

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<ObjetoDeCostoPeriodico, Integer>("id"));

        colObjetoDeCosto.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ObjetoDeCostoPeriodico, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ObjetoDeCostoPeriodico, String> param) {
                        return new SimpleStringProperty(param.getValue().getObjetoDeCosto().getNombre());
                    }
                });

        colTipoDistribucion.setCellValueFactory(
                new PropertyValueFactory<ObjetoDeCostoPeriodico, TipoDistribucion>("tipoDistribucion"));

        colTipoDistribucion.setOnEditCommit((CellEditEvent<ObjetoDeCostoPeriodico, TipoDistribucion> t) -> {
            ((ObjetoDeCostoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                    .setTipoDistribucion(t.getNewValue());
            modificado(((ObjetoDeCostoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        ObservableList<TipoDistribucion> tiposDistribucionValues = FXCollections
                .observableArrayList(TipoDistribucion.values());
        tiposDistribucionValues.add(0, null);

        Callback<TableColumn<ObjetoDeCostoPeriodico, TipoDistribucion>, TableCell<ObjetoDeCostoPeriodico, TipoDistribucion>> tipoDistribucionCellFactory = ComboBoxTableCell
                .forTableColumn(tipoDistribucionConverter, tiposDistribucionValues);

        colTipoDistribucion.setCellFactory(tipoDistribucionCellFactory);

        colComponenteDriver.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ObjetoDeCostoPeriodico, ComponenteDriver>, ObservableValue<ComponenteDriver>>() {

                    @Override
                    public ObservableValue<ComponenteDriver> call(
                            TableColumn.CellDataFeatures<ObjetoDeCostoPeriodico, ComponenteDriver> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getComponenteDriver());
                    }
                });

        colComponenteDriver.setOnEditCommit((CellEditEvent<ObjetoDeCostoPeriodico, ComponenteDriver> t) -> {
            ((ObjetoDeCostoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())).getValorAsignado()
                    .setComponenteDriver(t.getNewValue());
            modificado(((ObjetoDeCostoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });

        colValorParticular.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ObjetoDeCostoPeriodico, Double>, ObservableValue<Double>>() {

                    @Override
                    public ObservableValue<Double> call(
                            TableColumn.CellDataFeatures<ObjetoDeCostoPeriodico, Double> param) {

                        return new SimpleObjectProperty<>(param.getValue().getValorAsignado().getValorParticular());
                    }
                });
        colValorParticular.setCellFactory(EditingTextCell.doubleCellFactory());
        colValorParticular.setOnEditCommit((CellEditEvent<ObjetoDeCostoPeriodico, Double> t) -> {
            ((ObjetoDeCostoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())).getValorAsignado()
                    .setValorParticular(t.getNewValue());
            modificado(((ObjetoDeCostoPeriodico) t.getTableView().getItems().get(t.getTablePosition().getRow())));
        });
    }

    private void modificado(ObjetoDeCostoPeriodico recurso) {
        if (!paraGuardar.contains(recurso)) {
            paraGuardar.add(recurso);
        }
        dataModificada = true;
    }

    public void loadData() {

        Empresa empresa = currentStatus.getCopaStatus().getEmpresa();
        Escenario escenario = currentStatus.getCopaStatus().getEscenario();
        Periodo periodo = currentStatus.getCopaStatus().getPeriodo();

        ObservableList<ComponenteDriver> componentesDrivers = FXCollections
                .observableArrayList(componenteDriverService.findByEmpresa(empresa));
        componentesDrivers.add(0, null);

        Callback<TableColumn<ObjetoDeCostoPeriodico, ComponenteDriver>, TableCell<ObjetoDeCostoPeriodico, ComponenteDriver>> componenteDriverCellFactory = ComboBoxTableCell
                .forTableColumn(componenteDriverConverter, componentesDrivers);

        colComponenteDriver.setCellFactory(componenteDriverCellFactory);

        tblObjetoDeCostosPeriodicas.setItems(
                FXCollections.observableArrayList(recursoPeriodicoService.findOrCreate(empresa, escenario, periodo)));
        paraGuardar = new ArrayList<>();
        dataModificada = false;
    }

    public void persist() {

        List<ObjetoDeCostoPeriodico> realParaGuardar = paraGuardar.stream()
                .filter(ap -> ap.getTipoDistribucion() != null).collect(Collectors.toList());
        List<ObjetoDeCostoPeriodico> paraEliminar = paraGuardar.stream().filter(ap -> ap.getTipoDistribucion() == null)
                .collect(Collectors.toList());

        realParaGuardar.forEach(dto -> guardar(dto));
        paraEliminar.stream().filter(ds -> ds.getId() != null).map(ds -> ds.getId()).distinct()
                .forEach(id -> recursoPeriodicoService.getRepo().deleteById(id));
        this.loadData();
    }

    private void guardar(ObjetoDeCostoPeriodico dto) {
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
