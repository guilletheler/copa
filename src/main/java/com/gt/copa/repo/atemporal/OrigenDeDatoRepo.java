package com.gt.copa.repo.atemporal;

import com.gt.copa.model.atemporal.OrigenDeDato;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrigenDeDatoRepo
		extends PagingAndSortingRepository<OrigenDeDato, Integer>, JpaSpecificationExecutor<OrigenDeDato> {

}
