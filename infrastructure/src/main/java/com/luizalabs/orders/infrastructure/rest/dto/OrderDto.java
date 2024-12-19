package com.luizalabs.orders.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luizalabs.orders.application.vo.OrderUserVo;
import com.luizalabs.orders.domain.order.Order;
import com.luizalabs.orders.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private Double total;

    private LocalDate date;

    private List<ProductDto> products;

    public static OrderDto of(OrderUserVo orderUserVo) {
        return OrderDto.builder()
                .orderId(orderUserVo.getOrderId())
                .userId(orderUserVo.getUserId())
                .name(orderUserVo.getName())
                .total(calculateOrderValue(orderUserVo.getProducts()))
                .date(orderUserVo.getDate())
                .products(ProductDto.of(orderUserVo.getProducts()))
                .build();
    }

    public static List<OrderDto> of(List<Order> orders) {
        return orders.stream()
                .map(order -> OrderDto.builder()
                        .orderId(order.getOrderId())
                        .total(calculateOrderValue(order.getProducts()))
                        .date(order.getDate())
                        .products(ProductDto.of(order.getProducts()))
                        .build())
                .collect(Collectors.toList());
    }

    private static Double calculateOrderValue(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getValue)
                .sum();
    }
}
