package com.gt.copa.repo.atemporal;

import java.util.Optional;

import com.gt.copa.model.atemporal.TipoClasificacionDato;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoClasificacionDatoRepo extends PagingAndSortingRepository<TipoClasificacionDato, Integer>,
		JpaSpecificationExecutor<TipoClasificacionDato> {

    Optional<TipoClasificacionDato> findByNombre(String string);

}
