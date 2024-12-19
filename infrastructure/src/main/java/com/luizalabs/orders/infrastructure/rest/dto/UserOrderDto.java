package com.luizalabs.orders.infrastructure.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.luizalabs.orders.domain.user.UserOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class UserOrderDto {

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private List<OrderDto> orders;

    public static UserOrderDto of (UserOrder userOrder) {
        return UserOrderDto.builder()
                .userId(userOrder.getUserId())
                .name(userOrder.getName())
                .orders(OrderDto.of(userOrder.getOrders()))
                .build();
    }
}
