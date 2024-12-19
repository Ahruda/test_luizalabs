package com.luizalabs.orders.infrastructure.jpa.repositories;

import com.luizalabs.orders.infrastructure.jpa.entity.UserOrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserOrderJpaRepository extends CrudRepository<UserOrderEntity, Long> {

    List<UserOrderEntity> findAllByOrderByUserIdAsc();

    @Query("SELECT u FROM UserOrderEntity u JOIN u.orders o WHERE o.orderId = :orderId")
    Optional<UserOrderEntity> findUserByOrderId(@Param("orderId") Long orderId);
}
