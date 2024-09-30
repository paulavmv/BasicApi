package com.basic.controller;

import com.basic.dto.input.ProductInputDto;
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
    public ResponseEntity<ProductDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateObject(@PathVariable String id, @RequestBody ProductInputDto input)
    {
        return ResponseEntity.ok(service.update(id, input));
    }

    @PatchMapping("/{id}")
    public String partialUpdate(@PathVariable String id) {
        return "updated";
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductInputDto input) {
        logger.info("Creating object: {}", input);
        return ResponseEntity.ok(service.create(input));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObject(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("Object was removed");
    }
}
