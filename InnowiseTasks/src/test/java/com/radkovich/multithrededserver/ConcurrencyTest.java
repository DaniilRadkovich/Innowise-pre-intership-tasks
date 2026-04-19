package com.radkovich.multithrededserver;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcurrencyTest {

    @BeforeAll
    static void startServer() throws InterruptedException {
        CustomHttpServerRunner.startServer();
        Thread.sleep(1000);
    }

    @Test
    void shouldHandle10ConcurrentRequests() {
        int requestsAmount = 10;

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < requestsAmount; i++) {
            futures.add(CompletableFuture.supplyAsync(this::sendRequest));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (CompletableFuture<Boolean> f : futures) {
            assertTrue(f.join());
        }
    }

    private boolean sendRequest() {
        try (Socket socket = new Socket("localhost", 8080)) {

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
            outputStream.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = br.readLine();

            return response.contains("HTTP/1.1 200 OK");
        } catch (IOException e) {
            return false;
        }
    }
}
