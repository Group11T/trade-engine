package io.t11.tradeEngine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.t11.tradeEngine.dao.MarketDataRepository;
import io.t11.tradeEngine.dto.MarketDataDto;
import io.t11.tradeEngine.model.MarketData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketDataService {

    @Autowired
    MarketDataRepository marketDataRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private static Logger logger = LoggerFactory.getLogger((MarketDataService.class));

    public MarketDataService(MarketDataRepository marketDataRepository, RestTemplate restTemplate) {
        this.marketDataRepository = marketDataRepository;
        this.restTemplate = restTemplate;
    }

    public MarketData saveMarketData(MarketDataDto data){
        MarketData marketData = new MarketData();
        marketData.setTicker(data.getTicker());
        marketData.setAskPrice(data.getAskPrice());
        marketData.setBidPrice(data.getBidPrice());
        marketData.setLast_Traded_Price(data.getLastTradedPrice());
       return marketDataRepository.save(marketData);
    }

    public MarketData updateMarketData(MarketDataDto data){
        MarketData marketData = marketDataRepository.findByTicker(data.getTicker()).get();
        marketData.setAskPrice(data.getAskPrice());
        marketData.setBidPrice(data.getBidPrice());
        marketData.setLast_Traded_Price(data.getLastTradedPrice());
        marketData.setTicker(data.getTicker());
        marketDataRepository.save(marketData);

        logger.info("Publishing trade complete event for order in wait");
        eventPublisher.publishEvent(data);
        return marketData;
    }

    public Optional<MarketData> findDataByTicker(String ticker){
        return marketDataRepository.findByTicker(ticker);
    }

//    @Scheduled(fixedDelay = 60000)
    public void getUpdatedMarketData(){
        String url = "https://exchange.matraining.com/md/";
        List<Object> marketData = restTemplate.getForObject(url, ArrayList.class);

        logger.info("Data in from market");
        ObjectMapper objectMapper = new ObjectMapper();

        List marketDataList = marketData.stream()
                .map(item->objectMapper.convertValue(item, MarketDataDto.class))
                .map(item->{
                    MarketData marketDataItem;
                    Optional<MarketData> data = findDataByTicker(item.getTicker());
                    if(data.isPresent()){
                        marketDataItem = updateMarketData(item);
                    }
                    else{
                        marketDataItem =  saveMarketData(item);
                    }
                    return marketDataItem;
                })
                .collect(Collectors.toList());

        logger.info("Market data updated successfully");
    }
}
