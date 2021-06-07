package com.gt.copa.repo.atemporal;

import com.gt.copa.model.atemporal.Actividad;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepo
		extends PagingAndSortingRepository<Actividad, Integer>, JpaSpecificationExecutor<Actividad> {

}
