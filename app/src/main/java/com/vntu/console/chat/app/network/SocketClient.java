package com.vntu.console.chat.app.network;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class SocketClient {

    public void startClient(Socket socket, String[] args) {
        log.info("Start client application...");
        Scanner in = new Scanner(System.in);
        System.out.print("client#1> Enter name: ");
        String name = in.nextLine();

        while(true) {
            System.out.printf("%s#1> ", name);
            String command = in.nextLine();
            if (command.equals("QUIT")) {
                break;
            }
        }
    }
}