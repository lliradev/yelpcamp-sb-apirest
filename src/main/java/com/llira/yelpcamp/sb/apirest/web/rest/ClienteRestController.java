package com.llira.yelpcamp.sb.apirest.web.rest;

import com.llira.yelpcamp.sb.apirest.entity.Cliente;
import com.llira.yelpcamp.sb.apirest.service.ClienteService;
import com.llira.yelpcamp.sb.apirest.web.rest.vm.ClienteVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClienteRestController
 *
 * @author llira
 * @version 1.0
 * @since 22/01/2021
 * <p>
 * Clase controlador que contiene las rutas del endpoint
 */
// @TODO - Revisar como evitar que aparezca el warning y para los métodos en general
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    // @TODO - Revisar como evitar que aparezca el warning
    @Autowired
    private ClienteService clienteService;

    /**
     * Método para obtener una lista de registros sin paginación
     *
     * @return {@link ResponseEntity}
     */
    @GetMapping("/clientes")
    public ResponseEntity<?> index() {
        Map<String, Object> params = new HashMap<>();
        List<ClienteVM> clientes;
        try {
            clientes = clienteService.findAll().stream().map(ClienteVM::new).collect(Collectors.toList());
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al consultar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
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
    @GetMapping("/clientes/paginated")
    public ResponseEntity<?> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "limit", defaultValue = "5") Integer limit,
                                     @RequestParam(name = "orderBy", defaultValue = "id") String orderBy,
                                     @RequestParam(name = "shape", defaultValue = "desc") String shape) {
        Map<String, Object> params = new HashMap<>();
        Page<ClienteVM> clientes;
        try {
            Pageable pageable = PageRequest.of(page, limit, Sort.by(orderBy).descending());
            if (shape.equals("asc"))
                pageable = PageRequest.of(page, limit, Sort.by(orderBy));
            clientes = clienteService.findAll(pageable).map(ClienteVM::new);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al consultar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
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
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Cliente cliente;
        Map<String, Object> params = new HashMap<>();
        try {
            cliente = clienteService.findById(id);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al consultar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (cliente == null) {
            params.put("message", "No se encontro el cliente.");
            return new ResponseEntity<>(params, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    /**
     * Método para crear un nuevo objeto
     *
     * @param cliente objeto a insertar en la base de datos
     * @return {@link ResponseEntity}
     */
    @PostMapping("/clientes")
    public ResponseEntity<?> insert(@Valid @RequestBody Cliente cliente, BindingResult result) {
        Map<String, Object> params = new HashMap<>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(e -> "El campo [" + e.getField().toUpperCase() + "] " + e.getDefaultMessage())
                    .collect(Collectors.toList());
            params.put("errors", errors);
            params.put("message", "Los campos no cumplen con la validación.");
            return new ResponseEntity<>(params, HttpStatus.BAD_REQUEST);
        }
        try {
            clienteService.save(cliente);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al insertar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    /**
     * Método para editar un objeto
     *
     * @param data objeto a editar
     * @param id   identificador único del registro a editar
     * @return {@link ResponseEntity}
     */
    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente data, BindingResult result, @PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        Map<String, Object> params = new HashMap<>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(e -> "El campo [" + e.getField().toUpperCase() + "] " + e.getDefaultMessage())
                    .collect(Collectors.toList());
            params.put("errors", errors);
            return new ResponseEntity<>(params, HttpStatus.BAD_REQUEST);
        }
        if (cliente == null) {
            params.put("message", "No se encontro el cliente.");
            return new ResponseEntity<>(params, HttpStatus.NOT_FOUND);
        }
        try {
            cliente.setApellido(data.getApellido());
            cliente.setNombre(data.getNombre());
            cliente.setEmail(data.getEmail());
            cliente.setCreatedAt(data.getCreatedAt());
            clienteService.save(cliente);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al editar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
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
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Método para subir una imagen
     *
     * @param image imagen del cliente
     * @param id    identificador único del registro
     * @return {@link ResponseEntity}
     */
    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile image, @RequestParam("id") Long id) {
        Map<String, Object> params = new HashMap<>();
        Cliente cliente = clienteService.findById(id);
        if (!image.isEmpty()) {
            String nombre = image.getOriginalFilename();
            Path ruta = Paths.get("uploads").resolve(nombre).toAbsolutePath();

            // Files.copy(image.getInputStream(), ruta);
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
