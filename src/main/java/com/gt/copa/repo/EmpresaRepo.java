package com.gt.copa.repo;

import java.util.List;

import com.gt.copa.model.SetDatos;
import com.gt.copa.model.atemporal.Empresa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepo extends PagingAndSortingRepository<Empresa, Integer>,
		JpaSpecificationExecutor<Empresa> {

	List<Empresa> findBySetDatos(SetDatos setDatos);
}
