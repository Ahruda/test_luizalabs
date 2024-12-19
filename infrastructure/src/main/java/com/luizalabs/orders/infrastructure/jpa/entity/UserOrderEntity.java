package com.luizalabs.orders.infrastructure.jpa.entity;

import com.luizalabs.orders.domain.user.UserOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderEntity {

    @Id
    private Long userId;

    private String name;

    @OneToMany(cascade= CascadeType.ALL)
    private List<OrderEntity> orders;

    public static List<UserOrderEntity> of(List<UserOrder> userOrders) {
        return userOrders.stream()
                .map(user -> UserOrderEntity.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .orders(OrderEntity.of(user.getOrders()))
                        .build())
                .collect(Collectors.toList());
    }

    public UserOrder toUser() {
        return UserOrder.builder()
                .userId(this.userId)
                .name(this.name)
                .orders(this.orders.stream()
                        .map(OrderEntity::toOrder).toList())
                .build();
    }
}
