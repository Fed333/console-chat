package com.vntu.console.chat.app.network;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.handler.ServerAcceptConnectionHandler;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public class SocketServer {

    private static final String QUIT_COMMAND = "QUIT";

    private final ServerOutMessagePrinter messagePrinter;

    private final ChatUserService chatUserService;

    private final AtomicBoolean serverShutdown;

    private final ServerAcceptConnectionHandler acceptConnectionHandler;

    public void startServer(ServerSocket serverSocket, String[] args) {
        log.info("Start server application...");

        Scanner in = new Scanner(System.in);
        Thread serverPromptThread = new Thread(() -> {
            log.info("Start server prompt thread...");
            while (true) {
                messagePrinter.printPrompt();
                String command = in.nextLine();
                if (command.equalsIgnoreCase(QUIT_COMMAND)) {
                    //TODO disconnect all connected clients
                    serverShutdown.set(true);
                    break;
                }
            }
        });
        serverPromptThread.start();

        Thread serverAcceptConnectionsThread = new Thread(() -> {
            log.info("Start server accept connections thread...");

            while (true) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                    messagePrinter.printlnPromptMessage("Client connected: " + clientSocket.getInetAddress());

                    acceptConnectionHandler.startConnectionHandling(clientSocket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        serverAcceptConnectionsThread.start();

        try {
            serverPromptThread.join();
            serverAcceptConnectionsThread.join();
        } catch (InterruptedException e) {
            log.error("Couldn't join server threads.", e);
            throw new RuntimeException(e);
        }
    }
}