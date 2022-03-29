package com.gt.copa.components;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import com.gt.copa.calc.engine.CopaCalculator;
import com.gt.copa.infra.CopaStatus;
import com.gt.copa.service.atemporal.EmpresaService;
import com.gt.copa.service.atemporal.EscenarioService;
import com.gt.copa.service.atemporal.TipoClasificacionDatoService;
import com.gt.copa.service.sistema.ParametroService;
import com.gt.copa.service.temporal.PeriodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class CurrentStatus {

    @Setter
    CopaStatus copaStatus;

    @Getter
    @Setter
    CopaCalculator copaCalculator;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    EscenarioService escenarioService;

    @Autowired
    PeriodoService periodoService;

    @Autowired
    ParametroService parametroService;

    @Autowired
    TipoClasificacionDatoService tipoClasificacionDatoService;

    @PostConstruct
    public void init() {
        this.copaStatus = new CopaStatus();
        int curEmpresaId = Integer.parseInt(parametroService.getValor("current.empresa.id", "0"));
        int curEscenarioId = Integer.parseInt(parametroService.getValor("current.escenario.id", "0"));
        int curPeriodoId = Integer.parseInt(parametroService.getValor("current.periodo.id", "0"));
        int curTipoClasificacionDatoId = Integer
                .parseInt(parametroService.getValor("current.tipoClasificacionDato.id", "0"));

        // Logger.getLogger(getClass().getName()).log(Level.INFO, "Cargando curStatus {0} {1} {2} {3}",
        //         new Object[] { curEmpresaId, curEscenarioId, curPeriodoId, curTipoClasificacionDatoId });

        this.copaStatus.setEmpresa(empresaService.getRepo().findById(curEmpresaId).orElse(null));
        this.copaStatus.setEscenario(escenarioService.getRepo().findById(curEscenarioId).orElse(null));
        this.copaStatus.setPeriodo(periodoService.getRepo().findById(curPeriodoId).orElse(null));
        this.copaStatus.setTipoClasificacion(
                tipoClasificacionDatoService.getRepo().findById(curTipoClasificacionDatoId).orElse(null));

    }

    public CopaStatus getCopaStatus() {
        return copaStatus;
    }

    public void saveInParams() {
        parametroService.setValor("current.empresa.id",
                this.copaStatus.getEmpresa() == null ? "0" : this.copaStatus.getEmpresa().getId().toString());
        parametroService.setValor("current.escenario.id",
                this.copaStatus.getEscenario() == null ? "0" : this.copaStatus.getEscenario().getId().toString());
        parametroService.setValor("current.periodo.id",
                this.copaStatus.getPeriodo() == null ? "0" : this.copaStatus.getPeriodo().getId().toString());
        parametroService.setValor("current.tipoClasificacionDato.id",
                this.copaStatus.getTipoClasificacion() == null ? "0"
                        : this.copaStatus.getTipoClasificacion().getId().toString());
    }
}
