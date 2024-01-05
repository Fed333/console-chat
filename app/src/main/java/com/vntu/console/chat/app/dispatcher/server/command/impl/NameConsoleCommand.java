package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class NameConsoleCommand implements ConsoleCommand {

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;

    @Override
    public void process(Map<String, Object> params) {

    }
}
