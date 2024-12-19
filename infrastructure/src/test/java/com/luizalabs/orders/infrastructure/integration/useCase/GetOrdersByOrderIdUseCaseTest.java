package com.luizalabs.orders.infrastructure.integration.useCase;

import com.luizalabs.orders.application.order.GetOrdersByOrderIdUseCase;
import com.luizalabs.orders.domain.exception.OrderNotFoundException;
import com.luizalabs.orders.infrastructure.integration.IntegrationTest;
import com.luizalabs.orders.infrastructure.jpa.entity.OrderEntity;
import com.luizalabs.orders.infrastructure.jpa.entity.ProductEntity;
import com.luizalabs.orders.infrastructure.jpa.entity.UserOrderEntity;
import com.luizalabs.orders.infrastructure.jpa.repositories.UserOrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

public class GetOrdersByOrderIdUseCaseTest extends IntegrationTest {

    @Autowired
    private UserOrderJpaRepository userOrderJpaRepository;

    @Autowired
    private GetOrdersByOrderIdUseCase getOrdersByOrderIdUseCase;

    @Test
    @DisplayName("Should be return a specific order existing in the database")
    void getOrderById() {

        createOrderInDatabase();

        final var input = new GetOrdersByOrderIdUseCase.Input(1L);

        final var output = getOrdersByOrderIdUseCase.execute(input).orderUserVo();

        assertNotNull(output);
        assertEquals(1L, output.getOrderId());
        assertEquals(2, output.getProducts().size());
        assertEquals(12.25, output.getProducts().get(0).getValue());
        assertEquals(7.10, output.getProducts().get(1).getValue());

    }

    @Test
    @DisplayName("Should return a Not Found")
    void orderNotFound() {

        final var input = new GetOrdersByOrderIdUseCase.Input(1L);

        assertThrows(OrderNotFoundException.class, () -> getOrdersByOrderIdUseCase.execute(input));

    }

    private void createOrderInDatabase() {
        var products = List.of(
                new ProductEntity(null,1L, 12.25),
                new ProductEntity(null,2L, 7.10)
        );

        var orders = List.of(
                OrderEntity.builder()
                        .orderId(1L)
                        .date(LocalDate.now())
                        .products(products)
                        .build()
        );

        final var userOrder = UserOrderEntity.builder()
                .userId(1L)
                .name("Leonardo Arruda")
                .orders(orders)
                .build();

        userOrderJpaRepository.save(userOrder);
    }
}
