package com.gt.copa.components;

import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.temporal.PeriodoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.util.StringConverter;

@Component
public class PeriodoConverter extends StringConverter<Periodo> {

    @Autowired
    PeriodoRepo periodoRepo;

    @Override
    public String toString(Periodo object) {
        if (object == null) {
            return "";
        }
        return object.getNombre();
    }

    @Override
    public Periodo fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        return periodoRepo.findByNombre(string).orElse(null);
    }

}
