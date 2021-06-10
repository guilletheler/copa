package com.gt.copa.repo.atemporal;

import java.util.List;
import java.util.Optional;

import com.gt.copa.model.atemporal.ClasificacionDato;
import com.gt.copa.model.atemporal.TipoClasificacionDato;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClasificacionDatoRepo
		extends PagingAndSortingRepository<ClasificacionDato, Integer>, JpaSpecificationExecutor<ClasificacionDato> {

		List<ClasificacionDato> findByTipoClasificacion(TipoClasificacionDato tipoClasificacion);

        Optional<ClasificacionDato> findByNombre(String string);

}
