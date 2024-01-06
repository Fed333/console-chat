package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import com.vntu.console.chat.app.network.utils.SocketIOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
@RequiredArgsConstructor
public class MessageConsoleCommand implements ConsoleCommand {

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ChatUserOutMessagePrinter chatUserOutMessagePrinter;
    private final ChatUserSockets chatUserSockets;

    @Override
    public void process(ChatUserRequest request) {
        log.info("Process MESG command for chatUser: {}", request.getChatUser());

        ChatUser senderUser = request.getChatUser();
        String receiverUserName = request.getParameterByOrder(0);
        String message = request.getParameterByOrder(1);

        Socket receiverUserSocket = chatUserSockets.getSocket(receiverUserName);

        if (receiverUserSocket == null) {
            log.info("No socket for user: {} is found.", receiverUserName);
            return;
        }

        PrintWriter receiverPrintWriter = SocketIOUtils.getSocketPrintWriter(receiverUserSocket);

        chatUserOutMessagePrinter.printPrompt(senderUser, receiverPrintWriter);
        chatUserOutMessagePrinter.printlnMessage(message, receiverPrintWriter);
        receiverPrintWriter.flush();
        log.info("Sent message: {} to {}", message, receiverUserName);
    }
}