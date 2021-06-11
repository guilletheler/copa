package com.gt.copa.component;

import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.repo.atemporal.ComponenteDriverRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class ComponenteDriverConverter extends StringConverter<ComponenteDriver> {

    @Autowired
    ComponenteDriverRepo componenteDriverRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Override
    public String toString(ComponenteDriver object) {
        if (object == null) {
            return "";
        }
        return object.getDriver().getNombre() + "->" + object.getNombre();
    }

    @Override
    public ComponenteDriver fromString(String string) {
        if (currentStatus.getCopaStatus() == null || currentStatus.getCopaStatus().getEmpresa() == null
                || string == null || string.isEmpty()) {
            return null;
        }

        String nombres[] = string.split("->");

        return componenteDriverRepo.findByDriver_EmpresaAndDriver_NombreAndNombre(currentStatus.getCopaStatus().getEmpresa(), nombres[0], nombres[1]).orElse(null);
    }

}
