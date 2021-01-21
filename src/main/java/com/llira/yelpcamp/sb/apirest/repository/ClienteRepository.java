package com.llira.yelpcamp.sb.apirest.repository;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
