package com.gt.copa.component;

import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.repo.atemporal.ClasificacionDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class ClasificacionDatoConverter extends StringConverter<ClasificacionDato> {

    @Autowired
    ClasificacionDatoRepo clasificacionDatoRepo;

    @Override
    public String toString(ClasificacionDato object) {
        if (object == null) {
            return null;
        }
        return object.getTipoClasificacion().getNombre() + ":" + object.getNombre();
    }

    @Override
    public ClasificacionDato fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        String[] partes = string.split(":");

        return clasificacionDatoRepo.findByNombre(partes[1]).orElse(null);
    }

}
