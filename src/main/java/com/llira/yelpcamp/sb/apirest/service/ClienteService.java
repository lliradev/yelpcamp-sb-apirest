package com.llira.yelpcamp.sb.apirest.service;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * ClienteService
 *
 * @author llira
 * @version 1.0
 * @since 22/01/2021
 * <p>
 * Interfaz que contiene la lógica de negocio e interactua con el repositorio
 */
public interface ClienteService {

    List<Cliente> findAll();

    Page<Cliente> findAll(Pageable pageable);

    Cliente findById(Long id);

    Cliente save(Cliente cliente);

    void delete(Long id);
}
