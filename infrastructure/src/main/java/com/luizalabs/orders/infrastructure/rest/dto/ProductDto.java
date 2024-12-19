package com.luizalabs.orders.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luizalabs.orders.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class ProductDto {

    @JsonProperty("product_id")
    private Long productIdFile;

    private Double value;

    public static List<ProductDto> of(List<Product> products) {
        return products.stream()
                .map(product -> ProductDto.builder()
                        .productIdFile(product.getProductIdFile())
                        .value(product.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
