package com.gt.copa.service.atemporal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.repo.atemporal.ComponenteDriverRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class ComponenteDriverService {
    
    @Getter
    @Autowired
    ComponenteDriverRepo repo;

    @Transactional
    public List<ComponenteDriver> findByEmpresa(Empresa empresa) {
        List<ComponenteDriver> ret = StreamSupport.stream(repo.findByDriver_EmpresaOrderByNombre(empresa).spliterator(), false).collect(Collectors.toList());

        return ret;
    }
}
