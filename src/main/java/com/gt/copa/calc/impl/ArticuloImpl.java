/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.ArrayList;
import java.util.List;

import com.gt.copa.calc.api.IArticulo;
import com.gt.copa.calc.api.ICostoEstandarEnArticulo;
import com.gt.copa.calc.api.IObjetoDeCostoEnArticulo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author guillermo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticuloImpl implements IArticulo {

	Integer codigo;

	String nombre;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	List<ICostoEstandarEnArticulo> costosEstandarInducidos;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	List<IObjetoDeCostoEnArticulo> objetosDeCostoInducidos;

	public List<ICostoEstandarEnArticulo> getCostosEstandarInducidos() {
		if (costosEstandarInducidos == null) {
			costosEstandarInducidos = new ArrayList<>();
		}
		return costosEstandarInducidos;
	}

	public List<IObjetoDeCostoEnArticulo> getObjetosDeCostoInducidos() {
		if (objetosDeCostoInducidos == null) {
			objetosDeCostoInducidos = new ArrayList<>();
		}
		return objetosDeCostoInducidos;
	}
}
