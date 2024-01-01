package com.vntu.console.chat.app.network;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class SocketServer {

    private static final String QUIT_COMMAND = "QUIT";

    private final ServerOutMessagePrinter messagePrinter;

    public void startServer(ServerSocket socket, String[] args) {
        log.info("Start server application...");

        Scanner in = new Scanner(System.in);
        while(true) {
            messagePrinter.printPrompt();
            String command = in.nextLine();
            if (command.equalsIgnoreCase(QUIT_COMMAND)) {
                //TODO disconnect all connected clients
                break;
            }
        }
    }
}