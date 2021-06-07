package com.gt.copa.repo.atemporal;

import java.util.List;

import com.gt.copa.model.atemporal.Driver;
import com.gt.copa.model.atemporal.Empresa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepo extends PagingAndSortingRepository<Driver, Integer>, JpaSpecificationExecutor<Driver> {

    List<Driver> findByEmpresa(Empresa empresa);

}
