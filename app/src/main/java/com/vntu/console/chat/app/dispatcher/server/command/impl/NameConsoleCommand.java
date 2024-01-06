package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NameConsoleCommand implements ConsoleCommand {

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;

    @Override
    public void process(ChatUserRequest request) {
        log.info("Process NAME command for chatUser: {}", request.getChatUser());
        //TODO add command processing...
    }
}
