package com.vntu.console.chat.app.handler;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.component.input.params.InputParamsExtractor;
import com.vntu.console.chat.app.network.component.BroadcastMessagesSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.LUNA_SERVER_PROMPT_COMMAND;

@Slf4j
@RequiredArgsConstructor
public class ServerMessagePromptCommandHandler {

    private final InputParamsExtractor inputParamsExtractor;
    private final BroadcastMessagesSender broadcastMessagesSender;

    public void handleServerPromptCommand(String promptCommand) {
        log.info("Server handles the prompt command: {}...", promptCommand);

        if (promptCommand.isEmpty()) {
            return;
        }

        String command = inputParamsExtractor.extractCommand(promptCommand);

        ExtractedParams extractedParams = inputParamsExtractor.extractParams(promptCommand);

        //TODO add command dispatching here...
        if (Objects.equals(command, LUNA_SERVER_PROMPT_COMMAND)) {
            broadcastMessagesSender.broadCastMessagesToLunaChatUsers((String) extractedParams.getParamsList().get(0));
        }
    }
}
