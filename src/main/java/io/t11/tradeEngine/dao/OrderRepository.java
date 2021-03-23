package io.t11.tradeEngine.dao;

import io.t11.tradeEngine.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
