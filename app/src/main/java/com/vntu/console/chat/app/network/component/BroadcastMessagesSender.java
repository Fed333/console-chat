package com.vntu.console.chat.app.network.component;

import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import com.vntu.console.chat.app.network.utils.SocketIOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BroadcastMessagesSender {

    private final ChatUserSockets chatUserSockets;
    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final ChatUserOutMessagePrinter chatUserOutMessagePrinter;

    public void broadCastMessagesToLunaChatUsers(String message) {
        List<Socket> lunaUserSockets = chatUserSockets.getLunaUserSockets();

        lunaUserSockets.forEach(s -> sendMessage(message, s));
    }

    public void broadCastMessagesToChatUsers(String message) {
        List<Socket> lunaUserSockets = chatUserSockets.getAllUsersSockets();

        lunaUserSockets.forEach(s -> sendMessage(message, s));
    }

    public void broadCastMessagesToChatUsers(ChatUser chatUser, String message) {
        List<Socket> lunaUserSockets = chatUserSockets.getAllUsersSockets();

        lunaUserSockets.forEach(s -> sendMessage(chatUser, message, s));
    }

    private void sendMessage(String message, Socket s) {
        PrintWriter printWriter = getSocketPrintWriter(s);
        serverOutMessagePrinter.printlnPromptMessage(message, printWriter);
        printWriter.flush();
        log.info("Send message to the Client /{}:{}", s.getInetAddress(), s.getPort());
    }

    private void sendMessage(ChatUser chatUser, String message, Socket s) {
        PrintWriter printWriter = getSocketPrintWriter(s);
        chatUserOutMessagePrinter.printlnPromptMessage(chatUser, message, printWriter);
        printWriter.flush();
        log.info("Send message to the Client /{}:{}", s.getInetAddress(), s.getPort());
    }

    private PrintWriter getSocketPrintWriter(Socket s) {
        return SocketIOUtils.getSocketPrintWriter(s);
    }
}
