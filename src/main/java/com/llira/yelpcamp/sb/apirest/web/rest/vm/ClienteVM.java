package com.llira.yelpcamp.sb.apirest.web.rest.vm;

import com.llira.yelpcamp.sb.apirest.domain.Cliente;
import com.llira.yelpcamp.sb.apirest.util.Util;
import lombok.Getter;
import lombok.Setter;

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
    private String correo;
    private String fechaCreacion;
    private String imagen;

    public ClienteVM(Cliente data) {
        this.id = data.getId();
        this.nombre = data.getNombre();
        this.apellidoPaterno = data.getApellidoPaterno();
        this.apellidoMaterno = data.getApellidoMaterno();
        this.nombreCompleto = data.getNombre() + " " + data.getApellidoPaterno() + " " + data.getApellidoMaterno();
        this.nombreCompleto = this.nombreCompleto.replace("null", "").trim();
        this.correo = data.getCorreo();
        this.fechaCreacion = Util.convertDate(data.getFechaCreacion());
        this.imagen = data.getImagen();
    }
}
