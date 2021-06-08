package com.gt.copa.model.atemporal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gt.copa.model.CodigoNombre;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "clasificaciones")
public class ClasificacionDato extends CodigoNombre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	private TipoClasificacionDato tipoClasificacion;

	public ClasificacionDato(TipoClasificacionDato tipo, Integer codigo, String nombre) {
		setCodigo(codigo);
		setNombre(nombre);
		setTipoClasificacion(tipo);
	}
}
