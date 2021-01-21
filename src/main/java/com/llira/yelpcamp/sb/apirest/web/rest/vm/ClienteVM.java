package com.llira.yelpcamp.sb.apirest.web.rest.vm;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
public class ClienteVM {
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String email;
    private String createdAt;

    public ClienteVM(Cliente cliente) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();
        this.nombreCompleto = this.nombreCompleto.replace("null", "");
        this.email = cliente.getEmail();
        this.createdAt = convertDate(cliente.getCreatedAt());
    }

    private String convertDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
