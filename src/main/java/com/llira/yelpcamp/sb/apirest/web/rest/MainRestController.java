package com.llira.yelpcamp.sb.apirest.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainRestController {

    @GetMapping("/")
    public ResponseEntity<?> index(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        String url = request.getRequestURL().toString().concat("swagger-ui.html");
        params.put("message", "Bienvenido a Yelpcamp Rest Api");
        params.put("author", "Luis Lira");
        params.put("version", "1.0");
        params.put("documentation", url);
        return new ResponseEntity<>(params, HttpStatus.OK);
    }
}
