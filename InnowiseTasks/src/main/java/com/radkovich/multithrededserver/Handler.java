package com.radkovich.multithrededserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class Handler {
    private static final String TEXT_PLAIN = "text/plain";

    private Handler() {
    }

    public static void handleClient(Socket socket, String directory) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream output = socket.getOutputStream()) {
            String requestLine = input.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            System.out.println("Received request: " + requestLine);

            String[] parts = requestLine.split(" ");
            String requestMethod = parts[0];
            String requestPath = parts[1];

            if (!requestMethod.equals("GET")) {
                send405(output);
                return;
            }

            if (requestPath.equals("/")) {
                sendRoot(output);
                return;
            }

            handleFileRequest(output, directory, requestPath);
        } catch (Exception e) {
            send500(socket);
            e.printStackTrace();
        } finally {
            closeSocket(socket);
        }
    }

    private static void sendRoot(OutputStream output) throws IOException {
        sendResponse(output, 200, "OK", TEXT_PLAIN, "Hello from Custom Server!");
    }

    private static void send405(OutputStream output) throws IOException {
        sendResponse(output, 405, "Method not allowed", TEXT_PLAIN, "Only GET method supported!");
    }

    private static void send404(OutputStream output) throws IOException {
        sendResponse(output, 404, "Not found!", TEXT_PLAIN, "404 Not found");
    }

    private static void send500(Socket socket) {
        try {
            OutputStream out = socket.getOutputStream();
            sendResponse(out, 500, "Internal Server Error", TEXT_PLAIN, "500 Internal Server Error");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleFileRequest(OutputStream output, String directory, String path) throws IOException {
        File file = new File(directory + path);

        if (!file.exists() || file.isDirectory()) {
            send404(output);
            return;
        }
        byte[] content = Files.readAllBytes(file.toPath());
        String contentType = ContentType.fromFileName(file.getName());

        sendResponse(output, 200, "OK", contentType, content);
    }


    private static void sendResponse(OutputStream out, int statusCode, String statusText, String type, String body) throws IOException {
        sendResponse(out, statusCode, statusText, type, body.getBytes());
    }

    private static void sendResponse(OutputStream output, int statusCode, String statusText, String type, byte[] body) throws IOException {
        PrintStream printStream = new PrintStream(output);
        printStream.printf("HTTP/1.1 %s %s%n", statusCode, statusText + "\r\n");
        printStream.printf("Content-Type: %s%n", type);
        printStream.print("Content-Length: " + body.length + "\r\n");
        printStream.print("Connection: close\r\n");
        printStream.print("\r\n");
        printStream.flush();

        output.write(body);
        output.flush();
    }
}
