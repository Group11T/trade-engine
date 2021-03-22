package io.t11.tradeEngine.dao;

import io.t11.tradeEngine.dto.CreatedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatedOrderRepository extends JpaRepository<CreatedOrder,Long> {
}
