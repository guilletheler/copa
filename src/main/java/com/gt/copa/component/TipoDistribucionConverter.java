package com.gt.copa.component;

import com.gt.copa.calc.api.TipoDistribucion;
import com.gt.copa.repo.atemporal.TipoClasificacionDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class TipoDistribucionConverter extends StringConverter<TipoDistribucion> {

    @Autowired
    TipoClasificacionDatoRepo tipoClasificacionDatoRepo;

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
