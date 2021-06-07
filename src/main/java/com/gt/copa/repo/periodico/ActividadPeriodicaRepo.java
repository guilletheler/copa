package com.gt.copa.repo.periodico;

import java.util.List;

import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ActividadPeriodica;
import com.gt.copa.model.temporal.Periodo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadPeriodicaRepo
		extends PagingAndSortingRepository<ActividadPeriodica, Integer>, JpaSpecificationExecutor<ActividadPeriodica> {

    List<ActividadPeriodica> findByEscenarioAndPeriodo(Escenario escenario, Periodo periodo);

}