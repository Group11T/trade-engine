package io.t11.tradeEngine.dao;

import io.t11.tradeEngine.dto.OrderDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDtoRepository extends CrudRepository<OrderDto,Long> {

    List<OrderDto> findByPrice(double price);
}
