package com.llira.yelpcamp.sb.apirest.service;

import com.llira.yelpcamp.sb.apirest.domain.Cliente;
import com.llira.yelpcamp.sb.apirest.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * ClienteServiceImpl
 *
 * @author llira
 * @version 1.0
 * @since 22/01/2021
 * <p>
 * Clase de implementación que contiene la lógica de negocio e interactua con el repositorio
 */
@Service
class ClienteServiceImpl implements ClienteService {
    private final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        log.info("Obtener lista");
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        log.info("Obtener lista paginada");
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        log.info("Obtener por id");
        return clienteRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        if (cliente.getFechaCreacion() == null)
            cliente.setFechaCreacion(LocalDate.now());
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminar un registro");
        clienteRepository.deleteById(id);
    }
}
