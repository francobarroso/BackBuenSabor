package com.entidades.buenSabor.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
@Audited
public class Provincia extends Base {
    private String nombre;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pais_id")
    private Pais pais;

}
