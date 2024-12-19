package com.luizalabs.orders.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserOrdersRepository {

    void create (List<UserOrder> userOrders);

    List<UserOrder> findAll ();

    Optional<UserOrder> findUserByOrderId(Long orderId);

}
