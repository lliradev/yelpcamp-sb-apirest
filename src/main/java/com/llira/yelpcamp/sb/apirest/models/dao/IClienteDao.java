package com.llira.yelpcamp.sb.apirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.llira.yelpcamp.sb.apirest.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

}
