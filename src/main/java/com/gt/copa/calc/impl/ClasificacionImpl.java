/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.impl;

import com.gt.copa.calc.api.IClasificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author guille
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionImpl implements IClasificacion {
	Integer codigo;

	String nombre;

}
