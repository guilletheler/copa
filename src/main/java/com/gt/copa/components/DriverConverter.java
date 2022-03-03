package com.gt.copa.components;

import com.gt.copa.model.atemporal.Driver;
import com.gt.copa.repo.atemporal.DriverRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class DriverConverter extends StringConverter<Driver> {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    CurrentStatus currentStatus;

    @Override
    public String toString(Driver object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Driver fromString(String string) {
        if (currentStatus.getCopaStatus() == null || currentStatus.getCopaStatus().getEmpresa() == null
                || string == null || string.isEmpty()) {
            return null;
        }
        return driverRepo.findByEmpresaAndNombre(currentStatus.getCopaStatus().getEmpresa(), string).orElse(null);
    }

}
