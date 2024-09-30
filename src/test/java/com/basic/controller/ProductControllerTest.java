package com.basic.controller;


import com.basic.dto.input.ProductInputDto;
import com.basic.dto.output.ProductDto;
import com.basic.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@WebMvcTest(ProductController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    ProductDto product;

    @BeforeEach
    public void setup(){

        product = new ProductDto();
        product.setPrice(1.0);
        product.setName("product");
        product.setId(UUID.randomUUID().toString());

    }

    @Test
    @Order(1)
    public void saveProductTest() throws Exception{
        // precondition
        given(productService.create(any(ProductInputDto.class))).willReturn(product);

        // action
        ResultActions response = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        // verify
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is(product.getName())))
                .andExpect(jsonPath("$.price",
                        is(product.getPrice())))
                .andExpect(jsonPath("$.id",
                        is(product.getId())));
    }

    @Test
    @Order(2)
    public void getProductTest() throws Exception{
        // precondition
        List<ProductDto> products = new ArrayList<>();
        products.add(product);
        products.add(new ProductDto());
        given(productService.getAllProducts()).willReturn(products);

        // action
        ResultActions response = mockMvc.perform(get("/products"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(products.size())));

    }

    @Test
    @Order(3)
    public void getByIdProductTest() throws Exception{
        // precondition
        given(productService.getProductById(product.getId())).willReturn(product);

        // action
        ResultActions response = mockMvc.perform(get("/products/{id}", product.getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name",
                        is(product.getName())))
                .andExpect(jsonPath("$.price",
                        is(product.getPrice())))
                .andExpect(jsonPath("$.id",
                        is(product.getId())));
    }


    //Update employee
    @Test
    @Order(4)
    public void updateProductTest() throws Exception{
        // precondition
        given(productService.getProductById(product.getId())).willReturn(product);
        product.setName("new product");
        product.setPrice(5.0);
        given(productService.update(product.getId(),new ProductInputDto("new product", 5.0)))
                .willReturn(product);

        // action
        ResultActions response = mockMvc.perform(put("/products/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.price", is(product.getPrice())));
    }


    @Test
    public void deleteProductTest() throws Exception{
        // precondition
        willDoNothing().given(productService).delete(product.getId());

        // action
        ResultActions response = mockMvc.perform(delete("/products/{id}", product.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
