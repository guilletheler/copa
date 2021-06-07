package com.gt.copa.repo.atemporal;

import com.gt.copa.model.atemporal.ComponenteDriver;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponenteDriverRepo
		extends PagingAndSortingRepository<ComponenteDriver, Integer>, JpaSpecificationExecutor<ComponenteDriver> {

}
