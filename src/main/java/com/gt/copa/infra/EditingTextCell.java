package com.gt.copa.infra;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gt.copa.Utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class EditingTextCell<T, V> extends TableCell<T, V> {
    private ValidableTextField textField;

    @Getter
    @Setter
    private StringConverter<V> stringConverter;
    
    @Getter
    @Setter
    ITextFieldValidator textFieldValidator = null;

    public EditingTextCell() {
    }

    public EditingTextCell(StringConverter<V> stringConverter) {
        this.stringConverter = stringConverter;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null) {
            createValidableTextField();
        }
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textField.requestFocus();
                textField.selectAll();
            }
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getStringConverter().toString(getItem()));
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
                if (textField != null) {
                    textField.setText(getString());
                }
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createValidableTextField() {
        textField = new ValidableTextField(textFieldValidator, getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(getStringConverter().fromString(textField.getText()));
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (t.getCode() == KeyCode.TAB) {
                    commitEdit(getStringConverter().fromString(textField.getText()));
                    TableColumn<T, ?> nextColumn = getNextColumn(!t.isShiftDown());
                    if (nextColumn != null) {
                        getTableView().edit(getTableRow().getIndex(), nextColumn);
                    }
                }
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue && textField != null) {
                    commitEdit(getStringConverter().fromString(textField.getText()));
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getStringConverter().toString(getItem());
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

    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> stringCellFactory() {

        StringConverter<String> stringConverter = new StringConverter<String>() {

            @Override
            public String toString(String object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public String fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return string;
            }

        };

        Callback<TableColumn<T, String>, TableCell<T, String>> stringCellFactory = new Callback<TableColumn<T, String>, TableCell<T, String>>() {
            @Override
            public TableCell<T, String> call(TableColumn<T, String> p) {
                return new EditingTextCell<T, String>(stringConverter);
            }
        };

        return stringCellFactory;
    }

    public static <T> Callback<TableColumn<T, Integer>, TableCell<T, Integer>> integerCellFactory() {

        StringConverter<Integer> integerConverter = new StringConverter<Integer>() {

            @Override
            public String toString(Integer object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return Integer.valueOf(string);
            }

        };

        Callback<TableColumn<T, Integer>, TableCell<T, Integer>> integerCellFactory = new Callback<TableColumn<T, Integer>, TableCell<T, Integer>>() {
            @Override
            public TableCell<T, Integer> call(TableColumn<T, Integer> p) {
                EditingTextCell<T, Integer> ret = new EditingTextCell<>(integerConverter);
                ret.setTextFieldValidator(new IntegerTextFieldValidator());
                return ret;
            }
        };

        return integerCellFactory;
    }

    public static <T> Callback<TableColumn<T, Double>, TableCell<T, Double>> doubleCellFactory() {

        StringConverter<Double> doubleConverter = new StringConverter<Double>() {

            @Override
            public String toString(Double object) {
                if (object == null) {
                    return "";
                }
                return object.toString();
            }

            @Override
            public Double fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return Double.valueOf(string);
            }

        };

        Callback<TableColumn<T, Double>, TableCell<T, Double>> doubleCellFactory = new Callback<TableColumn<T, Double>, TableCell<T, Double>>() {
            @Override
            public TableCell<T, Double> call(TableColumn<T, Double> p) {
                EditingTextCell<T, Double> ret = new EditingTextCell<>(doubleConverter);
                ret.setTextFieldValidator(new DoubleTextFieldValidator());
                return ret;
            }
        };

        return doubleCellFactory;
    }

    public static <T> Callback<TableColumn<T, Date>, TableCell<T, Date>> dateCellFactory() {

        StringConverter<Date> dateConverter = new StringConverter<Date>() {

            @Override
            public String toString(Date object) {
                if (object == null) {
                    return "";
                }
                return Utils.SDF_SLASH_DMYY.format(object);
            }

            @Override
            public Date fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                try {
                    return Utils.SDF_SLASH_DMYY.parse(string);
                } catch (ParseException e) {
                    Logger.getLogger(getClass().getName()).log(Level.WARNING,
                            "Error al convertir " + string + " en fecha");
                }
                return null;
            }

        };

        Callback<TableColumn<T, Date>, TableCell<T, Date>> dateCellFactory = new Callback<TableColumn<T, Date>, TableCell<T, Date>>() {
            @Override
            public TableCell<T, Date> call(TableColumn<T, Date> p) {
                return new EditingTextCell<T, Date>(dateConverter);
            }
        };

        return dateCellFactory;
    }

}