package com.vntu.console.chat.app.handler;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.component.input.params.InputParamsExtractor;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.dispatcher.server.ChatUserRequest;
import com.vntu.console.chat.app.dispatcher.server.DispatcherCommand;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.network.component.BroadcastMessagesSender;
import com.vntu.console.chat.app.network.protocol.ChatUserFromPromptExtractor;
import com.vntu.console.chat.app.network.socket.ChatUserSocketThreadHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ServerRequestMessageHandler {

    private final DispatcherCommand serverDispatcherCommand;
    private final InputParamsExtractor inputParamsExtractor;
    private final ServerOutMessagePrinter serverOutMessagePrinter;
    private final BroadcastMessagesSender broadcastMessagesSender;
    private final ChatUserSocketThreadHolder chatUserSocketThreadHolder;
    private final ChatUserFromPromptExtractor chatUserFromPromptExtractor;

    public void handleRequest(String requestMessage) {
        log.info("Start handing client's request: {}", requestMessage);

        Optional<ChatUser> chatUserOptional = chatUserFromPromptExtractor.extractUser(requestMessage);

        if (chatUserOptional.isEmpty()) {
            log.info("ChatUser with given nickname and id wasn't found. Stop request handling");
            return;
        }

        String commandMessage = chatUserFromPromptExtractor.erasePrompt(requestMessage);

        String command = inputParamsExtractor.extractCommand(commandMessage);
        ExtractedParams extractedParams = inputParamsExtractor.extractParams(commandMessage);

        ChatUser chatUser = chatUserOptional.get();
        ChatUserRequest chatUserRequest = new ChatUserRequest(chatUser, extractedParams);

        serverDispatcherCommand.dispatchCommand(command, chatUserRequest);

        if (chatUser.isLunaUser()) {
            log.info("Send echo message to luna user.");
            PrintWriter chatUserWriter = chatUserSocketThreadHolder.getChatUserWriter();
            serverOutMessagePrinter.printlnPromptMessage(commandMessage, chatUserWriter);
            chatUserWriter.flush();
        }

        log.info("End request handling");
    }
}