package io.t11.tradeEngine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.t11.tradeEngine.dto.MarketDataDto;
import io.t11.tradeEngine.model.MarketData;
import io.t11.tradeEngine.service.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private static Logger logger = LoggerFactory.getLogger((TradeController.class));

    @Autowired
    MarketDataService marketDataService;

    @PostMapping("/data")
    public void receiveMarketData(@RequestBody List<Object> marketData) throws IOException {
        logger.info("Data in from market");
        ObjectMapper objectMapper = new ObjectMapper();

        List marketDataList = marketData.stream()
                .map(item->objectMapper.convertValue(item, MarketDataDto.class))
                .map(item->{
                    MarketData marketDataItem;
                    Optional<MarketData> data = marketDataService.findDataByTicker(item.getTicker());
                    if(data.isPresent()){
                       marketDataItem = marketDataService.updateMarketData(item);
                    }
                    else{
                       marketDataItem =  marketDataService.saveMarketData(item);
                    }
                    return marketDataItem;
                })
                .collect(Collectors.toList());

        logger.info("Market data updated successfully");
    }

}