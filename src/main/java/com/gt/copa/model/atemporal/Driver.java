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
@Table(name = "drivers")
public class Driver extends CodigoNombre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	private Empresa empresa;

	// @Getter(value = AccessLevel.NONE)
	// @EqualsAndHashCode.Exclude
	// @ToString.Exclude
	// @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
	// private List<ComponenteDriver> componentesDriver;

	// public List<ComponenteDriver> getComponentesDriver() {
	// 	if (componentesDriver == null) {
	// 		componentesDriver = new ArrayList<>();
	// 	}
	// 	return componentesDriver;
	// }
}
