package com.exchange.rate;

import com.exchange.rate.exception.ProviderNotFoundException;
import com.exchange.rate.spi.ExchangeRateProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ExchangeRate {

    private static final String DEFAULT_PROVIDER = "com.exchange.rate.FinanceExchangeRateProvider";

    // All Providers
    public static List<ExchangeRateProvider> providers() {
        List<ExchangeRateProvider> services = new ArrayList<>();
        ServiceLoader<ExchangeRateProvider> loader = ServiceLoader.load(ExchangeRateProvider.class);
        loader.forEach(services::add);
        return services;
    }

    // Default provider
    public static ExchangeRateProvider provider() {
        return provider(DEFAULT_PROVIDER);
    }

    public static ExchangeRateProvider provider(String providerName) {
        ServiceLoader<ExchangeRateProvider> loader = ServiceLoader.load(ExchangeRateProvider.class);

        for (ExchangeRateProvider provider : loader) {
            if (providerName.equals(provider.getClass().getName())) {
                return provider;
            }
        }

        throw new ProviderNotFoundException("Exchange rate provider %s not found".formatted(providerName));
    }
}
