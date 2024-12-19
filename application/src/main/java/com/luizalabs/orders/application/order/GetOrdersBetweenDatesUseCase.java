package com.luizalabs.orders.application.order;

import com.luizalabs.orders.application.UseCase;
import com.luizalabs.orders.application.vo.OrderUserVo;
import com.luizalabs.orders.domain.order.OrderRepository;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class GetOrdersBetweenDatesUseCase implements UseCase<GetOrdersBetweenDatesUseCase.Input, GetOrdersBetweenDatesUseCase.Output> {

    private final OrderRepository orderRepository;
    private final UserOrdersRepository userOrdersRepository;

    public GetOrdersBetweenDatesUseCase(OrderRepository orderRepository, UserOrdersRepository userOrdersRepository) {
        this.orderRepository = orderRepository;
        this.userOrdersRepository = userOrdersRepository;
    }

    @Override
    public Output execute(Input input) {
        log.info("I=Starting_execution c=GetOrdersBetweenDatesUseCase m=execute");

        var orders = orderRepository.findOrdersBetweenDates(input.startDate, input.endDate);

        var orderUserVos = orders.stream()
                .flatMap(order -> userOrdersRepository.findUserByOrderId(order.getOrderId())
                    .map(user -> OrderUserVo.builder()
                        .orderId(order.getOrderId())
                        .userId(user.getUserId())
                        .name(user.getName())
                        .date(order.getDate())
                        .products(order.getProducts())
                        .build())
                    .stream()
                ).toList();


        return new Output(orderUserVos);

    }

    public record Input(LocalDate startDate, LocalDate endDate) {
    }

    public record Output(List<OrderUserVo> ordersUserVo) {
    }

}
