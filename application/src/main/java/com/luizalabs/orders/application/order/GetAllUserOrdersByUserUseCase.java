package com.luizalabs.orders.application.order;

import com.luizalabs.orders.application.NullaryUseCase;
import com.luizalabs.orders.domain.user.UserOrder;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GetAllUserOrdersByUserUseCase implements NullaryUseCase<GetAllUserOrdersByUserUseCase.Output> {

    private final UserOrdersRepository userOrdersRepository;

    public GetAllUserOrdersByUserUseCase(UserOrdersRepository userOrdersRepository) {
        this.userOrdersRepository = userOrdersRepository;
    }

    @Override
    public Output execute() {

        log.info("I=Starting_execution c=GetAllUserOrdersByUserUseCase m=execute");

        return new Output(userOrdersRepository.findAll());
    }

    public record Output(List<UserOrder> userOrders) {}
}
