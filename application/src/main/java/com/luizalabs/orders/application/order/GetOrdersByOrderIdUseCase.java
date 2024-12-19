package com.luizalabs.orders.application.order;

import com.luizalabs.orders.application.UseCase;
import com.luizalabs.orders.application.vo.OrderUserVo;
import com.luizalabs.orders.domain.exception.OrderNotFoundException;
import com.luizalabs.orders.domain.order.OrderRepository;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetOrdersByOrderIdUseCase implements UseCase<GetOrdersByOrderIdUseCase.Input, GetOrdersByOrderIdUseCase.Output> {

    private final OrderRepository orderRepository;
    private final UserOrdersRepository userOrdersRepository;

    public GetOrdersByOrderIdUseCase(OrderRepository orderRepository, UserOrdersRepository userOrdersRepository) {
        this.orderRepository = orderRepository;
        this.userOrdersRepository = userOrdersRepository;
    }

    public Output execute(Input input) {
        log.info("I=Starting_execution c=GetOrdersByOrderIdUseCase m=execute orderId={}", input.id());

        return orderRepository.findById(input.id)
                .flatMap(order -> userOrdersRepository.findUserByOrderId(order.getOrderId())
                    .map(user -> new Output(
                        OrderUserVo.builder()
                                .orderId(order.getOrderId())
                                .userId(user.getUserId())
                                .name(user.getName())
                                .date(order.getDate())
                                .products(order.getProducts())
                                .build()
                    )))
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public record Input(Long id) {
    }

    public record Output(OrderUserVo orderUserVo) {
    }

}
