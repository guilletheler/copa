package com.gt.copa.model.atemporal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.gt.copa.model.CodigoNombre;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "procesos")
public class Proceso extends CodigoNombre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	private Empresa empresa;

	// @Getter(value = AccessLevel.NONE)
	// @EqualsAndHashCode.Exclude
	// @ToString.Exclude
	// @OneToMany(mappedBy = "proceso", cascade = CascadeType.ALL, orphanRemoval = true)
	// private List<Actividad> actividades;

	// public List<Actividad> getActividades() {
	// 	if (actividades == null) {
	// 		actividades = new ArrayList<>();
	// 	}
	// 	return actividades;
	// }
}
