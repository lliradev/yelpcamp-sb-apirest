package com.llira.yelpcamp.sb.apirest.web.rest;

import com.llira.yelpcamp.sb.apirest.domain.Cliente;
import com.llira.yelpcamp.sb.apirest.service.ClienteService;
import com.llira.yelpcamp.sb.apirest.service.CloudinaryService;
import com.llira.yelpcamp.sb.apirest.util.Util;
import com.llira.yelpcamp.sb.apirest.web.rest.vm.ClienteVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8000"})
@RestController
@RequestMapping("/api/v1")
public class ClienteRestController {
    private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);
    private final ClienteService clienteService;
    private final CloudinaryService cloudinaryService;

    private ClienteRestController(ClienteService clienteService, CloudinaryService cloudinaryService) {
        this.clienteService = clienteService;
        this.cloudinaryService = cloudinaryService;
    }

    /**
     * Método para obtener una lista de registros sin paginación
     *
     * @return {@link ResponseEntity}
     */
    @GetMapping("/clientes/index")
    public ResponseEntity<?> findAll() {
        Map<String, Object> params = new HashMap<>();
        List<ClienteVM> clientes;
        try {
            clientes = clienteService.findAll()
                    .stream()
                    .map(ClienteVM::new)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al consultar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(clientes);
    }

    /**
     * Método para obtener una lista paginada de los registros
     *
     * @param page  página actual
     * @param limit limite de registros
     * @param sort  campo a ordenar
     * @param order forma ascendente o descendente
     * @return {@link ResponseEntity}
     */
    @GetMapping("/clientes")
    public ResponseEntity<?> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                     @RequestParam(name = "limit", defaultValue = "5") Integer limit,
                                     @RequestParam(name = "sort", defaultValue = "id") String sort,
                                     @RequestParam(name = "order", defaultValue = "desc") String order) {
        log.info("Obtenemos la lista paginada desde el controlador");
        Map<String, Object> params = new HashMap<>();
        Page<ClienteVM> clientes;
        try {
            Pageable pageable = order.equals("asc")
                    ? PageRequest.of(page, limit, Sort.by(sort))
                    : PageRequest.of(page, limit, Sort.by(sort).descending());
            clientes = clienteService.findAll(pageable).map(ClienteVM::new);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al consultar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(clientes);
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
        ClienteVM clienteVM = new ClienteVM(cliente);
        return ResponseEntity.ok().body(clienteVM);
    }

    /**
     * Método para crear un nuevo objeto
     *
     * @param data objeto a insertar en la base de datos
     * @return {@link ResponseEntity}
     */
    @PostMapping("/clientes")
    public ResponseEntity<?> insert(@Valid @RequestBody Cliente data, BindingResult result) {
        Map<String, Object> params = new HashMap<>();
        if (result.hasErrors()) {
            return Util.validateField(result);
        }
        try {
            clienteService.save(data);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al insertar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // @TODO - Validar si enviar el objeto creado
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
            return Util.validateField(result);
        }
        if (cliente == null) {
            params.put("message", "No se encontro el cliente.");
            return new ResponseEntity<>(params, HttpStatus.NOT_FOUND);
        }
        try {
            cliente.setApellidoPaterno(data.getApellidoPaterno());
            cliente.setApellidoMaterno(data.getApellidoMaterno());
            cliente.setNombre(data.getNombre());
            cliente.setCorreo(data.getCorreo());
            cliente.setFechaCreacion(data.getFechaCreacion());
            clienteService.save(cliente);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al editar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
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
            deleteImage(id);
            clienteService.delete(id);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al eliminar en la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.noContent().build();
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
        Cliente cliente;
        try {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            if (bufferedImage == null) {
                params.put("message", "Solo se admiten imagenes con extensión .jpeg, .jpg y .png.");
                return new ResponseEntity<>(params, HttpStatus.BAD_REQUEST);
            }
            Map<?, ?> result = cloudinaryService.upload(image);
            cliente = clienteService.findById(id);
            deleteImage(id);
            cliente.setImagen(result.get("secure_url").toString());
            cliente.setPublicId(result.get("public_id").toString());
            clienteService.save(cliente);
        } catch (IOException e) {
            params.put("message", "Se produjo un error al subir la imagen.");
            params.put("error", e.getMessage() + ": " + e.getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataAccessException e) {
            params.put("message", "Se produjo un error al subir la imagen a la base de datos.");
            params.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(params, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ClienteVM clienteVM = new ClienteVM(cliente);
        params.put("cliente", clienteVM);
        params.put("message", "Se ha subido correctamente la foto.");
        return new ResponseEntity<>(params, HttpStatus.CREATED);
    }

    /**
     * Método para eliminar una foto del cliente
     *
     * @param id identificador único del registro
     */
    private void deleteImage(Long id) {
        log.info("Vamos a eliminar la foto.");
        Cliente cliente = clienteService.findById(id);
        try {
            if (cliente.getPublicId() != null) {
                cloudinaryService.delete(cliente.getPublicId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}