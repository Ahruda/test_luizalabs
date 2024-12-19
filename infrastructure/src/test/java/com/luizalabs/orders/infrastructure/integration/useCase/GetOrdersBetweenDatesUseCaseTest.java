package com.luizalabs.orders.infrastructure.integration.useCase;

import com.luizalabs.orders.application.order.GetOrdersBetweenDatesUseCase;
import com.luizalabs.orders.infrastructure.integration.IntegrationTest;
import com.luizalabs.orders.infrastructure.jpa.entity.OrderEntity;
import com.luizalabs.orders.infrastructure.jpa.entity.UserOrderEntity;
import com.luizalabs.orders.infrastructure.jpa.repositories.UserOrderJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class GetOrdersBetweenDatesUseCaseTest extends IntegrationTest {

    @Autowired
    private UserOrderJpaRepository userOrderJpaRepository;

    @Autowired
    private GetOrdersBetweenDatesUseCase getOrdersBetweenDatesUseCase;

    @Test
    @DisplayName("Deve retornar todas as vendas no intervalo da data")
    void getOrderBetweenDates() {

        createOrderInDatabase();

        final var input = new GetOrdersBetweenDatesUseCase.Input(
                LocalDate.of(2024, 1,1),
                LocalDate.of(2024, 1,2)
        );

        var output = getOrdersBetweenDatesUseCase
                .execute(input).ordersUserVo();

        userOrderJpaRepository.findAllByOrderByUserIdAsc();

        assertNotNull(output);
        assertEquals(2, output.size());

        final var validOrderIds = List.of(1L, 2L);

        output.forEach(order ->
                assertTrue(validOrderIds.contains(order.getOrderId())));

        assertTrue(output.stream()
                        .noneMatch(order -> order.getOrderId().equals(3L)));

    }

    private void createOrderInDatabase() {
        IntStream.rangeClosed(1, 3).forEach(i -> {
            var order = OrderEntity.builder()
                    .orderId((long) i)
                    .date(LocalDate.of(2024, 1, i))
                    .products(List.of())
                    .build();

            var userOrder = UserOrderEntity.builder()
                    .userId((long) i)
                    .name("Leonardo Arruda")
                    .orders(List.of(order))
                    .build();

            userOrderJpaRepository.save(userOrder);
        });

    }
}
