package com.gt.copa.components;

import java.util.List;

import javafx.util.StringConverter;

public class EntityConverter<T> extends StringConverter<T> {

    List<T> entities;

    public EntityConverter(List<T> entities) {
        this.entities = entities;
    }

    @Override
    public T fromString(String arg0) {
        return entities.stream().filter(t -> t.toString().equals(arg0)).findFirst().orElse(null);
    }

    @Override
    public String toString(T arg0) {
        return arg0.toString();
    }
    
}
