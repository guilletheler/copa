package com.gt.copa.component;

import com.gt.copa.model.atemporal.TipoClasificacionDato;
import com.gt.copa.repo.atemporal.TipoClasificacionDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class TipoClasificacionDatoConverter extends StringConverter<TipoClasificacionDato> {

    @Autowired
    TipoClasificacionDatoRepo tipoClasificacionDatoRepo;

    @Override
    public String toString(TipoClasificacionDato object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public TipoClasificacionDato fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return tipoClasificacionDatoRepo.findByNombre(string).orElse(null);
    }

}
