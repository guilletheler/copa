package com.gt.copa.infra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
public class CheckComboBoxTableCell<T, V> extends TableCell<T, Collection<V>> {
    private CheckComboBox<V> checkComboBox;

    @Getter
    @Setter
    private StringConverter<Collection<V>> collectionConverter;

    @Getter
    private StringConverter<V> valueConverter;

    @Getter
    @Setter
    ObservableList<V> items;

    public CheckComboBoxTableCell(Collection<V> items) {
        this.items = FXCollections.observableArrayList(items);
    }

    public CheckComboBoxTableCell(Collection<V> items, StringConverter<V> stringConverter) {
        setValueConverter(stringConverter);
        this.items = FXCollections.observableArrayList(items);
    }

    private void setValueConverter(StringConverter<V> converter) {

        valueConverter = converter;

        if (collectionConverter == null && converter != null) {
            collectionConverter = new StringConverter<Collection<V>>() {

                @Override
                public String toString(Collection<V> object) {
                    if (object == null || object.isEmpty()) {
                        return "";
                    }
                    return object.stream().map(obj -> converter.toString(obj)).collect(Collectors.joining(", "));
                }

                @Override
                public Collection<V> fromString(String string) {
                    ArrayList<V> lst = new ArrayList<V>();
                    if (string != null && !string.isEmpty()) {
                        for (String strItem : string.split(", ")) {
                            lst.add(converter.fromString(strItem));
                        }
                    }

                    return lst;
                }

            };
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (checkComboBox == null) {
            createTextField();
        }
        setGraphic(checkComboBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkComboBox.requestFocus();
            }
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getCollectionConverter().toString(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(Collection<V> item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (checkComboBox != null) {
                    checkComboBox.getCheckModel().clearChecks();
                    for (V vItem : getCollectionConverter().fromString(getString())) {
                        checkComboBox.getCheckModel().check(vItem);
                    }

                }
                setGraphic(checkComboBox);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createTextField() {
        checkComboBox = new CheckComboBox<>(items);
        checkComboBox.setConverter(getValueConverter());
        for (V vItem : getCollectionConverter().fromString(getString())) {
            checkComboBox.getCheckModel().check(vItem);
        }
        checkComboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        checkComboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(checkComboBox.getCheckModel().getCheckedItems());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (t.getCode() == KeyCode.TAB) {
                    commitEdit(checkComboBox.getCheckModel().getCheckedItems());
                    TableColumn<T, ?> nextColumn = getNextColumn(!t.isShiftDown());
                    if (nextColumn != null) {
                        getTableView().edit(getTableRow().getIndex(), nextColumn);
                    }
                }
            }
        });
        checkComboBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue && checkComboBox != null) {
                    commitEdit(checkComboBox.getCheckModel().getCheckedItems());
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getCollectionConverter().toString(getItem());
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

    public static <T, V> Callback<TableColumn<T, Collection<V>>, TableCell<T, Collection<V>>> checkComboCellFactory(
            Collection<V> items, StringConverter<V> converter) {

        Callback<TableColumn<T, Collection<V>>, TableCell<T, Collection<V>>> comboCellFactory = new Callback<TableColumn<T, Collection<V>>, TableCell<T, Collection<V>>>() {
            @Override
            public TableCell<T, Collection<V>> call(TableColumn<T, Collection<V>> p) {
                return new CheckComboBoxTableCell<>(items, converter);
            }
        };

        return comboCellFactory;
    }
}