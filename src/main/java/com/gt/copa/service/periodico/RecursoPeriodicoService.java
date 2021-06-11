package com.gt.copa.service.periodico;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.gt.copa.model.atemporal.Recurso;
import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.RecursoPeriodico;
import com.gt.copa.model.periodico.ConfiguracionPeriodo;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.repo.atemporal.RecursoRepo;
import com.gt.copa.repo.periodico.RecursoPeriodicoRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
public class RecursoPeriodicoService {

    @Getter
    @Autowired
    RecursoPeriodicoRepo repo;

    @Autowired
    RecursoRepo actividadRepo;

    @Transactional
    public List<RecursoPeriodico> findOrCreate(Empresa empresa, Escenario escenario, Periodo periodo) {
        List<Recurso> actividades = StreamSupport
                .stream(actividadRepo.findByEmpresa(empresa).spliterator(), false).collect(Collectors.toList());

        List<RecursoPeriodico> lst = repo
                .findByRecurso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(
                        empresa, escenario, periodo);

        List<Recurso> existen = lst.stream().map(ap -> ap.getRecurso()).collect(Collectors.toList());

        List<Recurso> noExisten = actividades.stream().filter(act -> !existen.contains(act))
                .collect(Collectors.toList());

        List<RecursoPeriodico> nuevas = noExisten.stream().map(a -> createRecursoPeriodico(a, escenario, periodo)).collect(Collectors.toList());

        return Stream.concat(lst.stream(),  nuevas.stream()).collect(Collectors.toList());
    }

    private RecursoPeriodico createRecursoPeriodico(Recurso a, Escenario escenario, Periodo periodo) {
        RecursoPeriodico ret = new RecursoPeriodico();
        ret.setRecurso(a);
        ret.setConfiguracionPeriodo(ConfiguracionPeriodo.builder().escenario(escenario).periodo(periodo).build());
        ret.setTipoDistribucion(null);
        ret.setPromedioNoVacio(false);
        ret.setTamanioMuestra(1);
        ret.setTratamientoMuestra(null);
        return ret;
    }
}
