package com.llira.yelpcamp.sb.apirest.repository;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ClienteRepository
 *
 * @author llira
 * @version 1.0
 * @since 22/01/2021
 * <p>
 * Interfaz que interactua con la base de datos
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
