package com.basic.converter;

import com.basic.dto.input.ProductInputDto;
import com.basic.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InputProductToModelConverter implements Converter<ProductInputDto, Product> {

    @Override
    public Product convert(ProductInputDto product) {
        Product output = new Product();
        output.setId(UUID.randomUUID());
        output.setName(product.name());
        output.setPrice(product.price());

        return output;
    }

}
