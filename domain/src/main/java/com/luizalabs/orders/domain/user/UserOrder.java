package com.luizalabs.orders.domain.user;

import com.luizalabs.orders.domain.order.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class UserOrder {

    private Long userId;
    private String name;
    private List<Order> orders;

}