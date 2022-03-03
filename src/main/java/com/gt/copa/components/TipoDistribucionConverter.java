package com.gt.copa.components;

import com.gt.copa.calc.api.TipoDistribucion;

import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class TipoDistribucionConverter extends StringConverter<TipoDistribucion> {

    @Override
    public String toString(TipoDistribucion object) {
        if (object == null) {
            return "";
        }
        return object.name();
    }
    
    @Override
    public TipoDistribucion fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return TipoDistribucion.valueOf(string);
    }

}
