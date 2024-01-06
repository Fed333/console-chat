package com.vntu.console.chat.app.network.component;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import com.vntu.console.chat.app.network.utils.SocketIOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BroadcastMessagesSender {

    private final ChatUserSockets chatUserSockets;
    private final ServerOutMessagePrinter messagePrinter;

    public void broadCastMessagesToLunaChatUsers(String message) {
        List<Socket> lunaUserSockets = chatUserSockets.getLunaUserSockets();

        lunaUserSockets.forEach(s -> {
            PrintWriter printWriter;
            printWriter = getSocketPrintWriter(s);
            messagePrinter.printlnPromptMessage(message, printWriter);
            printWriter.flush();
            log.info("Send message to the Client /{}:{}", s.getInetAddress(), s.getPort());
        });
    }

    private PrintWriter getSocketPrintWriter(Socket s) {
        return SocketIOUtils.getSocketPrintWriter(s);
    }
}
