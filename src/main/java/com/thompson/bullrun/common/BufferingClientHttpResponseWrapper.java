package com.thompson.bullrun.common;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

    private final ClientHttpResponse response;
    private byte[] body;

    public BufferingClientHttpResponseWrapper(ClientHttpResponse response) throws IOException {
        this.response = response;
        this.body = StreamUtils.copyToByteArray(response.getBody());
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(body);
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return response.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return response.getStatusText();
    }

    @Override
    public void close() {
        response.close();
    }

    @Override
    public org.springframework.http.HttpStatus getStatusCode() throws IOException {
        return (org.springframework.http.HttpStatus) response.getStatusCode();
    }

    @Override
    public org.springframework.http.HttpHeaders getHeaders() {
        return response.getHeaders();
    }
}