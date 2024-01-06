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
public class EchoConsoleCommand implements ConsoleCommand {

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final ChatUserService chatUserService;
    private final ProtocolMessageBuilder protocolMessageBuilder;

    @Override
    public void process(ChatUserRequest request) {
        log.info("Process ECHO command for chatUser: {}", request.getChatUser());

        ChatUser chatUser = request.getChatUser();
        String turnEcho = request.getParameterByOrder(0);

        if (turnEcho == null) {
            log.info("No echo options specified!");
            return;
        }

        if (isOn(turnEcho)) {
            chatUser.setLunaUser(true);
        } else if (isOff(turnEcho)) {
            chatUser.setLunaUser(false);
        } else {
            log.info("Wrong ECHO parameter: {}", turnEcho);
        }

        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();

        serverOutMessagePrinter.printlnPromptMessage(protocolMessageBuilder.buildUpdateChatUserCommandMessage(chatUser), chatUserWriter);
        chatUserWriter.flush();
    }

    private boolean isOn(String turnEcho) {
        return turnEcho.equalsIgnoreCase("ON");
    }

    private boolean isOff(String turnEcho) {
        return turnEcho.equalsIgnoreCase("OFF");
    }
}
