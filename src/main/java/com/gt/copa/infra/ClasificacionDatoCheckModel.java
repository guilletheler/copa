package com.gt.copa.infra;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.model.atemporal.ClasificacionDato;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.IndexedCheckModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClasificacionDatoCheckModel implements IndexedCheckModel<ClasificacionDato> {

    List<Pair<ClasificacionDato, Boolean>> items;

    public ClasificacionDatoCheckModel(Iterable<ClasificacionDato> items) {
        this.items = StreamSupport.stream(items.spliterator(), false).map(item -> new MutablePair<>(item, false))
                .collect(Collectors.toList());
    }

    public ClasificacionDatoCheckModel(Collection<ClasificacionDato> items) {
        this.items = items.stream().map(item -> new MutablePair<>(item, false)).collect(Collectors.toList());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public ObservableList<ClasificacionDato> getCheckedItems() {
        return FXCollections.observableArrayList(items.stream().filter(entry -> entry.getValue())
                .map(entry -> entry.getKey()).collect(Collectors.toList()));
    }

    @Override
    public void checkAll() {
        items.forEach(entry -> entry.setValue(true));
    }

    @Override
    public void clearCheck(ClasificacionDato item) {
        items.stream().filter(entry -> entry.getKey().equals(item)).findAny().orElse(null).setValue(false);
    }

    @Override
    public void clearChecks() {
        items.forEach(entry -> entry.setValue(false));
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean isChecked(ClasificacionDato item) {
        return items.stream().filter(entry -> entry.getKey().equals(item)).findAny().orElse(null).getValue();
    }

    @Override
    public void check(ClasificacionDato item) {
        items.stream().filter(entry -> entry.getKey().equals(item)).findAny().orElse(null).setValue(true);
    }

    @Override
    public void toggleCheckState(ClasificacionDato item) {
        Pair<ClasificacionDato, Boolean> entry = items.stream().filter(e -> e.getKey().equals(item)).findAny()
                .orElse(null);
        entry.setValue(!entry.getValue());
    }

    @Override
    public ClasificacionDato getItem(int index) {
        return items.get(index).getKey();
    }

    @Override
    public int getItemIndex(ClasificacionDato item) {
        Pair<ClasificacionDato, Boolean> entry = items.stream().filter(e -> e.getKey().equals(item)).findAny()
                .orElse(null);
        return items.indexOf(entry);
    }

    @Override
    public ObservableList<Integer> getCheckedIndices() {
        return FXCollections.observableArrayList(items.stream().filter(entry -> entry.getValue())
                .map(entry -> items.indexOf(entry)).collect(Collectors.toList()));
    }

    @Override
    public void checkIndices(int... indices) {
        for (int i : indices) {
            check(getItem(i));
        }
    }

    @Override
    public void clearCheck(int index) {
        items.get(index).setValue(false);
    }

    @Override
    public boolean isChecked(int index) {
        return items.get(index).getValue();
    }

    @Override
    public void check(int index) {
        items.get(index).setValue(true);
    }

    @Override
    public void toggleCheckState(int index) {
        items.get(index).setValue(!items.get(index).getValue());
    }

}
