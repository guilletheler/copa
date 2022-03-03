package com.gt.copa.components;

import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.repo.atemporal.ActividadRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class ActividadConverter extends StringConverter<Actividad> {

    @Autowired
    ActividadRepo ActividadRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Override
    public String toString(Actividad object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Actividad fromString(String string) {
        if (currentStatus.getCopaStatus() == null || currentStatus.getCopaStatus().getEmpresa() == null
                || string == null || string.isEmpty()) {
            return null;
        }
        return ActividadRepo.findByProceso_EmpresaAndNombre(currentStatus.getCopaStatus().getEmpresa(), string).orElse(null);
    }

}
