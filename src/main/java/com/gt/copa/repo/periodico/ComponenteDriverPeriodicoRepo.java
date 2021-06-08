package com.gt.copa.repo.periodico;

import java.util.List;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.periodico.ComponenteDriverPeriodico;
import com.gt.copa.model.temporal.Periodo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponenteDriverPeriodicoRepo extends PagingAndSortingRepository<ComponenteDriverPeriodico, Integer>,
		JpaSpecificationExecutor<ComponenteDriverPeriodico> {

	List<ComponenteDriverPeriodico> findByEmpresaAndEscenarioAndPeriodo(Empresa empresa, Escenario escenario, Periodo periodo);

}
