package com.vntu.console.chat.app.network;

import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.util.Scanner;

@Slf4j
public class SocketServer {

    public void startServer(ServerSocket socket, String[] args) {
        log.info("Start server application...");

        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.print("server> ");
            String command = in.nextLine();
            if (command.equals("QUIT")) {
                //TODO disconnect all connected clients
                break;
            }
        }
    }
}