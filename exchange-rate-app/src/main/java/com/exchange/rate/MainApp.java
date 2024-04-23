package com.exchange.rate;

import com.exchange.rate.api.Quote;

import java.time.LocalDate;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        ExchangeRate.providers().forEach(provider -> {
            System.out.printf("Retrieving USD quotes from provider : %s\n", provider);

            List<Quote> quotes = provider.create().getQuotes("USD", LocalDate.now());

            System.out.printf("%14s%12s|%12s\n", "", "Ask", "Bid");
            System.out.println("----------------------------------------");

            quotes.forEach(quote -> System.out.printf("USD --> %s : %s%n", quote.getCurrency(), String.format("%12f|%12f", quote.getAsk(), quote.getBid())));
        });
    }
}