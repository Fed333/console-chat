package com.vntu.console.chat.app.network.socket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ChatUserSockets {

    private final Map<Integer, Socket> chatUsersSockets;

    public void addSocket(Integer chatUserId, Socket socket) {
        chatUsersSockets.put(chatUserId, socket);
    }

    public void closeSocket(Integer chatUserId) {
        Socket removed = chatUsersSockets.remove(chatUserId);
        try {
            removed.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
