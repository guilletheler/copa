package com.gt.copa.repo.sistema;

import java.util.Optional;

import com.gt.copa.model.sistema.Parametro;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepo extends CrudRepository<Parametro, Integer>,
        JpaSpecificationExecutor<Parametro> {

    Optional<Parametro> findByNombre(String nombre);
}
