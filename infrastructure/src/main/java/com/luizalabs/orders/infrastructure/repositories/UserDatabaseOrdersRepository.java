package com.luizalabs.orders.infrastructure.repositories;

import com.luizalabs.orders.domain.user.UserOrder;
import com.luizalabs.orders.domain.user.UserOrdersRepository;
import com.luizalabs.orders.infrastructure.jpa.entity.UserOrderEntity;
import com.luizalabs.orders.infrastructure.jpa.repositories.UserOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDatabaseOrdersRepository implements UserOrdersRepository {

    private final UserOrderJpaRepository userOrderJpaRepository;

    @Override
    public void create(List<UserOrder> userOrders) {
        var userEntities = UserOrderEntity.of(userOrders);
        userOrderJpaRepository.saveAll(userEntities);
    }

    @Override
    public List<UserOrder> findAll() {
        return userOrderJpaRepository.findAllByOrderByUserIdAsc().stream().map(UserOrderEntity::toUser).toList();
    }

    @Override
    public Optional<UserOrder> findUserByOrderId(Long orderId) {
        return userOrderJpaRepository.findUserByOrderId(orderId).map(UserOrderEntity::toUser);
    }

}
