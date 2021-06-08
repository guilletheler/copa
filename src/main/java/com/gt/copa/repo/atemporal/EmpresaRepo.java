package com.gt.copa.repo.atemporal;

import java.util.Optional;

import com.gt.copa.model.atemporal.Empresa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepo extends PagingAndSortingRepository<Empresa, Integer>,
		JpaSpecificationExecutor<Empresa> {

    Optional<Empresa> findByNombre(String string);

}
