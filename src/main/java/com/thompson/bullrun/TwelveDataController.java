package com.thompson.bullrun;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/marketMovers")
public class TwelveDataController {

    @Value("${twelveDataAPIKey}")
    private String apiKey;

    //Market Movers
    private final String apiUrl = "https://api.twelvedata.com/market_movers/price?apikey={apiKey}";

    //Stock Price
    private final String apiUrls = "https://api.twelvedata.com/price?symbol=TSLA&&apikey={apiKey}";


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/getMovers")
    public ResponseEntity<String> retrieveDataFromApi() {
        // Set the API key in the URL
        String url = apiUrls.replace("{apiKey}", apiKey);

        // Make the GET request and handle the response using ResponseEntity
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // You can access various details from the ResponseEntity if needed
        // For example, HttpStatus, headers, etc.
        // HttpStatus statusCode = responseEntity.getStatusCode();
        // HttpHeaders headers = responseEntity.getHeaders();

        return responseEntity;
    }
}
