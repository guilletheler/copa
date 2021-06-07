package com.gt.copa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "setsdatos")
public class SetDatos extends CodigoNombre implements IWithId<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;
}
