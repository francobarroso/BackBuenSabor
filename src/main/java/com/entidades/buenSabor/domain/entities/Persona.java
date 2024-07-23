package com.entidades.buenSabor.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
//@Audited
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona extends Base {

    protected String nombre;
    protected String apellido;
    protected String telefono;
    private LocalDate fechaNacimiento;

    @OneToOne(cascade = CascadeType.ALL)
    protected Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @NotAudited
    protected ImagenPersona imagenPersona;
}