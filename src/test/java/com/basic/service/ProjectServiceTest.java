package com.basic.service;

import com.basic.dto.input.ProductInputDto;
import com.basic.dto.output.ProductDto;
import com.basic.model.Product;
import com.basic.repository.ProductRepository;
import com.basic.validator.ValidUUID;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @InjectMocks
    ProductService service;

    @Mock
    ProductRepository repository;

    @Spy
    private static Product spiedProduct;

    private static Product product;
    private static final String invalidUUID = "avc4";
    private static final UUID validUUID = UUID.randomUUID();


    @BeforeAll
    public static void setup() {
        product = new Product();
        product.setId(validUUID);
        product.setName("name");
        product.setPrice(9.0);

        spiedProduct = new Product();
        spiedProduct.setId(validUUID);
        spiedProduct.setName("name");
        spiedProduct.setPrice(9.0);

    }

    @Test
    @DisplayName("Method should return all products when called")
    void shouldReturnAllProducts() {
        var products = Arrays.asList(new Product[]{product, product});
        Mockito.when(repository.findAll()).thenReturn(products);

        Assertions.assertEquals(products.size(), service.getAllProducts().size());
    }

    @Test
    @DisplayName("Get By Id Should throw exception for invalid uuid")
    void shouldValidateUUID() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.getProductById(invalidUUID));
    }

    @Test
    @DisplayName("getProductById should return object for valid uuid")
    void shouldReturnObject() {
        Mockito.when(repository.findById(validUUID))
                .thenReturn(Optional.ofNullable(product));

        Assertions.assertEquals(product.getId().toString(),
                service.getProductById(validUUID.toString()).getId());

    }

    @Test
    @DisplayName("getProductById should throw exception when object doesn't exist")
    void shouldThrowNotFound() {
        Mockito.when(repository.findById(validUUID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> service.getProductById(validUUID.toString()));

    }

    @DisplayName("Should create object for valid input")
    @Test
    void createShouldThrowExceptionForInvalidInput() {
        ProductInputDto dto = new ProductInputDto("name", 9.0);
        Mockito.when(repository.save(any()))
                .thenReturn(product);

        Assertions.assertDoesNotThrow(() -> service.create(dto));
    }


    @Test
    @DisplayName("Update method should update all fields")
    void updateShouldUpdateAllFields() {

        ProductInputDto dto = new ProductInputDto("new name", 5.0);
        Mockito.when(repository.findById(validUUID))
                .thenReturn(Optional.ofNullable(spiedProduct));

        Mockito.when(repository.save(spiedProduct))
                .thenReturn(product);

        service.update(validUUID.toString(), dto);
        Assertions.assertEquals(spiedProduct.getName(), dto.name());
    }

    @Test
    @DisplayName("Update should throw exception when no object was found")
    void shouldThrowExceptionUpdateNoObjectFound() {
        ProductInputDto dto = new ProductInputDto("new name", 5.0);
        Mockito.when(repository.findById(validUUID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> service.update(validUUID.toString(), dto));

    }

    @Test
    @DisplayName("Delete should throw exception when no object was found")
    void shouldThrowExceptionDeleteNoObjectFound() {
        Mockito.when(repository.findById(validUUID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class,
                () -> service.delete(validUUID.toString()));

    }

    @Test
    @DisplayName("Delete should be called for valid uuid")
    void shouldCallDelete() {
        Mockito.when(repository.findById(validUUID))
                .thenReturn(Optional.ofNullable(product));

        service.delete(validUUID.toString());

        verify(repository, times(1)).delete(any());
    }


}
