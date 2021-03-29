package io.t11.tradeEngine.dao;

import io.t11.tradeEngine.model.MarketData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MarketDataRepository extends CrudRepository<MarketData,Long> {
    Optional<MarketData> findByTicker(String ticker);
}
