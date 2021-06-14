package com.gt.copa.infra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.controlsfx.control.SearchableComboBox;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Graham Smith
 */
public class SearchableComboBoxTableCell<T, V> extends TableCell<T, V> {
    private SearchableComboBox<V> searchableComboBox;

    @Getter
    @Setter
    private StringConverter<V> converter;

    @Getter
    @Setter
    ObservableList<V> items;

    public SearchableComboBoxTableCell(Collection<V> items) {
        if(items instanceof ObservableList) {
            this.items = (ObservableList<V>) items;
        } else {
            this.items = FXCollections.observableArrayList(items);
        }
    }

    public SearchableComboBoxTableCell(Collection<V> items, StringConverter<V> converter) {
        this(items);
        this.converter = converter;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (searchableComboBox == null) {
            createTextField();
        }
        setGraphic(searchableComboBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchableComboBox.requestFocus();
            }
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getConverter().toString(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(V item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (searchableComboBox != null) {
                    searchableComboBox.getSelectionModel().select(getItem());

                }
                setGraphic(searchableComboBox);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createTextField() {
        searchableComboBox = new SearchableComboBox<>();
        
        searchableComboBox.setItems(items);
        
        searchableComboBox.setCellFactory(ComboBoxListCell.forListView(getConverter(), items));

        searchableComboBox.setConverter(getConverter());
        
        searchableComboBox.getSelectionModel().select(getConverter().fromString(getString()));
        
        searchableComboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        searchableComboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(searchableComboBox.getSelectionModel().getSelectedItem());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (t.getCode() == KeyCode.TAB) {
                    commitEdit(searchableComboBox.getSelectionModel().getSelectedItem());
                    TableColumn<T, ?> nextColumn = getNextColumn(!t.isShiftDown());
                    if (nextColumn != null) {
                        getTableView().edit(getTableRow().getIndex(), nextColumn);
                    }
                }
            }
        });
        searchableComboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue && searchableComboBox != null) {
                    commitEdit(searchableComboBox.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getConverter().toString(getItem());
    }

    /**
     *
     * @param forward true gets the column to the right, false the column to the
     *                left of the current column
     * @return
     */
    private TableColumn<T, ?> getNextColumn(boolean forward) {
        List<TableColumn<T, ?>> columns = new ArrayList<>();
        for (TableColumn<T, ?> column : getTableView().getColumns()) {
            columns.addAll(getLeaves(column));
        }
        // There is no other column that supports editing.
        if (columns.size() < 2) {
            return null;
        }
        int currentIndex = columns.indexOf(getTableColumn());
        int nextIndex = currentIndex;
        if (forward) {
            nextIndex++;
            if (nextIndex > columns.size() - 1) {
                nextIndex = 0;
            }
        } else {
            nextIndex--;
            if (nextIndex < 0) {
                nextIndex = columns.size() - 1;
            }
        }
        return columns.get(nextIndex);
    }

    private List<TableColumn<T, ?>> getLeaves(TableColumn<T, ?> root) {
        List<TableColumn<T, ?>> columns = new ArrayList<>();
        if (root.getColumns().isEmpty()) {
            // We only want the leaves that are editable.
            if (root.isEditable()) {
                columns.add(root);
            }
            return columns;
        } else {
            for (TableColumn<T, ?> column : root.getColumns()) {
                columns.addAll(getLeaves(column));
            }
            return columns;
        }
    }

    public static <T, V> Callback<TableColumn<T, V>, TableCell<T, V>> searchableComboCellFactory(
            Collection<V> items, StringConverter<V> converter) {

        Callback<TableColumn<T, V>, TableCell<T,V>> searchableCellFactory = new Callback<TableColumn<T, V>, TableCell<T, V>>() {
            @Override
            public TableCell<T, V> call(TableColumn<T, V> p) {
                return new SearchableComboBoxTableCell<>(items, converter);
            }
        };

        return searchableCellFactory;
    }
}