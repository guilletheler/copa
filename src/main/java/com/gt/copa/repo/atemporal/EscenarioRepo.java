package com.gt.copa.repo.atemporal;

import java.util.Optional;

import com.gt.copa.model.atemporal.Escenario;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscenarioRepo
		extends JpaSpecificationExecutor<Escenario>, PagingAndSortingRepository<Escenario, Integer> {

    Optional<Escenario> findByNombre(String string);

}
