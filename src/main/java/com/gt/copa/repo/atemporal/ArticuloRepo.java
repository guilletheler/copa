package com.gt.copa.repo.atemporal;

import java.util.List;

import com.gt.copa.model.atemporal.Articulo;
import com.gt.copa.model.atemporal.Empresa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepo
		extends PagingAndSortingRepository<Articulo, Integer>, JpaSpecificationExecutor<Articulo> {

    List<Articulo> findByEmpresaOrderByNombre(Empresa empresa);

}
