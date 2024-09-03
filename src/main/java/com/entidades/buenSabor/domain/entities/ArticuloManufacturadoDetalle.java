package com.entidades.buenSabor.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@Audited
public class ArticuloManufacturadoDetalle extends Base{
    private Integer cantidad;

    @ManyToOne
    @NotAudited
    private ArticuloInsumo articuloInsumo;
}
