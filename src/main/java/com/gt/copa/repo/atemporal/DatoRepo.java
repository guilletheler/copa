package com.gt.copa.repo.atemporal;

import com.gt.copa.model.atemporal.Dato;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatoRepo extends PagingAndSortingRepository<Dato, Integer>, JpaSpecificationExecutor<Dato> {

}
