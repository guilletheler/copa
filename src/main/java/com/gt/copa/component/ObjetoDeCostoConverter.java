package com.gt.copa.component;

import com.gt.copa.model.atemporal.ObjetoDeCosto;
import com.gt.copa.repo.atemporal.ObjetoDeCostoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class ObjetoDeCostoConverter extends StringConverter<ObjetoDeCosto> {

    @Autowired
    ObjetoDeCostoRepo objetoDeCostoRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Override
    public String toString(ObjetoDeCosto object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public ObjetoDeCosto fromString(String string) {
        if (currentStatus.getCopaStatus() == null || currentStatus.getCopaStatus().getEmpresa() == null
                || string == null || string.isEmpty()) {
            return null;
        }
        return objetoDeCostoRepo.findByEmpresaAndNombre(currentStatus.getCopaStatus().getEmpresa(), string).orElse(null);
    }

}
