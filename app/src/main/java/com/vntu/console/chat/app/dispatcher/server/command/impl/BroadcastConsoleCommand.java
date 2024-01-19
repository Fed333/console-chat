package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.component.BroadcastMessagesSender;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class BroadcastConsoleCommand implements ConsoleCommand {

    private final BroadcastMessagesSender broadcastMessagesSender;

    @Override
    public void process(ChatUserRequest request) {
        log.info("Process BCST command for chatUser: {}", request.getChatUser());

        ChatUser chatUser = request.getChatUser();
        String message = request.getParameterByOrder(0);

        if (message == null) {
            log.info("No message provided.");
            return;
        }

        broadcastMessagesSender.broadCastMessagesToChatUsers(chatUser, message);
    }

    @Override
    public String getDescription() {
        return "Broadcasts messages to all connected clients. Example: BCST \"Broadcast message\"";
    }

    @Override
    public String getSynopsis() {
        return "BCST \"<message>\"";
    }
}
