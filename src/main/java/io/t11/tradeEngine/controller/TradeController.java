package io.t11.tradeEngine.controller;

import io.t11.tradeEngine.dto.MarketDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    RestTemplate restTemplate=new RestTemplate();

    private static Logger logger = LoggerFactory.getLogger((TradeController.class));
    //lets work here later

    private List<MarketDataDto> dataList = new ArrayList<>();

    @PostMapping("/data")
    public void receiveMarketData(@RequestBody MarketDataDto marketDataDto){
        dataList.add(marketDataDto);
    }

    @GetMapping("/history")
    public List<MarketDataDto> getAllMarketData(){
        return dataList;
    }

    public void subscribeToMarketData(){

    }

}
