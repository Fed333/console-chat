package com.vntu.console.chat.app.handler;

import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class ServerAcceptConnectionHandler {

    private final ChatUserService chatUserService;

    private final ChatUserSockets chatUserSockets;

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;

    public void startConnectionHandling(Socket socket) {
        ChatUser chatUser = chatUserService.createChatUser("client");
        chatUser.setSessionStart(Instant.now());
        chatUserSockets.addSocket(chatUser.getId(), socket);

        ChatUserOutMessagePrinter userOutMessagePrinter = new ChatUserOutMessagePrinter();

        Thread chatUserConnectionRequestHandler = new Thread(() -> {
            chatUserSocketThreadHolder.setChatUserSocket(socket);

            BufferedReader in = chatUserSocketThreadHolder.getChatUserReader();
            try {
                String line = in.readLine();
                while(line != null) {
                    //TODO add dispatching here...
                    userOutMessagePrinter.printPromptMessage(chatUser, "Received line: " + line);
                    line = in.readLine();
                }

            } catch (IOException e) {
                log.error("Couldn't read a line from chatUser socket input stream.", e);
                throw new RuntimeException(e);
            }

        });
        chatUserConnectionRequestHandler.start();



    }

}
