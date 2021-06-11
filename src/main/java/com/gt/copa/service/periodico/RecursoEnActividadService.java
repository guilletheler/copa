package com.gt.copa.service.periodico;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.gt.copa.model.atemporal.Recurso;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.RecursoPeriodico;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.repo.periodico.RecursoEnActividadRepo;
import com.gt.copa.repo.periodico.RecursoPeriodicoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class RecursoEnActividadService {

    @Getter
    @Autowired
    RecursoEnActividadRepo repo;

    @Autowired
    RecursoRepo actividadRepo;

}
