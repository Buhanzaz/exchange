package com.exchange.rate;

import com.exchange.rate.api.QuoteManager;
import com.exchange.rate.spi.ExchangeRateProvider;

public class FinanceExchangeRateProvider implements ExchangeRateProvider {
    @Override
    public QuoteManager create() {
        return new QuoteManagerImpl();
    }
}
