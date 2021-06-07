package com.gt.copa.repo.atemporal;

import com.gt.copa.model.atemporal.ObjetoDeCosto;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetoDeCostoRepo
		extends PagingAndSortingRepository<ObjetoDeCosto, Integer>, JpaSpecificationExecutor<ObjetoDeCosto> {

}
