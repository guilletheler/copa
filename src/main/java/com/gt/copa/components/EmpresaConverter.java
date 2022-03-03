package com.gt.copa.components;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.repo.atemporal.EmpresaRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class EmpresaConverter extends StringConverter<Empresa> {

    @Autowired
    EmpresaRepo empresaRepo;

    @Override
    public String toString(Empresa object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Empresa fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return empresaRepo.findByNombre(string).orElse(null);
    }

}
