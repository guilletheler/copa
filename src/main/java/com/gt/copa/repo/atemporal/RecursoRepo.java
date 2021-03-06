package com.gt.copa.repo.atemporal;

import java.util.List;
import java.util.Optional;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Recurso;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoRepo extends PagingAndSortingRepository<Recurso, Integer>, JpaSpecificationExecutor<Recurso> {

    List<Recurso> findByEmpresaOrderByNombre(Empresa empresa);

    Optional<Recurso> findByEmpresaAndNombre(Empresa empresa, String nombre);
}
