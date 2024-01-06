package com.vntu.console.chat.app.dispatcher.server;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DispatcherCommand {

    private final Map<String, ConsoleCommand> serverCommands;
    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ServerOutMessagePrinter serverOutMessagePrinter;

    public void dispatchCommand(String command, ChatUserRequest request) {
        command = command.toUpperCase();
        if (serverCommands.containsKey(command)) {
            serverCommands.get(command).process(request);
        } else {
            log.info("Command \"{}\" not found!", command);
            ChatUser chatUser = request.getChatUser();
            if (chatUser.isLunaUser()) {
                printEcho(request.getCommandMessage());
            }
        }
    }

    private void printEcho(String command) {
        log.info("Send echo message to luna user.");
        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();
        serverOutMessagePrinter.printlnPromptMessage(command, chatUserWriter);
        chatUserWriter.flush();
    }
}