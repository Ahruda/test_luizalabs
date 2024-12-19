package com.luizalabs.orders.infrastructure.jpa.entity;

import com.luizalabs.orders.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    private Long orderId;

    @OneToMany(cascade=CascadeType.ALL)
    private List<ProductEntity> products;

    private LocalDate date;

    public static List<OrderEntity> of(List<Order> orders) {
        return orders.stream()
                .map(order -> OrderEntity.builder()
                        .orderId(order.getOrderId())
                        .products(ProductEntity.of(order.getProducts()))
                        .date( order.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    public Order toOrder() {
        return Order.builder()
                .orderId(this.orderId)
                .products(this.getProducts().stream()
                        .map(ProductEntity::toProduct).toList())
                .date(this.date)
                .build();
    }

}
