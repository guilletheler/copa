package com.gt.copa.component;

import com.gt.copa.model.atemporal.Recurso;
import com.gt.copa.repo.atemporal.RecursoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class RecursoConverter extends StringConverter<Recurso> {

    @Autowired
    RecursoRepo recursoRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Override
    public String toString(Recurso object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Recurso fromString(String string) {
        if (currentStatus.getCopaStatus() == null || currentStatus.getCopaStatus().getEmpresa() == null
                || string == null || string.isEmpty()) {
            return null;
        }
        return recursoRepo.findByEmpresaAndNombre(currentStatus.getCopaStatus().getEmpresa(), string).orElse(null);
    }

}
