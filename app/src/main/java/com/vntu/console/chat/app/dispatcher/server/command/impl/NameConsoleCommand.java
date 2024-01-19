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
import org.springframework.core.convert.converter.Converter;

import java.io.PrintWriter;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.UPDATE_CHAT_USER_COMMAND;

@Slf4j
@RequiredArgsConstructor
public class NameConsoleCommand implements ConsoleCommand {

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final ChatUserService chatUserService;
    private final ProtocolMessageBuilder protocolMessageBuilder;

    @Override
    public void process(ChatUserRequest request) {
        log.info("Process NAME command for chatUser: {}", request.getChatUser());

        ChatUser chatUser = request.getChatUser();
        String name = request.getParameterByOrder(0);

        if (name == null) {
            log.info("Name wasn't found!");
            return;
        }

        chatUser.setNickname(name);
        chatUserService.save(chatUser);

        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();

        serverOutMessagePrinter.printlnPromptMessage(protocolMessageBuilder.buildUpdateChatUserCommandMessage(chatUser), chatUserWriter);
        chatUserWriter.flush();
    }

    @Override
    public String getDescription() {
        return "Changes a nickname of the current user. Example: NAME roman";
    }

    @Override
    public String getSynopsis() {
        return "NAME <nickname>";
    }
}