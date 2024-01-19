package com.vntu.console.chat.app.dispatcher.server.command.impl;

import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.command.ConsoleCommand;
import com.vntu.console.chat.app.entity.dto.StatChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import com.vntu.console.chat.app.service.ChatUserService;
import com.vntu.console.chat.app.service.StatChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.io.PrintWriter;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class StatConsoleCommand implements ConsoleCommand {

    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final StatChatUserService statChatUserService;
    private final Converter<StatChatUser, String> statChatUserToJsonConverter;


    @Override
    public void process(ChatUserRequest request) {
        log.info("Process STAT command for chatUser: {}", request.getChatUser());

        List<StatChatUser> statChatUsers = statChatUserService.getStatChatUsers();

        PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();
        serverOutMessagePrinter.printlnPromptMessage("Online users:", chatUserWriter);

        List<String> responseJson = statChatUsers.stream().map(statChatUserToJsonConverter::convert).collect(Collectors.toList());
        StringJoiner joiner = new StringJoiner("\n");
        responseJson.forEach(joiner::add);

        chatUserWriter.println(joiner);
        chatUserWriter.flush();
    }

    @Override
    public String getDescription() {
        return "Prints statistic about online users. Example: STATS";
    }

    @Override
    public String getSynopsis() {
        return "STATS";
    }
}