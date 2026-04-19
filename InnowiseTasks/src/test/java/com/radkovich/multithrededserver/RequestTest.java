package com.radkovich.multithrededserver;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestTest {

    @Test
    void shouldParseGetRequest() throws IOException {
        String request = "GET /text.txt HTTP/1.1";

        BufferedReader br = new BufferedReader(new StringReader(request));
        String requestLine = br.readLine();

        String[] parts = requestLine.split(" ");
        String method = parts[0];
        String path = parts[1];

        assertEquals("GET", method);
        assertEquals("/text.txt", path);
    }
}
