package com.gt.copa.service.periodico;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ComponenteDriverPeriodico;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ComponenteDriverRepo;
import com.gt.copa.repo.periodico.ComponenteDriverPeriodicoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class ComponenteDriverPeriodicoService {

    @Getter
    @Autowired
    ComponenteDriverPeriodicoRepo repo;

    @Autowired
    ComponenteDriverRepo componenteDriverRepo;

    @Transactional
    public List<ComponenteDriverPeriodico> findOrCreate(Empresa empresa, Escenario escenario, Periodo periodo) {
        List<ComponenteDriver> componentesDriver = StreamSupport
                .stream(componenteDriverRepo.findByDriver_EmpresaOrderByNombre(empresa).spliterator(), false).collect(Collectors.toList());

        List<ComponenteDriverPeriodico> lst = repo
                .findByComponenteDriver_Driver_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
                        empresa, escenario, periodo);

        List<ComponenteDriver> existen = lst.stream().map(ap -> ap.getComponenteDriver()).collect(Collectors.toList());

        List<ComponenteDriver> noExisten = componentesDriver.stream().filter(act -> !existen.contains(act))
                .collect(Collectors.toList());

        List<ComponenteDriverPeriodico> nuevas = noExisten.stream().map(a -> createComponenteDriverPeriodico(a, escenario, periodo)).collect(Collectors.toList());

        return Stream.concat(lst.stream(),  nuevas.stream()).collect(Collectors.toList());
    }

    private ComponenteDriverPeriodico createComponenteDriverPeriodico(ComponenteDriver cd, Escenario escenario, Periodo periodo) {
        ComponenteDriverPeriodico ret = new ComponenteDriverPeriodico();
        ret.setComponenteDriver(cd);
        ret.setConfiguracionPeriodo(ConfiguracionPeriodo.builder().escenario(escenario).periodo(periodo).build());
        ret.setValor(null);
        return ret;
    }
}
