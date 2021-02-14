package com.llira.yelpcamp.sb.apirest.web.rest.vm;

import com.llira.yelpcamp.sb.apirest.domain.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ClienteVM
 *
 * @author llira
 * @version 1.0
 * @since 22/01/2021
 * <p>
 * Vista modelo, contiene los atributos que serán expuestos al Rest Controller
 */
@Setter
@Getter
public class ClienteVM {
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCompleto;
    private String email;
    private String createdAt;
    private String imagen;

    public ClienteVM(Cliente data) {
        this.id = data.getId();
        this.nombre = data.getNombre();
        this.apellidoPaterno = data.getApellidoPaterno();
        this.apellidoMaterno = data.getApellidoMaterno();
        this.nombreCompleto = data.getNombre() + " " + data.getApellidoPaterno() + " " + data.getApellidoMaterno();
        this.nombreCompleto = this.nombreCompleto.replace("null", "");
        this.email = data.getEmail();
        this.createdAt = convertDate(data.getCreatedAt());
        this.imagen = data.getImagen();
    }

    private String convertDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(date);
    }
}
