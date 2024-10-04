package com.basic.controller;

import com.basic.dto.input.ProductInputDto;
import com.basic.dto.output.GenericResponse;
import com.basic.dto.output.ProductDto;
import com.basic.service.ProductService;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductDto>> list() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(
                new GenericResponse.Builder()
                        .data(service.getProductById(id))
                        .success(true)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateObject(@PathVariable String id, @RequestBody ProductInputDto input)
    {
        return ResponseEntity.ok(
                new GenericResponse.Builder()
                        .data(service.update(id, input))
                        .success(true)
                        .build());
    }

    @PostMapping
    public ResponseEntity<GenericResponse> create(@RequestBody ProductInputDto input) {
        logger.info("Creating object: {}", input);
        return ResponseEntity.ok(
                new GenericResponse.Builder()
                        .data(service.create(input))
                        .success(true)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteObject(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok(
                new GenericResponse.Builder()
                        .message("Object was removed")
                        .success(true)
                        .build());
    }
}
