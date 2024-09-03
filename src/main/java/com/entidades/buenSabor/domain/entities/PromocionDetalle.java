package com.entidades.buenSabor.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@Audited
public class PromocionDetalle extends Base{
    private int cantidad;

    @ManyToOne
    @NotAudited
    private Articulo articulo;
}
