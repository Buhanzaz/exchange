package com.exchange.rate.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Quote {
    private String currency;
    private BigDecimal ask;
    private BigDecimal bid;
    private LocalDate date;

    public Quote(String currency, BigDecimal ask, BigDecimal bid) {
        this.currency = currency;
        this.ask = ask;
        this.bid = bid;
        this.date = LocalDate.now();
    }
}
