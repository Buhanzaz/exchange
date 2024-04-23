package com.exchange.rate.spi;

import com.exchange.rate.api.QuoteManager;

public interface ExchangeRateProvider {
    QuoteManager create();
}
