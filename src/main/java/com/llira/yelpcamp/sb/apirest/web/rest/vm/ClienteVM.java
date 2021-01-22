package com.llira.yelpcamp.sb.apirest.web.rest.vm;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String apellido;
    private String nombreCompleto;
    private String email;
    private String createdAt;

    public ClienteVM(Cliente data) {
        this.id = data.getId();
        this.nombre = data.getNombre();
        this.apellido = data.getApellido();
        this.nombreCompleto = data.getNombre() + " " + data.getApellido();
        this.nombreCompleto = this.nombreCompleto.replace("null", "");
        this.email = data.getEmail();
        this.createdAt = convertDate(data.getCreatedAt());
    }

    private String convertDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
