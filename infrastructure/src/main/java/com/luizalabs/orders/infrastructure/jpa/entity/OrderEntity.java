package com.luizalabs.orders.infrastructure.jpa.entity;

import com.luizalabs.orders.domain.order.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
