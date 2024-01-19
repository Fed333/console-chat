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
        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();

        String turnEcho = request.getParameterByOrder(0);

        if (turnEcho == null) {
            log.info("No echo options specified!");
            printEchoBadSyntax("Bad syntax. No echo option was specified.", chatUserWriter);
            return;
        }

        if (isOn(turnEcho)) {
            chatUser.setLunaUser(true);
        } else if (isOff(turnEcho)) {
            chatUser.setLunaUser(false);
        } else {
            log.info("Wrong ECHO parameter: {}", turnEcho);
            printEchoBadSyntax("No valid echo option was specified.", chatUserWriter);
            return;
        }

        chatUserService.save(chatUser);

        String messageServerResponse = protocolMessageBuilder.buildUpdateChatUserCommandMessage(chatUser);
        serverOutMessagePrinter.printlnPromptMessage(messageServerResponse, chatUserWriter);
        chatUserWriter.flush();
    }

    @Override
    public String getDescription() {
        return "Enables server echo messages. Is set to true by default. Example: ECHO OFF";
    }

    @Override
    public String getSynopsis() {
        return "ECHO [ON | OFF]";
    }

    private void printEchoBadSyntax(String message, PrintWriter chatUserWriter) {
        serverOutMessagePrinter.printlnPromptMessage(message, chatUserWriter);
        serverOutMessagePrinter.printlnPromptMessage("Please use [ON | OFF]", chatUserWriter);
    }

    private boolean isOn(String turnEcho) {
        return turnEcho.equalsIgnoreCase("ON");
    }

    private boolean isOff(String turnEcho) {
        return turnEcho.equalsIgnoreCase("OFF");
    }
}
