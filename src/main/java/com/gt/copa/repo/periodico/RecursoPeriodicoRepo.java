package com.gt.copa.repo.periodico;

import java.util.List;

import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.RecursoPeriodico;
import com.gt.copa.model.temporal.Periodo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoPeriodicoRepo
		extends PagingAndSortingRepository<RecursoPeriodico, Integer>, JpaSpecificationExecutor<RecursoPeriodico> {

    List<RecursoPeriodico> findByEscenarioAndPeriodo(Escenario escenario, Periodo periodo);

}
