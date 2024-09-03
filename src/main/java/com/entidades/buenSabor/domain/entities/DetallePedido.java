package com.entidades.buenSabor.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
@Audited
public class DetallePedido extends Base{
    private Integer cantidad;
    private Double subTotal;

    @ManyToOne
    @NotAudited
    private Articulo articulo;

    @ManyToOne
    @NotAudited
    private Promocion promocion;
}
