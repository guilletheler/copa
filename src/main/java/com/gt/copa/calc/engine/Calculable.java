/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.copa.calc.engine;

/**
 *
 * @author guille
 */
public interface Calculable {
    void calcular() throws CopaEngineException;
    void clearCalc();
}
