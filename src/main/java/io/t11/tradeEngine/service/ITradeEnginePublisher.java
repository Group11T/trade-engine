package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ITradeEnginePublisher {

    void publishTradeToRegister(Object message) throws JsonProcessingException;

    void publishOrdersToExchangeConnectivityQueue(String message) throws JsonProcessingException;
}
