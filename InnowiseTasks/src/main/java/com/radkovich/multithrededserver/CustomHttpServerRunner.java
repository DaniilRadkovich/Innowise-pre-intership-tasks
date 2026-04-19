package com.radkovich.multithrededserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.radkovich.multithrededserver.Handler.handleClient;

public class CustomHttpServerRunner {
    private static final int PORT = 8080;
    private static final String RES_DIR = "C:\\Users\\mradk\\Desktop\\Java projects\\InnowiseTasks\\src\\main\\resources\\files";

    private static final int WORKER_THREADS = 10;
    private static final ExecutorService pool = Executors.newFixedThreadPool(WORKER_THREADS);
    private static ServerSocket serverSocket;
    private static boolean isRunning = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter command to server:");
        System.out.println("'start' to start server");
        System.out.println("'stop' to stop server");
        System.out.println("'exit' to shutdown server");

        while (true) {
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "start" -> beforeStart();
                case "stop" -> beforeStop();
                case "exit" -> {
                    System.exit(0);
                    return;
                }
                default -> System.out.println("Invalid command!");
            }
        }
    }

    static void startServer() {
        isRunning = true;

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("Server started on port: " + PORT + "!");
                System.out.println("Waiting for your requests...");
                runServerSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void runServerSocket() {
        while (isRunning) {
            try {
                Socket socket = serverSocket.accept();
                pool.submit(() -> handleClient(socket, RES_DIR));
            } catch (IOException e) {
                if (isRunning) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void stopServer() {
        isRunning = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            pool.shutdown();
            System.out.println("Server stopped!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void beforeStart() {
        if (isRunning) {
            System.out.println("Server is already running!");
        } else {
            startServer();
        }
    }

    private static void beforeStop() {
        if (!isRunning) {
            System.out.println("Server is not running!");
        } else {
            stopServer();
        }
    }
}
