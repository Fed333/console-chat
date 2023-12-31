package com.vntu.console.chat.app.runner;

import com.vntu.console.chat.app.network.SocketClient;
import com.vntu.console.chat.app.network.SocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
public class ConsoleChatApplicationRunner {

    private final SocketServer serverStarter;

    private final SocketClient clientStarter;

    @Value("${server.port}")
    private int serverPort;

    public void runConsoleChat(String... args) {
        try (ServerSocket socket = new ServerSocket(serverPort)) {
            log.info("Start chat server on port {}", serverPort);

            serverStarter.startServer(socket, args);
        } catch (BindException e) {
            log.info("The port {} is already taken by the chat server. Add new chat client.", serverPort);

            clientStarter.startClient(createClientSocket(), args);
        } catch (IOException e) {
            log.error("Couldn't start neither chat server nor chat client");
            throw new RuntimeException(e);
        }
    }

    private Socket createClientSocket() {
        try {
            return new Socket("localhost", serverPort);
        } catch (IOException e) {
            log.error("Couldn't create client socket for localhost: {}", serverPort);
            throw new RuntimeException(e);
        }
    }
}
