package com.luizalabs.orders.infrastructure.repositories;

import com.luizalabs.orders.domain.order.Order;
import com.luizalabs.orders.domain.order.OrderRepository;
import com.luizalabs.orders.infrastructure.jpa.entity.OrderEntity;
import com.luizalabs.orders.infrastructure.jpa.entity.UserOrderEntity;
import com.luizalabs.orders.infrastructure.jpa.repositories.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderDatabaseRepository implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(OrderEntity::toOrder);
    }

    @Override
    public List<Order> findOrdersBetweenDates(LocalDate startDate, LocalDate endDate) {
        return orderJpaRepository.findOrdersBetweenDates(startDate, endDate).stream().map(OrderEntity::toOrder).toList();
    }
}
