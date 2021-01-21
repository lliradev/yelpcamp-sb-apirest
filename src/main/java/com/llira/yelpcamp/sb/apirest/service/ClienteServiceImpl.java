package com.llira.yelpcamp.sb.apirest.service;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;
import com.llira.yelpcamp.sb.apirest.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

// @TODO - Revisar como evitar que aparezca el warning
@Service
class ClienteServiceImpl implements ClienteService {

    // @TODO - Revisar como evitar que aparezca el warning
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        if (cliente.getCreatedAt() == null) {
            cliente.setCreatedAt(new Date());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
