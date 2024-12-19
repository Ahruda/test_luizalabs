package com.luizalabs.orders.domain.order;

import com.luizalabs.orders.domain.product.Product;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Order {

    private Long orderId;
    private List<Product> products;
    private LocalDate date;

}
