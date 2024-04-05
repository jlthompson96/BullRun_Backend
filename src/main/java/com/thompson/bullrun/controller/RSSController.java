package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class RSSController {

    @GetMapping("/rss/{symbol}")
    public ResponseEntity<String> getRSSFeed(@PathVariable String symbol) {
        log.info("Getting RSS feed for symbol: {}", symbol);
        final String url = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=" + symbol;
        RestTemplate restTemplate = new RestTemplate();
        String rssFeed = restTemplate.getForObject(url, String.class);

        assert rssFeed != null;
        JSONObject json = XML.toJSONObject(rssFeed);
        JSONArray items = json.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");

        log.info("RSS feed for symbol: {} retrieved successfully", symbol);
        log.info("RSS feed: {}", items);
        return ResponseEntity.ok(items.toString());
    }
}