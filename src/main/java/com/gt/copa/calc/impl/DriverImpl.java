/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import java.util.ArrayList;
import java.util.List;

import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.IDriver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author guille
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverImpl implements IDriver {

	Integer codigo;
	String nombre;

	List<IComponenteDriver> componentes;

	public IComponenteDriver searchComponente(String nombre) {
		for (IComponenteDriver cd : this.getComponentes()) {
			if (cd.getNombre().equals(nombre)) {
				return cd;
			}
		}
		return null;
	}

	public List<IComponenteDriver> getComponentes() {
		if (componentes == null) {
			componentes = new ArrayList<>();
		}
		return componentes;
	}


	public void addComponent(Integer codigo, String nombre, Double valor) {
		getComponentes().add(ComponenteDriverImpl.builder()
				.driver(this)
				.codigo(codigo)
				.nombre(nombre)
				.valor(valor)
				.build());
	}
}
