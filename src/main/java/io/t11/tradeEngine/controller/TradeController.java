package io.t11.tradeEngine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.t11.tradeEngine.service.TradeEnginePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private static Logger logger = LoggerFactory.getLogger((TradeController.class));
    //lets work here later
}
