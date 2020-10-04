package com.llira.yelpcamp.sb.apirest.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();

	public Page<Cliente> findAll(Pageable pageable);

	public Cliente findById(Long id);

	public Cliente save(Cliente cliente);

	public void delete(Long id);

}
