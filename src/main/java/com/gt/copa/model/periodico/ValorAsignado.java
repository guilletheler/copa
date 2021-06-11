package com.gt.copa.model.periodico;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.gt.copa.model.atemporal.ComponenteDriver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValorAsignado {
        
    @ManyToOne
    ComponenteDriver componenteDriver;
    
    Double valorParticular;
}
