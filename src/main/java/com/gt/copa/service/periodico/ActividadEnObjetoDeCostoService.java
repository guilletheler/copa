package com.gt.copa.service.periodico;

import com.gt.copa.repo.periodico.ActividadEnObjetoDeCostoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class ActividadEnObjetoDeCostoService {

    @Getter
    @Autowired
    ActividadEnObjetoDeCostoRepo repo;

}
