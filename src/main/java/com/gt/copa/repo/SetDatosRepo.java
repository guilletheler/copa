package com.gt.copa.repo;

import com.gt.copa.model.SetDatos;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetDatosRepo
		extends PagingAndSortingRepository<SetDatos, Integer>, JpaSpecificationExecutor<SetDatos> {

}
