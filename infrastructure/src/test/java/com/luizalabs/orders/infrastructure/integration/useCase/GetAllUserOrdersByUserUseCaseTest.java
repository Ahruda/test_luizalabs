package com.luizalabs.orders.infrastructure.integration.useCase;

import com.luizalabs.orders.application.order.GetAllUserOrdersByUserUseCase;
import com.luizalabs.orders.domain.user.UserOrder;
import com.luizalabs.orders.infrastructure.integration.IntegrationTest;
import com.luizalabs.orders.infrastructure.jpa.entity.OrderEntity;
import com.luizalabs.orders.infrastructure.jpa.entity.UserOrderEntity;
import com.luizalabs.orders.infrastructure.jpa.repositories.UserOrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetAllUserOrdersByUserUseCaseTest extends IntegrationTest {

    @Autowired
    private UserOrderJpaRepository userOrderJpaRepository;

    @Autowired
    private GetAllUserOrdersByUserUseCase getAllUserOrdersByUserUseCase;

    @Test
    @DisplayName("Should be return all orders")
    void getAllOrders() {
        createOrdersInDatabase(3);

        List<UserOrder> output = getAllUserOrdersByUserUseCase.execute().userOrders();

        assertNotNull(output);
        assertEquals(3, output.size());
    }

    private void createOrdersInDatabase(Integer count) {
        IntStream.rangeClosed(1, count).forEach(i -> {
            var order = List.of(OrderEntity.builder()
                    .orderId((long) i)
                    .date(LocalDate.of(2024, 1, i))
                    .products(List.of())
                    .build());

            var userOrder = UserOrderEntity.builder()
                    .userId((long) i)
                    .name("User " + i)
                    .orders(order)
                    .build();

            userOrderJpaRepository.save(userOrder);
        });
    }
}
