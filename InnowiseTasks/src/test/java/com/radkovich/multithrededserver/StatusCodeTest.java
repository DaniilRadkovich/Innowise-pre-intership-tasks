package com.radkovich.multithrededserver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusCodeTest {
    @BeforeAll
    static void startServer() throws InterruptedException {
        CustomHttpServerRunner.startServer();
        Thread.sleep(500);
    }

    @Test
    void shouldReturn200ForRoot() throws IOException {
        try (Socket socket = new Socket("localhost", 8080)) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
            outputStream.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = br.readLine();

            assertTrue(response.contains("HTTP/1.1 200 OK"));
        }
    }

    @Test
    void shouldReturn404ForUnknownPath() throws IOException {
        try (Socket socket = new Socket("localhost", 8080)) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("GET /blablabla.txt HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
            outputStream.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = br.readLine();

            assertTrue(response.contains("HTTP/1.1 404"));
        }
    }

    @Test
    void shouldReturn405ForNotGetMethod() throws IOException {
        try (Socket socket = new Socket("localhost", 8080)) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("POST / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
            outputStream.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = br.readLine();

            assertTrue(response.contains("HTTP/1.1 405"));
        }
    }
}
