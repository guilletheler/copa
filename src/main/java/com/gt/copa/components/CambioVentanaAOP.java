package com.gt.copa.components;

import com.gt.copa.controller.MainViewController;
import com.gt.copa.controller.ModificadorDatos;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CambioVentanaAOP {

    // @Autowired
    // MainViewController mainViewController;

    // @Before("execution(* com.gt.copa.controller.MainViewController.mnu*Click(..))")
    public void prevenirPerdidaDatos(JoinPoint joinPoint) throws Throwable {

        System.out.println("logBefore() is running!");
        System.out.println("hijacked : " + joinPoint.getSignature().getName());
        System.out.println("this : " + joinPoint.getThis().getClass().getName());
        System.out.println("target : " + joinPoint.getTarget().getClass().getName());
        System.out.println("******");

        // ModificadorDatos md = mainViewController.getCurrentModificadorDatos();

        // if (md != null && md.isDataModificada()) {
        //     Integer ret = mainViewController.confirmarGuardarDatos();
        //     switch (ret) {
        //         case 0:
        //             // NO
        //             break;
        //         case 1:
        //             // CANCEL
        //             break;
        //         case 2:
        //             // YES
        //             md.saveDataModificada();
        //             break;
        //     }
        // }
    }
}
