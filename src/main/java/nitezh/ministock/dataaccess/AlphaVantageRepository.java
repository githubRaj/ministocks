package nitezh.ministock.dataaccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nitezh.ministock.domain.StockQuote;
import nitezh.ministock.utils.Cache;
import nitezh.ministock.utils.UrlDataTools;

/**
 * Created by GRao on 3/22/2018.
 */

public class AlphaVantageRepository {

    private static final String API_KEY = "ZKD8M6L9CEQAK89H";
    private String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_";
    private String cryptoBaseURL = "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_";
    private final FxChangeRepository fxChangeRepository;

    AlphaVantageRepository(FxChangeRepository fxChangeRepository) {
        this.fxChangeRepository = fxChangeRepository;
    }

    /*public HashMap<String, StockQuote> getQuotes(Cache cache, String symbols) {
        HashMap<String, StockQuote> quotes = new HashMap<>();
        HashMap<String, String> fxChanges = this.fxChangeRepository.getChanges(cache, symbols);
        JSONArray jsonArray;
        JSONObject quoteJson;

        try {
            jsonArray = this.retrieveQuotesAsJson(cache, symbols);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    quoteJson = jsonArray.getJSONObject(i);
                    StockQuote quote = new StockQuote(
                            quoteJson.optString("symbol"),
                            quoteJson.optString("price"),
                            quoteJson.optString("change"),
                            quoteJson.optString("percent"),
                            quoteJson.optString("exchange"),
                            quoteJson.optString("volume"),
                            quoteJson.optString("name"),
                            fxChanges.get(quoteJson.optString("symbol")),
                            Locale.US);
                    quotes.put(quote.getSymbol(), quote);
                }
            }
        } catch (JSONException e) {
            return null;
        }

        return quotes;
    }*/

    //for regular stock exchange
    JSONArray retrieveQuotesAsJson(Cache cache, String timeInterval, String symbols) throws JSONException {
        String url = buildRequestURL(timeInterval, symbols);
        String quotesString = UrlDataTools.getCachedUrlData(url, cache, 300);
        JSONObject quotesJson = new JSONObject(quotesString);
        JSONArray quotes = new JSONArray();
        Iterator<String> stringKey = quotesJson.keys();
        quotesJson = quotesJson.getJSONObject(stringKey.next());
        for (Iterator key = quotesJson.keys(); key.hasNext();)
        {
            String objKey = (String)key.next();
            JSONObject quoteJson =  quotesJson.getJSONObject(objKey);
            JSONObject data = new JSONObject();
            data.put("date", objKey);
            data.put("open", quoteJson.optString("1. open"));
            data.put("high", quoteJson.optString("2. high"));
            data.put("low", quoteJson.optString("3. low"));
            data.put("close", quoteJson.optString("4. close"));
            data.put("volume", quoteJson.optString("5. volume"));
            quotes.put(data);
       }

        return quotes;
    }


    //For Crypto Currency
    JSONArray retrieveQuotesAsJson(Cache cache, String timeInterval, String symbols, String market) throws JSONException {
        String url = buildRequestURL(timeInterval, symbols, market);
        String quotesString = UrlDataTools.getCachedUrlData(url, cache, 300);
        JSONObject quotesJson = new JSONObject(quotesString);
        JSONArray quotes = new JSONArray();
        Iterator<String> stringKey = quotesJson.keys();
        quotesJson = quotesJson.getJSONObject(stringKey.next());
        for (Iterator key = quotesJson.keys(); key.hasNext();)
        {
            String objKey = (String)key.next();
            JSONObject quoteJson =  quotesJson.getJSONObject(objKey);
            JSONObject data = new JSONObject();
            data.put("date", objKey);
            data.put("open", quoteJson.optString("1a. open ("+market+")"));
            data.put("high", quoteJson.optString("2a. high ("+market+")"));
            data.put("low", quoteJson.optString("3a. low ("+market+")"));
            data.put("close", quoteJson.optString("4a. close ("+market+")"));
            data.put("volume", quoteJson.optString("5. volume"));
            data.put("market cap", quoteJson.optString("6. market cap (USD)")); //market cap always in USD
            quotes.put(data);
        }

        return quotes;
    }

    private String buildRequestURL(String timeInterval, String symbol){
        return baseURL+timeInterval+"&symbol="+symbol+"&interval=15min&outputsize=full&apikey="+API_KEY;
    }

    private String buildRequestURL(String timeInterval, String symbol, String market){
        return cryptoBaseURL+timeInterval+"&symbol="+symbol+"&market="+market+"&apikey="+API_KEY;
    }
}
