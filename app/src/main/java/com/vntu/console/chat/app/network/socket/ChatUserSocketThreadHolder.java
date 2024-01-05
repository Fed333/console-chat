package com.vntu.console.chat.app.network.socket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


@Slf4j
@RequiredArgsConstructor
public class ChatUserSocketThreadHolder {

    private final ThreadLocal<Socket> chatUserSocket;

    private final ThreadLocal<BufferedReader> chatUserReader;

    private final ThreadLocal<PrintWriter> chatUserWriter;

    public void setChatUserSocket(Socket clientSocket) {
        this.chatUserSocket.set(clientSocket);
        try {
            this.chatUserReader.set(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
            this.chatUserWriter.set(new PrintWriter(clientSocket.getOutputStream(), true));
        } catch (IOException e) {
            log.error("Couldn't set up chat user socket for holding. Failed during obtaining socket i/o streams.", e);
            throw new RuntimeException(e);
        }
    }

    public BufferedReader getChatUserReader() {
        return chatUserReader.get();
    }

    public PrintWriter getChatUserWriter() {
        return chatUserWriter.get();
    }
}
