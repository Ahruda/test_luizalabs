package com.luizalabs.orders.infrastructure.integration.useCase;

import com.luizalabs.orders.application.file.OrdersFileProcessorUseCase;
import com.luizalabs.orders.domain.user.UserOrder;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import com.luizalabs.orders.infrastructure.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdersFileProcessorUseCaseTest extends IntegrationTest {

    @Autowired
    private OrdersFileProcessorUseCase ordersFileProcessorUseCase;

    @Autowired
    private UserOrdersRepository userOrdersRepository;

    @Test
    @DisplayName("Should be process one order with two products and save them in the database")
    void processOneOrderWithTwoProducts() {
        String fileContent =
                "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308\n"+
                "0000000070                              Palmer Prosacco00000007530000000004     1009.5420210308";

        MockMultipartFile file = new MockMultipartFile("file", "orders.txt", "text/plain", fileContent.getBytes());

        OrdersFileProcessorUseCase.Input input = new OrdersFileProcessorUseCase.Input(file);
        ordersFileProcessorUseCase.execute(input);

        List<UserOrder> userOrders = userOrdersRepository.findAll();

        assertEquals(1, userOrders.size());
        final var userOrder = userOrders.get(0);
        assertEquals(70L, userOrder.getUserId());
        assertEquals("Palmer Prosacco", userOrder.getName());

        assertEquals(1, userOrder.getOrders().size());
        final var order = userOrder.getOrders().get(0);
        assertEquals(753L, order.getOrderId());
        assertEquals(LocalDate.of(2021, 3, 8), order.getDate());

        assertEquals(2, order.getProducts().size());
        final var product1 = order.getProducts().get(0);
        assertEquals(3L, product1.getProductIdFile());
        assertEquals(1836.74, product1.getValue());

        final var product2 = order.getProducts().get(1);
        assertEquals(4L, product2.getProductIdFile());
        assertEquals(1009.54, product2.getValue());
    }
}
