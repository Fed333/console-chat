package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.DispatcherCommand;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

public class HelpConsoleCommand implements ConsoleCommand {

    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final DispatcherCommand dispatcherCommand;

    public HelpConsoleCommand(ServerOutMessagePrinter serverOutMessagePrinter,
                              ChatUserSocketThreadHolder chatUserSocketThreadHolder,
                              @Lazy DispatcherCommand dispatcherCommand) {
        this.serverOutMessagePrinter = serverOutMessagePrinter;
        this.chatUserSocketThreadHolder = chatUserSocketThreadHolder;
        this.dispatcherCommand = dispatcherCommand;
    }

    @Override
    public void process(ChatUserRequest chatUserRequest) {
        Map<String, ConsoleCommand> commandsMap = dispatcherCommand.getServerCommands();
        StringJoiner commandsSynopsisJoiner = new StringJoiner("\n");
        Collection<ConsoleCommand> commands = commandsMap.values();

        Integer largestSynopsis = commands.stream()
                .map(c -> c.getSynopsis().length())
                .max(Integer::compareTo)
                .orElse(30);

        int indentsBetween = 8;


        commands.forEach(command -> {
            int indentSymbols = largestSynopsis - command.getSynopsis().length() + indentsBetween;
            String synopsisFormatString = "\t%s %" + indentSymbols + "s %s";
            String synopsis = String.format(synopsisFormatString, command.getSynopsis(), " ", command.getDescription());
            commandsSynopsisJoiner.add(synopsis);
        });

        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();
        serverOutMessagePrinter.printlnPromptMessage("Commands synopsis:", chatUserWriter);
        serverOutMessagePrinter.printlnMessage(commandsSynopsisJoiner.toString(), chatUserWriter);

        serverOutMessagePrinter.printlnMessage("The commands are case insensitive.", chatUserWriter);
        serverOutMessagePrinter.printlnMessage("Please use double quotes to pass messages with spaces.", chatUserWriter);
        serverOutMessagePrinter.printlnMessage("To shutdown the server enter CLOSE: command in the server console.", chatUserWriter);

        chatUserWriter.flush();
    }

    @Override
    public String getDescription() {
        return "Shows information about available commands and its syntax. Example: HELP";
    }

    @Override
    public String getSynopsis() {
        return "HELP";
    }
}
