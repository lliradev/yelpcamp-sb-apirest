package com.llira.yelpcamp.sb.apirest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Cliente
 *
 * @author llira
 * @version 1.0
 * @since 19/01/2021
 * <p>
 * Entidad de la tabla clientes
 */
@Setter
@Getter
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "no debe estar vacío.")
    @Size(min = 4, max = 12, message = "debe tener entre 4 y 12 caracteres.")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "no debe estar vacío.")
    private String apellido;

    @NotEmpty(message = "no debe estar vacío.")
    @Email(message = "debe ser una dirección de correo electrónico con formato correcto.")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    private String imagen;

    private static final long serialVersionUID = 1L;
}
