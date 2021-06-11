package com.gt.copa.service.periodico;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ActividadPeriodica;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ActividadRepo;
import com.gt.copa.repo.periodico.ActividadPeriodicaRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class ActividadPeriodicaService {

    @Getter
    @Autowired
    ActividadPeriodicaRepo repo;

    @Autowired
    ActividadRepo actividadRepo;

    @Transactional
    public List<ActividadPeriodica> findOrCreate(Empresa empresa, Escenario escenario, Periodo periodo) {
        List<Actividad> actividades = StreamSupport
                .stream(actividadRepo.findByProceso_Empresa(empresa).spliterator(), false).collect(Collectors.toList());

        List<ActividadPeriodica> lst = repo
                .findByActividad_Proceso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
                        empresa, escenario, periodo);

        List<Actividad> existen = lst.stream().map(ap -> ap.getActividad()).collect(Collectors.toList());

        List<Actividad> noExisten = actividades.stream().filter(act -> !existen.contains(act))
                .collect(Collectors.toList());

        List<ActividadPeriodica> nuevas = noExisten.stream().map(a -> createActividadPeriodica(a, escenario, periodo)).collect(Collectors.toList());

        return Stream.concat(lst.stream(),  nuevas.stream()).collect(Collectors.toList());
    }

    private ActividadPeriodica createActividadPeriodica(Actividad a, Escenario escenario, Periodo periodo) {
        ActividadPeriodica ret = new ActividadPeriodica();
        ret.setActividad(a);
        ret.setConfiguracionPeriodo(ConfiguracionPeriodo.builder().escenario(escenario).periodo(periodo).build());
        ret.setTipoDistribucion(null);
        return ret;
    }
}
