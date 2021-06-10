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
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "recursos")
public class Recurso extends CodigoNombre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	Empresa empresa;

	// @Getter(value = AccessLevel.NONE)
	// @EqualsAndHashCode.Exclude
	// @ToString.Exclude
	// @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, orphanRemoval = true)
	// private List<Dato> datos;

	// public List<Dato> getDatos() {
	// 	if (datos == null) {
	// 		datos = new ArrayList<>();
	// 	}
	// 	return datos;
	// }
}
