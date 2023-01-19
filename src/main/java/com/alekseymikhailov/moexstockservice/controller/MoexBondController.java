package com.alekseymikhailov.moexstockservice.controller;

import com.alekseymikhailov.moexstockservice.dto.FigiesDto;
import com.alekseymikhailov.moexstockservice.dto.StocksDto;
import com.alekseymikhailov.moexstockservice.dto.StocksPricesDto;
import com.alekseymikhailov.moexstockservice.dto.TickersDto;
import com.alekseymikhailov.moexstockservice.service.BondService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bonds")
@RequiredArgsConstructor
public class MoexBondController {

    private final BondService bondService;

    @PostMapping("/getBondsByTickers")
    public StocksDto getBondsFromMoex(@RequestBody TickersDto tickersDto){
        return bondService.getBondsFromMoex(tickersDto);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPricesByFigies(@RequestBody FigiesDto figiesDto){
        return bondService.getPricesByFigies(figiesDto);
    }
}
