package com.llira.yelpcamp.sb.apirest.web.rest;

import com.llira.yelpcamp.sb.apirest.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CloudinaryRestController {
    private final Logger log = LoggerFactory.getLogger(CloudinaryRestController.class);

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/cloudinary")
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile image) throws IOException {
        Map<?, ?> params = cloudinaryService.upload(image);
        log.info("Parámetros: {}", params);
        return new ResponseEntity<>(params, HttpStatus.CREATED);
    }


    @DeleteMapping("/cloudinary/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) throws IOException {
        Map<?, ?> params = cloudinaryService.delete(id);
        log.info("Parámetros: {}", params);
        return new ResponseEntity<>(params, HttpStatus.NO_CONTENT);
    }
}
