package com.gt.copa.repo.temporal;

import java.util.Date;
import java.util.List;

import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.RecursoPeriodico;
import com.gt.copa.model.temporal.ValorDato;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValorDatoRepo
		extends PagingAndSortingRepository<ValorDato, Integer>, JpaSpecificationExecutor<ValorDato> {

    List<ValorDato> findByDato_RecursoAndEscenarioAndFechaGreaterThanEqualAndFechaLessThanEqual(RecursoPeriodico r, Escenario escenario, Date time, Date fin);

}
