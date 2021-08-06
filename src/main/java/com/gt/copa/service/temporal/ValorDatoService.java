package com.gt.copa.service.temporal;

import com.gt.copa.repo.temporal.ValorDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class ValorDatoService {

    @Getter
    @Autowired
    ValorDatoRepo valorDatoRepo;

}
