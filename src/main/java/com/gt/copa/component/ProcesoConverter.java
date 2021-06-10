package com.gt.copa.component;

import com.gt.copa.model.atemporal.Proceso;
import com.gt.copa.repo.atemporal.ProcesoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class ProcesoConverter extends StringConverter<Proceso> {

    @Autowired
    ProcesoRepo procesoRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Override
    public String toString(Proceso object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Proceso fromString(String string) {
        if (currentStatus.getCopaStatus() == null || currentStatus.getCopaStatus().getEmpresa() == null
                || string == null || string.isEmpty()) {
            return null;
        }
        return procesoRepo.findByEmpresaAndNombre(currentStatus.getCopaStatus().getEmpresa(), string).orElse(null);
    }

}
