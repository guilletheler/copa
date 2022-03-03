package com.gt.copa.components;

import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.repo.atemporal.EscenarioRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class EscenarioConverter extends StringConverter<Escenario> {

    @Autowired
    EscenarioRepo escenarioRepo;

    @Override
    public String toString(Escenario object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Escenario fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return escenarioRepo.findByNombre(string).orElse(null);
    }

}
