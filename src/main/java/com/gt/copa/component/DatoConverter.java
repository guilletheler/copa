package com.gt.copa.component;

import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.repo.atemporal.DatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class DatoConverter extends StringConverter<Dato> {

    @Autowired
    DatoRepo DatoRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Override
    public String toString(Dato object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Dato fromString(String string) {
        if (currentStatus.getCopaStatus() == null || currentStatus.getCopaStatus().getEmpresa() == null
                || string == null || string.isEmpty()) {
            return null;
        }
        return DatoRepo.findByRecurso_EmpresaAndNombreOrderByNombre(currentStatus.getCopaStatus().getEmpresa(), string).orElse(null);
    }

}
