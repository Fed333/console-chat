package com.vntu.console.chat.app.network.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class SocketIOUtils {

    public static PrintWriter getSocketPrintWriter(Socket s) {
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            log.error("Failed during obtaining socket output stream.", e);
            throw new RuntimeException(e);
        }
        return printWriter;
    }

}
