package com.llira.yelpcamp.sb.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.llira.yelpcamp.sb.apirest.models.entity.Cliente;
import com.llira.yelpcamp.sb.apirest.models.services.IClienteService;

@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	/**
	 * Método para obtener una lista de registros sin paginación
	 * 
	 * @return {@link ResponseEntity}
	 */
	@GetMapping("/clientes")
	public ResponseEntity<?> index() {
		Map<String, Object> params = new HashMap<>();
		List<Cliente> clientes;
		try {
			clientes = clienteService.findAll();
		} catch (DataAccessException e) {
			params.put("message", "Se produjo un error al consultar en la base de datos.");
			params.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	/**
	 * Método para obtener una lista paginada de los registros
	 * 
	 * @param page    página actual
	 * @param limit   limite de registros
	 * @param orderBy campo a ordenar
	 * @param shape   forma ascendente o descendente
	 * @return {@link ResponseEntity}
	 */
	@GetMapping("/clientes/page")
	public ResponseEntity<?> index(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "limit", defaultValue = "5") Integer limit,
			@RequestParam(name = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(name = "shape", defaultValue = "desc") String shape) {

		Map<String, Object> params = new HashMap<>();
		Page<Cliente> clientes;
		try {
			Pageable pageable = PageRequest.of(page, limit, Sort.by(orderBy).descending());
			if (shape.equals("asc"))
				pageable = PageRequest.of(page, limit, Sort.by(orderBy));
			clientes = clienteService.findAll(pageable);
		} catch (DataAccessException e) {
			params.put("message", "Se produjo un error al consultar en la base de datos.");
			params.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	/**
	 * Método para obtener un registro en específico
	 * 
	 * @param id identificador único del registro
	 * @return {@link ResponseEntity}
	 */
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente;
		Map<String, Object> params = new HashMap<>();
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			params.put("message", "Se produjo un error al consultar en la base de datos.");
			params.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (cliente == null) {
			params.put("message", "No se encontro el cliente.");
			return new ResponseEntity<>(params, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}

	/**
	 * Método para crear un nuevo objeto
	 * 
	 * @param cliente objeto a insertar en la base de datos
	 * @return {@link ResponseEntity}
	 */
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		Map<String, Object> params = new HashMap<>();
		try {
			clienteService.save(cliente);
		} catch (DataAccessException e) {
			params.put("message", "Se produjo un error al insertar en la base de datos.");
			params.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	/**
	 * Método para editar un objeto
	 * 
	 * @param cliente objeto a editar
	 * @param id      identificador único del registro a editar
	 * @return {@link ResponseEntity}
	 */
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente c1 = clienteService.findById(id);
		Map<String, Object> params = new HashMap<>();
		if (c1 == null) {
			params.put("message", "No se encontro el cliente.");
			return new ResponseEntity<>(params, HttpStatus.NOT_FOUND);
		}
		try {
			c1.setApellido(cliente.getApellido());
			c1.setNombre(cliente.getNombre());
			c1.setEmail(cliente.getEmail());
			c1.setCreatedAt(cliente.getCreatedAt());
			clienteService.save(c1);
		} catch (DataAccessException e) {
			params.put("message", "Se produjo un error al editar en la base de datos.");
			params.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	/**
	 * Método para eliminar un registro
	 * 
	 * @param id identificador único del registro a eliminar
	 * @return {@link ResponseEntity}
	 */
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> params = new HashMap<>();
		try {
			clienteService.delete(id);
		} catch (DataAccessException e) {
			params.put("message", "Se produjo un error al eliminar en la base de datos.");
			params.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}
