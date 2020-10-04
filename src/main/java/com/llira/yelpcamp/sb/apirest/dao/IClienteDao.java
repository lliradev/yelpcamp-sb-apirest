package com.llira.yelpcamp.sb.apirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
}
