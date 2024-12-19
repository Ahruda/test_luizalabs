package com.luizalabs.orders.domain.order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById (Long id);

    List<Order> findOrdersBetweenDates(LocalDate startDate, LocalDate endDate);
}
