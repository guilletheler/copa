package com.gt.copa.model.atemporal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "costosestandar")
public class CostoEstandar extends CodigoNombre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	Empresa empresa;

	@Getter(value = AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany
	@JoinTable(name = "clasificaciones_ce", joinColumns = { @JoinColumn(name = "ce_ceid") }, inverseJoinColumns = {
			@JoinColumn(name = "clasificaciones_clasificacionid") })
	private Set<ClasificacionDato> clasificaciones;

	public Set<ClasificacionDato> getClasificaciones() {
		if (clasificaciones == null) {
			clasificaciones = new HashSet<>();
		}
		return clasificaciones;
	}
}
