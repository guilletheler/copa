package com.gt.copa.repo.atemporal;

import java.util.List;
import java.util.Optional;

import com.gt.copa.model.atemporal.Actividad;
import com.gt.copa.model.atemporal.Empresa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepo
		extends PagingAndSortingRepository<Actividad, Integer>, JpaSpecificationExecutor<Actividad> {

    List<Actividad> findByProceso_Empresa(Empresa empresa);

    Optional<Actividad> findByProceso_EmpresaAndNombre(Empresa empresa, String string);

}
