package com.gt.copa.model.periodico;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.gt.copa.model.atemporal.Escenario;
import com.gt.copa.model.temporal.Periodo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionPeriodo {

    @NotNull
    @ManyToOne
    Periodo periodo;

    @NotNull
    @ManyToOne
    Escenario escenario;
}
