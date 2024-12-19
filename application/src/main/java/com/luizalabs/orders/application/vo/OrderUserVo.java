package com.luizalabs.orders.application.vo;

import com.luizalabs.orders.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class OrderUserVo {

    private Long orderId;
    private Long userId;
    private String name;
    private LocalDate date;
    private List<Product> products;

}
