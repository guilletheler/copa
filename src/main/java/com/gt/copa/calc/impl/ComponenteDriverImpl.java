/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import com.gt.copa.calc.api.IComponenteDriver;
import com.gt.copa.calc.api.IDriver;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author guille
 */
@Data
@Builder
public class ComponenteDriverImpl implements IComponenteDriver {

	Integer codigo;

	String nombre;

	Double valor;

	IDriver driver;

}
