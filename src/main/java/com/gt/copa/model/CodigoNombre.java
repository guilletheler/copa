package com.gt.copa.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.gt.copa.calc.api.ICodigoNombre;

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
@EqualsAndHashCode
@ToString
public abstract class CodigoNombre implements ICodigoNombre, Populable {
	
	@NotNull(message = "El código no puede ser nulo")
	@PositiveOrZero(message = "El código debe ser un valor positivo")
	Integer codigo;

	@NotNull(message = "El nombre no puede ser nulo")
	@Column(length = 255)
	String nombre;

	@Override
	public String getEtiqueta() {
		return getCodigoNombre();
	}

	public String getCodigoNombre() {
		return (codigo == null ? "NULL" : codigo) + " | " + (nombre == null ? "NULL" : nombre);
	}

}