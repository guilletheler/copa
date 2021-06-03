package com.gt.copa.repo;

import java.util.List;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Escenario;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscenarioRepo extends PagingAndSortingRepository<Escenario, Integer>,
JpaSpecificationExecutor<Escenario> {

	List<Escenario> findByEmpresa(Empresa empresa);
}
