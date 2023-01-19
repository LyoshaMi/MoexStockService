package com.alekseymikhailov.moexstockservice.service;

import com.alekseymikhailov.moexstockservice.dto.BondDto;
import com.alekseymikhailov.moexstockservice.exception.LimitRequestsException;
import com.alekseymikhailov.moexstockservice.moexclient.CorporateBondsClient;
import com.alekseymikhailov.moexstockservice.moexclient.GovBondsClient;
import com.alekseymikhailov.moexstockservice.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BondRepository {
    private final CorporateBondsClient corporateBondsClient;
    private final GovBondsClient govBondsClient;
    private final Parser parser;
    @Cacheable(value = "coprs")
    public List<BondDto> getCorporateBonds(){
        log.info("Getting corporate bonds from Moex");
        String xmlFromMoex = corporateBondsClient.getBondsFromMoex();
        List<BondDto> bondDtos = parser.parse(xmlFromMoex);
        if (bondDtos.isEmpty()){
            log.error("Moex isn`t answering for getting corporate bonds");
            throw new LimitRequestsException("Moex isn`t answering for getting corporate bonds");
        }
        return bondDtos;
    }

    @Cacheable(value = "govs")
    public List<BondDto> getGovBonds(){
        log.info("Getting goverment bonds from Moex");
        String xmlFromMoex = govBondsClient.getBondsFromMoex();
        List<BondDto> bondDtos = parser.parse(xmlFromMoex);
        if (bondDtos.isEmpty()){
            log.error("Moex isn`t answering for getting goverment bonds");
            throw new LimitRequestsException("Moex isn`t answering for getting goverment bonds");
        }
        return bondDtos;
    }
}
