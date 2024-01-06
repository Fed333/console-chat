package com.vntu.console.chat.app.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.CLIENT_QUIT_COMMAND;
import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.CREATED_CHAT_USER_COMMAND;

@Slf4j
@RequiredArgsConstructor
public class ServerAcceptConnectionHandler {

    private final ChatUserService chatUserService;
    private final ChatUserSockets chatUserSockets;
    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final Converter<ChatUser, String> chatUserToJsonConverter;
    private final ServerRequestMessageHandler serverRequestMessageHandler;

    public void startConnectionHandling(Socket socket) {
        ChatUser chatUser = chatUserService.createChatUser("client");
        chatUser.setSessionStart(Instant.now());
        chatUserSockets.addSocket(chatUser.getId(), socket);

        serverOutMessagePrinter.printfMessage("Client#%d connected: %s:%s\n", chatUser.getId(), socket.getInetAddress(), socket.getPort());

        Thread chatUserConnectionRequestHandler = new Thread(() -> {
            chatUserSocketThreadHolder.setChatUserSocket(socket);
            PrintWriter out = chatUserSocketThreadHolder.getChatUserWriter();

            String jsonChatUser = chatUserToJsonConverter.convert(chatUser);

            serverOutMessagePrinter.printPrompt(out);
            out.print(CREATED_CHAT_USER_COMMAND);
            out.println(jsonChatUser);
            out.flush();

            BufferedReader in = chatUserSocketThreadHolder.getChatUserReader();
            try {
                String requestMessage;
                do  {
                    requestMessage = in.readLine();
                    serverOutMessagePrinter.printlnPromptMessage(requestMessage);
                    serverRequestMessageHandler.handleRequest(requestMessage);

                    if (isQuitCommandEntered(requestMessage)) {
                        log.info("Disconnect client");
                        break;
                    }
                } while (requestMessage != null);

            } catch (IOException e) {
                log.error("Couldn't read a line from chatUser socket input stream.", e);
                serverOutMessagePrinter.printlnPromptMessage("ChatUser " + chatUser.getNickname() + "#" + chatUser.getId() + " has been disconnected.");
            }

        });
        chatUserConnectionRequestHandler.start();
    }

    private boolean isQuitCommandEntered(String requestMessage) {
        return requestMessage.toUpperCase().startsWith(CLIENT_QUIT_COMMAND);
    }
}
