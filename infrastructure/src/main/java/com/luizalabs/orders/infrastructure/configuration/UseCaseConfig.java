package com.luizalabs.orders.infrastructure.configuration;

import com.luizalabs.orders.application.order.GetAllUserOrdersByUserUseCase;
import com.luizalabs.orders.application.order.GetOrdersBetweenDatesUseCase;
import com.luizalabs.orders.application.order.GetOrdersByOrderIdUseCase;
import com.luizalabs.orders.application.file.OrdersFileProcessorUseCase;
import com.luizalabs.orders.domain.order.OrderRepository;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final UserOrdersRepository userOrdersRepository;
    private final OrderRepository orderRepository;

    public UseCaseConfig(UserOrdersRepository userOrdersRepository, OrderRepository orderRepository) {
        this.userOrdersRepository = userOrdersRepository;
        this.orderRepository = orderRepository;
    }

    @Bean
    public OrdersFileProcessorUseCase ordersFileProcessorUseCase() {
        return new OrdersFileProcessorUseCase(userOrdersRepository);
    }

    @Bean
    public GetAllUserOrdersByUserUseCase getAllOrdersUseCase() {
        return new GetAllUserOrdersByUserUseCase(userOrdersRepository);
    }

    @Bean
    public GetOrdersByOrderIdUseCase getOrdersByOrderIdUseCase() {
        return new GetOrdersByOrderIdUseCase(orderRepository, userOrdersRepository);
    }

    @Bean
    public GetOrdersBetweenDatesUseCase getOrdersBetweenDatesUseCase() {
        return new GetOrdersBetweenDatesUseCase(orderRepository, userOrdersRepository);
    }


}
