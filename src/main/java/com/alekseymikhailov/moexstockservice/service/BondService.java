package com.alekseymikhailov.moexstockservice.service;

import com.alekseymikhailov.moexstockservice.dto.*;
import com.alekseymikhailov.moexstockservice.exception.BondNotFoundException;
import com.alekseymikhailov.moexstockservice.exception.LimitRequestsException;
import com.alekseymikhailov.moexstockservice.model.Currency;
import com.alekseymikhailov.moexstockservice.model.Stock;
import com.alekseymikhailov.moexstockservice.moexclient.CorporateBondsClient;
import com.alekseymikhailov.moexstockservice.moexclient.GovBondsClient;
import com.alekseymikhailov.moexstockservice.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BondService {
    private final BondRepository bondRepository;
    private final CacheManager cacheManager;
    public StocksDto getBondsFromMoex(TickersDto tickersDto){
        List<BondDto> allBonds = new ArrayList<>();
        allBonds.addAll(bondRepository.getCorporateBonds());
        allBonds.addAll(bondRepository.getGovBonds());

        List<BondDto> resultBonds = allBonds.stream()
                .filter(b -> tickersDto.getTickers().contains(b.getTicker()))
                .collect(Collectors.toList());

        List<Stock> stocks = resultBonds.stream()
                .map(bondDto -> {
                    return Stock.builder()
                            .ticker(bondDto.getTicker())
                            .name(bondDto.getName())
                            .figi(bondDto.getTicker())
                            .type("Bond")
                            .currency(Currency.RUB)
                            .source("MOEX")
                            .build();
                })
                .collect(Collectors.toList());
        return new StocksDto(stocks);
    }

    public StocksPricesDto getPricesByFigies(FigiesDto figiesDto) {
        log.info("Request for figies {}", figiesDto.getFigies());
        List<String> figies = new ArrayList<>(figiesDto.getFigies());
        List<BondDto> allBonds = new ArrayList<>();
        allBonds.addAll(bondRepository.getCorporateBonds());
        allBonds.addAll(bondRepository.getGovBonds());
        figies.removeAll(allBonds.stream()
                .map(bondDto -> bondDto.getTicker())
                .collect(Collectors.toList()));
        if (!figies.isEmpty()){
            throw new BondNotFoundException(String.format("Bonds %s not found", figies));
        }
        List<StockPrice> prices = allBonds.stream()
                .filter(bondDto -> figiesDto.getFigies().contains(bondDto.getTicker()))
                .map(bondDto -> new StockPrice(bondDto.getTicker(), bondDto.getPrice() * 10))
                .collect(Collectors.toList());
        return new StocksPricesDto(prices);
    }
}
