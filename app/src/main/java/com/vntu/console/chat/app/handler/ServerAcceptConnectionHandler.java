package com.vntu.console.chat.app.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.protocol.ProtocolMessages;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.CREATED_CHAT_USER_COMMAND;

@Slf4j
@RequiredArgsConstructor
public class ServerAcceptConnectionHandler {

    private final ChatUserService chatUserService;

    private final ChatUserSockets chatUserSockets;

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;

    private final ServerOutMessagePrinter serverOutMessagePrinter;

    private final ObjectMapper objectMapper;

    public void startConnectionHandling(Socket socket) {
        ChatUser chatUser = chatUserService.createChatUser("client");
        chatUser.setSessionStart(Instant.now());
        chatUserSockets.addSocket(chatUser.getId(), socket);

        serverOutMessagePrinter.printfMessage("Client#%d connected: %s:%s\n", chatUser.getId(), socket.getInetAddress(), socket.getPort());

        Thread chatUserConnectionRequestHandler = new Thread(() -> {
            chatUserSocketThreadHolder.setChatUserSocket(socket);

            PrintWriter out = chatUserSocketThreadHolder.getChatUserWriter();

            String jsonChatUser = serializeChatUserToJson(chatUser);
            out.print(CREATED_CHAT_USER_COMMAND);
            out.println(jsonChatUser);
            out.flush();

            BufferedReader in = chatUserSocketThreadHolder.getChatUserReader();
            try {
                String line = in.readLine();
                while (line != null) {
                    //TODO add dispatching here...
                    serverOutMessagePrinter.printlnMessage("Received line: " + line);
                    line = in.readLine();
                }

            } catch (IOException e) {
                log.error("Couldn't read a line from chatUser socket input stream.", e);
                serverOutMessagePrinter.printlnMessage("ChatUser " + chatUser.getNickname() + "#" + chatUser.getId() + " has been disconnected.");
            }

        });
        chatUserConnectionRequestHandler.start();
    }

    private String serializeChatUserToJson(ChatUser chatUser) {
        try {
            return objectMapper.writeValueAsString(chatUser);
        } catch (JsonProcessingException e) {
            log.error("Couldn't serialize a chatUser object to a JSON string.", e);
            throw new RuntimeException(e);
        }
    }
}
