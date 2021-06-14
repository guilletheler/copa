package com.gt.copa.service.atemporal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.repo.atemporal.ActividadRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class ActividadService {
    
    @Getter
    @Autowired
    ActividadRepo repo;

    @Transactional
    public List<Actividad> findByEmpresa(Empresa empresa) {
        List<Actividad> ret = StreamSupport.stream(repo.findByProceso_EmpresaOrderByNombre(empresa).spliterator(), false).collect(Collectors.toList());

        return ret;
    }
}
