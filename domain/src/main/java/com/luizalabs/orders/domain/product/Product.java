package com.luizalabs.orders.domain.product;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Product {

    private Long productId;
    private Long productIdFile;
    private Double value;

}
