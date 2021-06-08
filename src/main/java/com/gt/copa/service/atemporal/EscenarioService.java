package com.gt.copa.service.atemporal;

import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.repo.atemporal.EscenarioRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EscenarioService {
    
    @Autowired
    EscenarioRepo repo;

    public void checkDefaults() {
        
        if(repo.count() == 0) {
            Escenario e = new Escenario();
            e.setCodigo(1);
            e.setNombre("Proyectado");
            repo.save(e);

            e = new Escenario();
            e.setCodigo(2);
            e.setNombre("Resultante");
            repo.save(e);
        }

    }
}
