package com.thompson.bullrun.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NonNull;
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
import java.util.UUID;
import java.util.stream.Collectors;

public class RestLoggerInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RestLoggerInterceptor.class);
    private static final String LOG_SEPARATOR = "========================================================================================";

    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request, byte @NonNull [] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        String uniqueId = UUID.randomUUID().toString();
        logRequest(request, body, uniqueId);
        ClientHttpResponse response = execution.execute(request, body);
        ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);
        logResponse(responseWrapper, uniqueId);
        return responseWrapper;
    }

    private void logRequest(HttpRequest request, byte[] body, String uniqueId) throws IOException {
        log.info("======================================= Request ========================================");
        log.info("Unique ID: {}", uniqueId);
        log.info("URI: {}", request.getURI());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("HTTP Headers: {}", request.getHeaders());
        log.info("Request Body: {}", new String(body, StandardCharsets.UTF_8));
        log.info(LOG_SEPARATOR);
        log.info("");
        log.info("");
    }

    private void logResponse(ClientHttpResponse response, String uniqueId) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            inputStringBuilder.append(bufferedReader.lines().collect(Collectors.joining("\n")));
        }
        String responseBody = inputStringBuilder.toString();
        String formattedResponseBody = formatJson(responseBody);

        log.info("======================================= Response =======================================");
        log.info("Unique ID: {}", uniqueId);
        log.info("HTTP Status Code: {}", response.getStatusCode());
        log.info("HTTP Status Text: {}", response.getStatusText());
        log.info("Response Body: {}", formattedResponseBody);
        log.info(LOG_SEPARATOR);
        log.info("");
        log.info("");
    }

    private String formatJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object jsonObject = mapper.readValue(json, Object.class);
            ObjectWriter writer = mapper.writer(SerializationFeature.INDENT_OUTPUT);
            return writer.writeValueAsString(jsonObject);
        } catch (IOException e) {
            return json;
        }
    }
}