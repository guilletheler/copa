package com.gt.copa.service.sistema;

import com.gt.copa.model.sistema.Parametro;
import com.gt.copa.repo.sistema.ParametroRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParametroService {

    @Autowired
    ParametroRepo repo;

    public String getValor(String nombre) {
        return repo.findByNombre(nombre).map(p -> p.getValor()).orElse(null);
    }

    public String getValor(String nombre, String defaultValue) {
        String ret = repo.findByNombre(nombre).map(p -> p.getValor()).orElse(null);
        if (ret == null) {
            ret = setValor(nombre, defaultValue).getValor();
        }
        return ret;
    }

    public Parametro setValor(String nombre, String valor) {
        Parametro p = repo.findByNombre(nombre).orElse(null);
        if (p == null) {
            p = new Parametro();
            p.setNombre(nombre);
        }
        p.setValor(valor);
        repo.save(p);
        return p;
    }
}
