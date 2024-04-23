package com.exchange.rate;

import com.exchange.rate.api.Quote;
import com.exchange.rate.api.QuoteManager;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public class QuoteManagerImpl implements QuoteManager {

    private static final String URL_PROVIDER = "https://query2.finance.yahoo.com/v6/finance/quoteSummary/%s=X?modules=summaryDetail";
    private final OkHttpClient client = new OkHttpClient();

    private static Quote parseResult(Map qrw) {
        Quote quote = null;

        if (qrw != null) {
            final Map quoteSummery = (Map) qrw.get("quoteSummery");

            if (quoteSummery != null) {
                final List<Map> results = (List<Map>) quoteSummery.get("results");

                if (results != null) {
                    final Map resultArray = results.get(0);

                    if (resultArray != null) {
                        final Map summaryDetail = (Map) resultArray.get("summaryDetail");

                        if (summaryDetail != null) {
                            quote = constructQuote(summaryDetail);
                        }
                    }
                }
            }
        }

        return quote;
    }

    private static Quote constructQuote(Map summaryDetail) {
        final String currency = (String) summaryDetail.get("currency");
        final Map ask = (Map) summaryDetail.get("ask");
        final Map bid = (Map) summaryDetail.get("bid");
        final BigDecimal askPrice = (BigDecimal) ask.get("raw");
        final BigDecimal bidPrice = (BigDecimal) bid.get("raw");

        if (askPrice != null && bidPrice != null) {
            return new Quote(currency, askPrice, bidPrice);
        }
        return null;
    }

    @Override
    public List<Quote> getQuotes(String baseCurrency, LocalDate date) {
        List<String> currentQuery = new ArrayList<>();

        Currency.getAvailableCurrencies().forEach(currency -> {
            if (!baseCurrency.equals(currency.getCurrencyCode())) {
                currentQuery.add(String.format(URL_PROVIDER, baseCurrency + currency.getCurrencyCode()));
            }
        });

        final List<Quote> quotes = new ArrayList<>();

        for (String url : currentQuery) {
            String response = doGetRequest(url);
            if (response != null) {
                final Quote map = map(response);
                if (map != null) {
                    quotes.add(map);
                }
            }
        }

        return quotes;
    }

    private Quote map(String response) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            final Map qrw = jsonb.fromJson(response, Map.class);
            return parseResult(qrw);
        } catch (Exception e) {
            System.out.println("Error while trying to read response");
            return null;
        }
    }

    private String doGetRequest(String url) {
        System.out.println(url);
        Request request = new Request.Builder().url(url).build();
        Response response;

        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }
}
