package com.gt.copa.repo.atemporal;

import java.util.List;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.Proceso;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcesoRepo extends PagingAndSortingRepository<Proceso, Integer>, JpaSpecificationExecutor<Proceso> {

    List<Proceso> findByEmpresa(Empresa empresa);

}
