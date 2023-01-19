package com.alekseymikhailov.moexstockservice.parser;

import com.alekseymikhailov.moexstockservice.dto.BondDto;

import java.util.List;

public interface Parser {
    List<BondDto> parse(String ratesAsString);
}
