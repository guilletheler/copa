package com.gt.copa.model.sistema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gt.copa.model.IWithId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@Entity
@Table(name = "parametros")
public class Parametro implements IWithId<Integer> {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

    String nombre;

    String valor;

}
