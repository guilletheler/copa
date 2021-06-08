package com.gt.copa.repo.temporal;

import java.util.Date;
import java.util.Optional;

import com.gt.copa.model.temporal.Periodo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoRepo extends PagingAndSortingRepository<Periodo, Integer>,
		JpaSpecificationExecutor<Periodo> {

    Optional<Periodo> findByInicioAndFin(Date inicio, Date fin);

    Optional<Periodo> findByNombre(String string);

}
