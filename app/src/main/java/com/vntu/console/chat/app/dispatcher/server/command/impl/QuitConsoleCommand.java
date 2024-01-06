package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.protocol.ProtocolMessageBuilder;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class QuitConsoleCommand implements ConsoleCommand {

    private final ChatUserService chatUserService;
    private final ProtocolMessageBuilder protocolMessageBuilder;
    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ServerOutMessagePrinter serverOutMessagePrinter;

    @Override
    public void process(ChatUserRequest request) {
        log.info("Process QUIT command for chatUser: {}", request.getChatUser());

        ChatUser chatUser = request.getChatUser();

        chatUserService.delete(chatUser);
        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();

        serverOutMessagePrinter.printlnPromptMessage(protocolMessageBuilder.buildDisconnectUserCommandMessage(), chatUserWriter);
        chatUserWriter.flush();
    }
}