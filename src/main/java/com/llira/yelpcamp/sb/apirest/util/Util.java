package com.llira.yelpcamp.sb.apirest.util;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {

    /**
     * Método que convierte el formato de la fecha a dd/MM/yyyy
     *
     * @param date fecha a convertir
     * @return fecha convertida al formato estandar
     */
    public static String convertDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(date);
    }

    /**
     * Método para validar los campos obligatorios
     *
     * @param result objeto que contiene el resultado de las validaciones
     * @return {@link ResponseEntity}
     */
    public static ResponseEntity<?> validateField(BindingResult result) {
        Map<String, Object> params = new HashMap<>();
        List<String> errors = result.getFieldErrors().stream()
                .map(e -> "El campo [" + e.getField().toUpperCase() + "] " + e.getDefaultMessage())
                .collect(Collectors.toList());
        params.put("errors", errors);
        params.put("message", "La petición contiene errores.");
        return ResponseEntity.badRequest().body(params);
    }
}
