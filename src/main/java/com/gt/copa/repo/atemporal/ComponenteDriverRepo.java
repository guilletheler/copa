package com.gt.copa.repo.atemporal;

import java.util.List;
import java.util.Optional;

import com.gt.copa.model.atemporal.ComponenteDriver;
import com.gt.copa.model.atemporal.Empresa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponenteDriverRepo
		extends PagingAndSortingRepository<ComponenteDriver, Integer>, JpaSpecificationExecutor<ComponenteDriver> {

    List<ComponenteDriver> findByDriver_Empresa(Empresa empresa);

    Optional<ComponenteDriver> findByDriver_EmpresaAndDriver_NombreAndNombre(Empresa empresa, String nombreDriver, String nombre);
}
