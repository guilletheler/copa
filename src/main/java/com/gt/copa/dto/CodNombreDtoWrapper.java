package com.gt.copa.dto;

import com.gt.copa.calc.api.ICodigoNombre;
import com.gt.copa.model.IWithId;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

@Data
public class CodNombreDtoWrapper<T extends ICodigoNombre & IWithId<Integer>> {

    T wrapped;

    IntegerProperty id;
    IntegerProperty codigo;
    StringProperty nombre;

    public CodNombreDtoWrapper(T wrapped) {
        this.wrapped = wrapped;

        if (wrapped.getId() != null) {
            id = new SimpleIntegerProperty(wrapped.getId());
        } else {
            id = new SimpleIntegerProperty();
        }
        if (wrapped.getCodigo() != null) {
            codigo = new SimpleIntegerProperty(wrapped.getCodigo());
        } else {
            codigo = new SimpleIntegerProperty();
        }
        if(wrapped.getNombre() != null) {
            nombre = new SimpleStringProperty(wrapped.getNombre());
        } else {
            nombre = new SimpleStringProperty();
        }
    }

    public T writeProps() {
        wrapped.setId(getId().get());
        wrapped.setCodigo(getCodigo().get());
        wrapped.setNombre(getNombre().get());

        return wrapped;
    }
}
