package com.gt.copa.service.atemporal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.repo.atemporal.DatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class DatoService {
    
    @Getter
    @Autowired
    DatoRepo repo;

    @Transactional
    public List<Dato> findByEmpresa(Empresa empresa) {
        List<Dato> ret = StreamSupport.stream(repo.findByRecurso_Empresa(empresa).spliterator(), false).collect(Collectors.toList());

        ret.forEach(dato -> dato.getClasificaciones().size());

        return ret;
    }
}
