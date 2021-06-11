package com.gt.copa.repo.periodico;

import java.util.List;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ActividadEnObjetoDeCosto;
import com.gt.copa.model.temporal.Periodo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadEnObjetoDeCostoRepo extends PagingAndSortingRepository<ActividadEnObjetoDeCosto, Integer>,
    JpaSpecificationExecutor<ActividadEnObjetoDeCosto> {

  List<ActividadEnObjetoDeCosto> findByActividad_Proceso_EmpresaAndConfiguracionPeriodo_EscenarioAndConfiguracionPeriodo_Periodo(Empresa empresa, Escenario escenario, Periodo periodo);

}
