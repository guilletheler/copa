package com.gt.copa.repo.atemporal;

import java.util.List;
import java.util.Optional;

import com.gt.copa.model.atemporal.Dato;
import com.gt.copa.model.atemporal.Empresa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatoRepo extends PagingAndSortingRepository<Dato, Integer>, JpaSpecificationExecutor<Dato> {

    List<Dato> findByRecurso_EmpresaOrderByNombre(Empresa empresa);
    
    Optional<Dato> findByRecurso_EmpresaAndNombre(Empresa empresa, String nombre);

}
