package com.luizalabs.orders.domain.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class OrderFile {

    private Long userId;
    private String name;
    private Long orderId;
    private Long productId;
    private Double valueProduct;
    private LocalDate date;

}
