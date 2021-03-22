package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.t11.tradeEngine.dto.CreatedOrder;

public interface ITradeEnginePublisher {

    void publishTradeToRegister(CreatedOrder createdOrder) throws JsonProcessingException;

    void publishOrdersToExchangeConnectivityQueue(CreatedOrder createdOrder) throws JsonProcessingException;
}
