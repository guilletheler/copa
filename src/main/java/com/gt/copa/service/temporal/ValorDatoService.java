package com.gt.copa.service.temporal;

import java.util.List;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.model.temporal.ValorDato;
import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.repo.temporal.ValorDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class ValorDatoService {

    @Getter
    @Autowired
    ValorDatoRepo repo;

    @Autowired
    RecursoRepo actividadRepo;

    @Transactional
    public List<ValorDato> findOrCreate(Empresa empresa, Escenario escenario, Periodo periodo) {
        return repo.findByDato_Recurso_EmpresaAndEscenarioAndFechaGreaterThanEqualAndFechaLessThanEqual(empresa, escenario, periodo.getInicio(), periodo.getFin());
    }

}
