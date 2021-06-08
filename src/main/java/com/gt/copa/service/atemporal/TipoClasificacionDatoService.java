package com.gt.copa.service.atemporal;

import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.TipoClasificacionDato;
import com.gt.copa.repo.atemporal.TipoClasificacionDatoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoClasificacionDatoService {
    
    @Autowired
    TipoClasificacionDatoRepo repo;

    public void checkDefaults() {
        
        if(repo.count() == 0) {
            TipoClasificacionDato rela = new TipoClasificacionDato();
            rela.setCodigo(1);
            rela.setNombre("Relacion Actividad");

            rela.getClasificaciones().add(new ClasificacionDato(rela, 1, "Fijo"));
            rela.getClasificaciones().add(new ClasificacionDato(rela, 2, "Variable"));

            TipoClasificacionDato relp = new TipoClasificacionDato();
            relp.setCodigo(2);
            relp.setNombre("Relacion Pago");

            relp.getClasificaciones().add(new ClasificacionDato(relp, 3, "Erogable"));
            relp.getClasificaciones().add(new ClasificacionDato(relp, 4, "No erogable"));

            repo.save(rela);
            repo.save(relp);
        }

    }
}
