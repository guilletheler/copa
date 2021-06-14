package com.gt.copa.service.periodico;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.gt.copa.model.atemporal.ObjetoDeCosto;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ObjetoDeCostoPeriodico;
import com.gt.copa.model.periodico.ValorAsignado;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.ObjetoDeCostoRepo;
import com.gt.copa.repo.periodico.ObjetoDeCostoPeriodicoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class ObjetoDeCostoPeriodicoService {

    @Getter
    @Autowired
    ObjetoDeCostoPeriodicoRepo repo;

    @Autowired
    ObjetoDeCostoRepo actividadRepo;

    @Transactional
    public List<ObjetoDeCostoPeriodico> findOrCreate(Empresa empresa, Escenario escenario, Periodo periodo) {
        List<ObjetoDeCosto> actividades = StreamSupport
                .stream(actividadRepo.findByEmpresaOrderByNombre(empresa).spliterator(), false).collect(Collectors.toList());

        List<ObjetoDeCostoPeriodico> lst = repo
                .findByObjetoDeCosto_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
                        empresa, escenario, periodo);

        List<ObjetoDeCosto> existen = lst.stream().map(ap -> ap.getObjetoDeCosto()).collect(Collectors.toList());

        List<ObjetoDeCosto> noExisten = actividades.stream().filter(act -> !existen.contains(act))
                .collect(Collectors.toList());

        List<ObjetoDeCostoPeriodico> nuevas = noExisten.stream().map(a -> createObjetoDeCostoPeriodico(a, escenario, periodo)).collect(Collectors.toList());

        return Stream.concat(lst.stream(),  nuevas.stream()).collect(Collectors.toList());
    }

    private ObjetoDeCostoPeriodico createObjetoDeCostoPeriodico(ObjetoDeCosto a, Escenario escenario, Periodo periodo) {
        ObjetoDeCostoPeriodico ret = new ObjetoDeCostoPeriodico();
        ret.setObjetoDeCosto(a);
        ret.setConfiguracionPeriodo(ConfiguracionPeriodo.builder().escenario(escenario).periodo(periodo).build());
        ret.setTipoDistribucion(null);
        ret.setValorAsignado(new ValorAsignado());
        return ret;
    }
}
