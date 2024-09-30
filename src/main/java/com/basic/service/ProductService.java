package com.basic.service;

import com.basic.converter.InputProductToModelConverter;
import com.basic.converter.ProductToOutputConverter;
import com.basic.dto.input.ProductInputDto;
import com.basic.dto.output.ProductDto;
import com.basic.model.Product;
import com.basic.repository.ProductRepository;
import com.basic.validator.ValidUUID;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final ProductToOutputConverter outputConverter;
    private final InputProductToModelConverter inputConverter;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
        this.outputConverter = new ProductToOutputConverter();
        this.inputConverter = new InputProductToModelConverter();
    }

    public List<ProductDto> getAllProducts() {
        try {
            logger.info("Retrieving all products");

            return repository.findAll()
                    .stream()
                    .map(outputConverter::convert)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("An error occurred when trying to retrieve products", e);
            throw new RuntimeException(e);
        }
    }

    public ProductDto getProductById(@Valid @ValidUUID String id) {
        ProductDto output;

        logger.info("Retrieving product by id = {}", id);
        Optional<Product> product = repository.findById(UUID.fromString(id));
        if (product.isPresent()) {
            output = outputConverter.convert(product.get());
        } else {
            logger.error("An error occurred when trying to retrieve product {}", id);
            throw new NoSuchElementException();
        }

        return output;
    }

    public ProductDto create(@Valid ProductInputDto input) {
        Product model = inputConverter.convert(input);
        try {
           logger.info("Creating product: {}", input);
           model = repository.save(model);
           return outputConverter.convert(model);

        } catch (Exception e) {
            logger.error("An error occurred when trying to build product", e);
            throw new RuntimeException(e);
        }

    }

    public ProductDto update(@Valid @ValidUUID(message = "Not a valid UUID") String id,
                             @Valid ProductInputDto input) {

        logger.info("Updating product id = {}", id);
        Optional<Product> model = repository.findById(UUID.fromString(id));
        if(model.isEmpty()) {
            logger.info("Product wasn't found id = {}", id);
            throw new NoSuchElementException();
        }
        model.get().setName(input.name());
        model.get().setPrice(input.price());

        return outputConverter.convert(repository.save(model.get()));
    }

    public void delete(@Valid @ValidUUID(message = "Not a valid UUID") String id) {

        logger.info("Trying to delete product id = {}", id);
        Optional<Product> model = repository.findById(UUID.fromString(id));
        if(model.isEmpty()) {
            logger.error("Product not found id = {}", id);
            throw new NoSuchElementException();
        }
        repository.delete(model.get());

    }

}
