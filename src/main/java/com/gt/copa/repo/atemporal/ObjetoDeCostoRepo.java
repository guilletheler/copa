package com.gt.copa.repo.atemporal;

import java.util.List;
import java.util.Optional;

import com.gt.copa.model.atemporal.Empresa;
import com.gt.copa.model.atemporal.ObjetoDeCosto;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetoDeCostoRepo
		extends PagingAndSortingRepository<ObjetoDeCosto, Integer>, JpaSpecificationExecutor<ObjetoDeCosto> {

    List<ObjetoDeCosto> findByEmpresaOrderByNombre(Empresa empresa);

    Optional<ObjetoDeCosto> findByEmpresaAndNombre(Empresa empresa, String string);

}
