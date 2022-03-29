package com.gt.copa.service.atemporal;

import com.gt.copa.repo.atemporal.EmpresaRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class EmpresaService {
    
    @Getter
    @Autowired
    EmpresaRepo repo;

}
