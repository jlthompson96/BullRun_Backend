package com.thompson.bullrun.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class RestLoggerInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RestLoggerInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);
        logResponse(responseWrapper);
        return responseWrapper;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException {
        log.info("======================================= Request ========================================");
        log.info("URI: {}", request.getURI());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("HTTP Headers: {}", request.getHeaders());
        log.info("Request Body: {}", new String(body, StandardCharsets.UTF_8));
        log.info("========================================================================================");
        log.info("");
        log.info("");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            inputStringBuilder.append(bufferedReader.lines().collect(Collectors.joining("\n")));
        }
        log.info("======================================= Response =======================================");
        log.info("HTTP Status Code: {}", response.getStatusCode());
        log.info("HTTP Status Text: {}", response.getStatusText());
        log.info("Response Body: {}", inputStringBuilder.toString());
        log.info("========================================================================================");
        log.info("");
        log.info("");
    }
}