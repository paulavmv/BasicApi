package com.basic.converter;

import com.basic.dto.output.ProductDto;
import com.basic.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToOutputConverter implements Converter<Product, ProductDto> {

    @Override
    public ProductDto convert(Product product) {
        ProductDto output = new ProductDto();
        output.setId(product.getId().toString());
        output.setName(product.getName());
        output.setPrice(product.getPrice());
        output.setCreatedAt(product.getCreatedAt());
        output.setUpdatedAt(product.getUpdatedAt());

        return output;
    }

}
