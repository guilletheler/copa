package com.gt.copa.component;

import com.gt.copa.calc.api.TratamientoMuestra;

import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class TratamientoMuestraConverter extends StringConverter<TratamientoMuestra> {

    @Override
    public String toString(TratamientoMuestra object) {
        if (object == null) {
            return "";
        }
        return object.name();
    }

    @Override
    public TratamientoMuestra fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return TratamientoMuestra.valueOf(string);
    }

}
