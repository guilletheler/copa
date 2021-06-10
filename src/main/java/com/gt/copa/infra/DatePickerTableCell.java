package com.gt.copa.infra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
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
public class DatePickerTableCell<T> extends TableCell<T, Date> {
    private DatePicker datePicker;

    @Getter
    @Setter
    private StringConverter<Date> dateStringConverter;

    @Getter
    @Setter
    private StringConverter<LocalDate> stringConverter;

    public DatePickerTableCell() {
    }

    public DatePickerTableCell(String pattern) {
        
        dateStringConverter = new StringConverter<Date>(){

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            @Override
            public String toString(Date object) {
                if(object == null) {
                    return "";
                }
                return sdf.format(object);
            }

            @Override
            public Date fromString(String string) {
                if(string == null || string.isEmpty()) {
                    return null;
                }
                try {
                    return sdf.parse(string);
                } catch (ParseException e) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Error al convertir '" + string + "' en fecha");
                };
                return null;
            }
            
        };

        stringConverter = new StringConverter<LocalDate>(){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate object) {
                if(object == null) {
                    return "";
                }
                return formatter.format(object);
            }

            @Override
            public LocalDate fromString(String string) {
                if(string == null || string.isEmpty()) {
                    return null;
                }

                return LocalDate.parse(string, formatter);
            }
            
        };
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (datePicker == null) {
            createTextField();
        }
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                datePicker.requestFocus();
            }
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDateStringConverter().toString(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    LocalDate localDate = Instant.ofEpochMilli(item.getTime()).atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    datePicker.setValue(localDate);
                }
                setGraphic(datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createTextField() {
        LocalDate localDate = Instant.ofEpochMilli(getItem().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker = new DatePicker(localDate);
        
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    LocalDate localDate = datePicker.getValue();

                    Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                    commitEdit(date);
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (t.getCode() == KeyCode.TAB) {
                    LocalDate localDate = datePicker.getValue();

                    Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                    commitEdit(date);
                    TableColumn<T, ?> nextColumn = getNextColumn(!t.isShiftDown());
                    if (nextColumn != null) {
                        getTableView().edit(getTableRow().getIndex(), nextColumn);
                    }
                }
            }
        });
        datePicker.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue && datePicker != null) {
                    LocalDate localDate = datePicker.getValue();

                    Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                    commitEdit(date);
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getDateStringConverter().toString(getItem());
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

    public static <T> Callback<TableColumn<T, Date>, TableCell<T, Date>> dateCellFactory(String pattern) {

        Callback<TableColumn<T, Date>, TableCell<T, Date>> dateCellFactory = new Callback<TableColumn<T, Date>, TableCell<T, Date>>() {
            @Override
            public TableCell<T, Date> call(TableColumn<T, Date> p) {
                return new DatePickerTableCell<T>(pattern);
            }
        };

        return dateCellFactory;
    }

}