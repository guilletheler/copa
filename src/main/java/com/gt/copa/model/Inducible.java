package com.gt.copa.model;

import javax.persistence.MappedSuperclass;

import com.gt.copa.calc.api.TipoDistribucion;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author guille
 */
@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class Inducible extends CodigoNombre {

	TipoDistribucion tipoDistribucion;

}