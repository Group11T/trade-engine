package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.t11.tradeEngine.model.Order;

public interface ITradeEnginePublisher {

    void publishTradeToRegister(Order order) throws JsonProcessingException;

    void publishOrdersToExchangeConnectivityQueue(Order order) throws JsonProcessingException;
}
