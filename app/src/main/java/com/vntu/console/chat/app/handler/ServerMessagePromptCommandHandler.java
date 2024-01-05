package com.vntu.console.chat.app.handler;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.component.input.params.InputParamsExtractor;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.LUNA_SERVER_PROMPT_COMMAND;

@Slf4j
@RequiredArgsConstructor
public class ServerMessagePromptCommandHandler {

    private final InputParamsExtractor inputParamsExtractor;

    private final ChatUserSockets chatUserSockets;

    private final ServerOutMessagePrinter messagePrinter;

    public void handleServerPromptCommand(String promptCommand) {
        log.info("Server handles the prompt command: {}...", promptCommand);

        if (promptCommand.isEmpty()) {
            return;
        }

        String command = inputParamsExtractor.extractCommand(promptCommand);

        ExtractedParams extractedParams = inputParamsExtractor.extractParams(promptCommand);

        //TODO add command dispatching here...
        if (Objects.equals(command, LUNA_SERVER_PROMPT_COMMAND)) {
            sendLunaMessages((String) extractedParams.getParamsList().get(0));
        }
    }

    public void sendLunaMessages(String message) {
        List<Socket> lunaUserSockets = chatUserSockets.getLunaUserSockets();

        lunaUserSockets.forEach(s -> {
            PrintWriter printWriter;
            printWriter = getSocketPrintWriter(s);
            messagePrinter.printlnPromptMessage(message, printWriter);
            printWriter.flush();
            log.info("Send message to the Client /{}:{}", s.getInetAddress(), s.getPort());
        });
    }

    private PrintWriter getSocketPrintWriter(Socket s) {
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException e) {
            log.error("Failed during obtaining socket output stream.", e);
            throw new RuntimeException(e);
        }
        return printWriter;
    }
}
