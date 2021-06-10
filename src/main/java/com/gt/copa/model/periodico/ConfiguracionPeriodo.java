package com.gt.copa.model.periodico;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.temporal.Periodo;

import lombok.Data;

@Embeddable
@Data
public class ConfiguracionPeriodo {

    @ManyToOne
    Periodo periodo;
    
	@ManyToOne
	Empresa empresa;

    @ManyToOne
    Escenario escenario;
}
