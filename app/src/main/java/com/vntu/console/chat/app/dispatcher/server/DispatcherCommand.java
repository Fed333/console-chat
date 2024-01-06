package com.vntu.console.chat.app.dispatcher.server;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.entity.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DispatcherCommand {

    private final Map<String, ConsoleCommand> serverCommands;

    public void dispatchCommand(String command, ChatUserRequest request) {
        serverCommands.getOrDefault(command, (p) -> log.info("Command \"{}\" not found!", command)).process(request);
    }
}