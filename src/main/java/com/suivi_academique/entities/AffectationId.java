package com.suivi_academique.entities;


import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode // REQUIS pour les @IdClass ou @Embeddable (HHH000038)
@Embeddable

public class AffectationId implements Serializable {

    @Basic(optional = false)
    private String codeCours;

    @Basic(optional = false)
    private String codePersonnel;

    public AffectationId(String codeCours, String codePersonnel) {
        this.codeCours = codeCours;
        this.codePersonnel = codePersonnel;
    }

    public AffectationId() {

    }



}
