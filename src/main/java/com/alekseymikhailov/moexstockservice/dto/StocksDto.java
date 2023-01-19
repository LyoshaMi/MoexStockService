package com.alekseymikhailov.moexstockservice.dto;

import com.alekseymikhailov.moexstockservice.model.Stock;
import lombok.Value;

import java.util.List;

@Value
public class StocksDto {
    List<Stock> stocks;
}
