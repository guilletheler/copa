package com.gt.copa.service.periodico;

import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.repo.periodico.RecursoEnActividadRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class RecursoEnActividadService {

    @Getter
    @Autowired
    RecursoEnActividadRepo repo;

    @Autowired
    RecursoRepo actividadRepo;

}
