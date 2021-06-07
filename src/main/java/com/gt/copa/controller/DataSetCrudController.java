package com.gt.copa.controller;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.model.SetDatos;
import com.gt.copa.repo.SetDatosRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("/com/gt/copa/view/DataSetCrudView.fxml")
public class DataSetCrudController {

    @Autowired
    SetDatosRepo setDatosRepo;

    @FXML
    private SplitPane nodeView;

    @FXML
    private TableView<SetDatos> tblDataSets;

    @FXML
    private TableColumn<SetDatos, Integer> colId;

    @FXML
    private TableColumn<SetDatos, Integer> colCodigo;

    @FXML
    private TableColumn<SetDatos, String> colNombre;

    @FXML
    void btnEliminarClick(ActionEvent event) {

    }

    @FXML
    void btnGuardarClick(ActionEvent event) {

    }

    @FXML
    void btnNuevoClick(ActionEvent event) {
        tblDataSets.getItems().add(new SetDatos());
    }

    @FXML
    public void initialize() {
        nodeView.setVisible(false);
        colId.setCellValueFactory(new PropertyValueFactory<SetDatos, Integer>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colCodigo.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {

            @Override
            public String toString(Integer object) {
                if(object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                if(string == null || string.isEmpty()) {
                    return null;
                }
                return Integer.valueOf(string);
            }

        }));
        colCodigo.setOnEditCommit((CellEditEvent<SetDatos, Integer> t) -> {
            ((SetDatos) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodigo(t.getNewValue());
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setCellFactory(TextFieldTableCell.<SetDatos>forTableColumn());
        colNombre.setOnEditCommit((CellEditEvent<SetDatos, String> t) -> {
            ((SetDatos) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNombre(t.getNewValue());
        });
    }

    public void loadData() {

        tblDataSets.setItems(FXCollections.observableArrayList(
                StreamSupport.stream(setDatosRepo.findAll().spliterator(), false).collect(Collectors.toList())));
    }

    public void persist() {
        tblDataSets.getItems().forEach(dto -> setDatosRepo.save(dto));
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
