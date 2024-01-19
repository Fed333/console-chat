package com.vntu.console.chat.app.network;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.handler.ServerAcceptConnectionHandler;
import com.vntu.console.chat.app.handler.ServerMessagePromptCommandHandler;
import com.vntu.console.chat.app.network.component.BroadcastMessagesSender;
import com.vntu.console.chat.app.network.protocol.ProtocolMessageBuilder;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.CLOSE_SERVER_PROMPT_COMMAND;

@Slf4j
@RequiredArgsConstructor
public class SocketServer {

    private final ServerOutMessagePrinter messagePrinter;
    private final AtomicBoolean serverShutdown;
    private final ServerAcceptConnectionHandler acceptConnectionHandler;
    private final ServerMessagePromptCommandHandler serverMessagePromptHandler;
    private final BroadcastMessagesSender broadcastMessagesSender;
    private final ProtocolMessageBuilder protocolMessageBuilder;

    public void startServer(ServerSocket serverSocket, String[] args) {
        log.info("Start server application...");

        Scanner in = new Scanner(System.in);
        Thread serverPromptThread = new Thread(() -> {
            log.info("Start server prompt thread...");
            while (true) {
                messagePrinter.printPrompt();
                String command = in.nextLine();
                if (command.toUpperCase().startsWith(CLOSE_SERVER_PROMPT_COMMAND)) {
                    shutDownServer(serverSocket);
                    break;
                }
                serverMessagePromptHandler.handleServerPromptCommand(command);
            }
        });
        serverPromptThread.setDaemon(true);
        serverPromptThread.start();

        Thread serverAcceptConnectionsThread = new Thread(() -> {
            log.info("Start server accept connections thread...");

            while (true) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                    log.info("Client connected: {}:{}", clientSocket.getInetAddress(), clientSocket.getPort());

                    acceptConnectionHandler.startConnectionHandling(clientSocket);
                } catch (IOException e) {
                    if (serverShutdown.get()) {
                        log.info("Exiting client socket accept connections loop.");
                    } else {
                        log.error("Couldn't accept client socket connection.", e);
                    }
                    break;
                }
            }
        });
        serverAcceptConnectionsThread.setDaemon(true);
        serverAcceptConnectionsThread.start();

        try {
            serverAcceptConnectionsThread.join();
        } catch (InterruptedException e) {
            log.error("Couldn't join server threads.", e);
            throw new RuntimeException(e);
        }
    }

    private void shutDownServer(ServerSocket serverSocket) {
        serverShutdown.set(true);
        log.info("Server shutdown...");
        messagePrinter.printlnPromptMessage("Disconnecting chat users...");
        broadcastMessagesSender.broadCastMessagesToChatUsers(protocolMessageBuilder.buildDisconnectUserCommandMessage());
        messagePrinter.printlnPromptMessage("Chat users have been disconnected.");

        try {
            messagePrinter.printlnPromptMessage("Closing server socket...");
            serverSocket.close();
            messagePrinter.printlnPromptMessage("Server socket has been successfully closed.");
        } catch (IOException e) {
            log.error("Socket closing with errors", e);
            throw new RuntimeException(e);
        }

        serverShutdown.set(true);
        messagePrinter.printfMessage("Server shutdown.\n");
        log.info("Server shutdown success.");
    }
}