package com.luizalabs.orders.infrastructure.jpa.repositories;

import com.luizalabs.orders.infrastructure.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderJpaRepository extends CrudRepository<OrderEntity, Long> {

    @Query("SELECT o FROM OrderEntity o WHERE o.date BETWEEN :startDate AND :endDate order by o.date")
    List<OrderEntity> findOrdersBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
