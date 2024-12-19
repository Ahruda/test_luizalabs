package com.luizalabs.orders.infrastructure.rest;

import com.luizalabs.orders.application.order.GetAllUserOrdersByUserUseCase;
import com.luizalabs.orders.application.order.GetOrdersBetweenDatesUseCase;
import com.luizalabs.orders.application.order.GetOrdersByOrderIdUseCase;
import com.luizalabs.orders.application.vo.OrderUserVo;
import com.luizalabs.orders.domain.user.UserOrder;
import com.luizalabs.orders.infrastructure.rest.dto.OrderDto;
import com.luizalabs.orders.infrastructure.rest.dto.UserOrderDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "orders")
public class OrdersController {

    private final GetAllUserOrdersByUserUseCase getAllUserOrdersByUserUseCase;
    private final GetOrdersByOrderIdUseCase getOrdersByOrderIdUseCase;
    private final GetOrdersBetweenDatesUseCase getOrdersBetweenDatesUseCase;

    public OrdersController(GetAllUserOrdersByUserUseCase getAllUserOrdersByUserUseCase,
                            GetOrdersByOrderIdUseCase getOrdersByOrderIdUseCase,
                            GetOrdersBetweenDatesUseCase getOrdersBetweenDatesUseCase) {
        this.getAllUserOrdersByUserUseCase = getAllUserOrdersByUserUseCase;
        this.getOrdersByOrderIdUseCase = getOrdersByOrderIdUseCase;
        this.getOrdersBetweenDatesUseCase = getOrdersBetweenDatesUseCase;
    }

    @GetMapping
    public ResponseEntity<List<UserOrderDto>> getAll() {
        List<UserOrder> output = getAllUserOrdersByUserUseCase.execute()
                .userOrders();

        return ResponseEntity.ok(output.stream()
                .map(UserOrderDto::of).toList());

    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        final var input = new GetOrdersByOrderIdUseCase.Input(id);

        OrderUserVo output = getOrdersByOrderIdUseCase.execute(input)
                .orderUserVo();

        return ResponseEntity.ok(OrderDto.of(output));
    }

    @GetMapping("/between-dates/{startDate}/{endDate}")
    public ResponseEntity<List<OrderDto>> getBetweenDates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        final var input = new GetOrdersBetweenDatesUseCase.Input(
                startDate,
                endDate
        );

        List<OrderUserVo> output = getOrdersBetweenDatesUseCase.execute(input)
                .ordersUserVo();

        return ResponseEntity.ok(output.stream()
                .map(OrderDto::of).toList());
    }

}
