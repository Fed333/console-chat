package com.vntu.console.chat.app.dispatcher.server;

import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DispatcherCommand {

    private final Map<String, ConsoleCommand> serverCommands;

    public void dispatchCommand(String command, Map<String, Object> params) {
        serverCommands.getOrDefault(command, (p) -> log.info("Command \"{}\" not found!", command)).process(params);
    }
}