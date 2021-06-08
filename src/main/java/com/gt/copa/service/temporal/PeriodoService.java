package com.gt.copa.service.temporal;

import java.util.Calendar;

import com.gt.copa.Utils;
import com.gt.copa.model.temporal.Periodo;
import com.gt.copa.model.temporal.TipoPeriodo;
import com.gt.copa.repo.temporal.PeriodoRepo;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeriodoService {
    @Autowired
    PeriodoRepo periodoRepo;

    /**
     * Agrega los meses del año pasado, de este año y del año siguiente
     */
    public void checkYear() {

        Calendar cal = Calendar.getInstance();
        cal = DateUtils.truncate(cal, Calendar.YEAR);

        cal.add(Calendar.YEAR, -1);

        for(int i = 0; i < 36; i++) {
            Calendar cFin = Calendar.getInstance();
            cFin.setTime(cal.getTime());
            cFin.add(Calendar.MONTH, 1);
            cFin.add(Calendar.DAY_OF_MONTH, -1);

            Periodo periodo = periodoRepo.findByInicioAndFin(cal.getTime(), cFin.getTime()).orElse(null);

            if(periodo == null) {
                periodo = new Periodo();
                periodo.setCodigo(Integer.valueOf(Utils.SDF_YM.format(cal.getTime())));
                periodo.setNombre(Utils.SDF_YM.format(cal.getTime()));
                periodo.setInicio(cal.getTime());
                periodo.setFin(cFin.getTime());
                periodo.setTipoPeriodo(TipoPeriodo.MENSUAL);
                periodoRepo.save(periodo);
            }

            cal.add(Calendar.MONTH, 1);
        }

    }
}
