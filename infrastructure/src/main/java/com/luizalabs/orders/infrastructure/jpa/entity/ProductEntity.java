package com.luizalabs.orders.infrastructure.jpa.entity;

import com.luizalabs.orders.domain.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long productId;

    private Long productIdFile;

    private Double value;

    public static List<ProductEntity> of(List<Product> products) {
        return products.stream()
                .map(product ->
                        ProductEntity.builder()
                                .productIdFile(product.getProductIdFile())
                                .value(product.getValue())
                                .build())
                .collect(Collectors.toList());
    }

    public Product toProduct() {
        return Product.builder()
                .productId(this.productId)
                .productIdFile(this.productIdFile)
                .value(this.value)
                .build();
    }
}
