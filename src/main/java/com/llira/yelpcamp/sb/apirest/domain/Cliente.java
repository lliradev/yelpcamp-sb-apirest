package com.llira.yelpcamp.sb.apirest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

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
    @Size(min = 2, max = 50, message = "debe tener entre 2 y 50 caracteres.")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "no debe estar vacío.")
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @NotEmpty(message = "no debe estar vacío.")
    @Email(message = "debe ser una dirección de correo electrónico con formato correcto.")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at")
    @JsonDeserialize(as = LocalDate.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate createdAt;

    private String imagen;

    @Column(name = "public_id")
    private String publicId;

    private static final long serialVersionUID = 1L;
}
