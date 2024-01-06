package com.vntu.console.chat.app.network;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.component.input.params.InputParamsExtractor;
import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.*;

@Slf4j
@RequiredArgsConstructor
public class SocketClient {

    private static final String NICKNAME_PARAMS_FLAG = "--nickname";

    private final ChatUserService chatUserService;
    private final InputParamsExtractor paramsExtractor;
    private final ChatUserOutMessagePrinter messagePrinter;
    private final Converter<String, ChatUser> jsonToChatUserConverter;

    private final AtomicReference<ChatUser> clientChatUser;
    private final AtomicBoolean isClientDisconnected;

    public void startClient(Socket clientSocket, String[] args) {
        log.info("Start client application...");
        ExtractedParams params = paramsExtractor.extractedParams(args);

        CountDownLatch createdChatUserRetrievalLatch = new CountDownLatch(1);

        Thread chatUserSenderThread = new Thread(() -> {
            log.info("Start client send requests thread...");

            awaitForCreatedChatUserRetrievalFromServer(createdChatUserRetrievalLatch);

            log.info("Retrieved chatUser {}", clientChatUser.get());

            PrintWriter out = getPrintWriter(clientSocket);
            Scanner in = new Scanner(System.in);
            while (!isClientDisconnected.get()) {
                messagePrinter.printPrompt(clientChatUser.get());
                String messageLine = in.nextLine();
                log.info("Prompted chatUser message: {}", messageLine);

                messagePrinter.printPrompt(clientChatUser.get(), out);
                out.println(messageLine);
                out.flush();
            }
        });
        chatUserSenderThread.start();

        BufferedReader in = getBufferedReader(clientSocket);
        Thread chatUserReceiverThread = new Thread(() -> {
            log.info("Start client receive requests thread...");
            try {
                String inputLine = in.readLine();

                messagePrinter.printlnMessage(inputLine);
                if (inputLine.contains(CREATED_CHAT_USER_COMMAND)) {
                    String createdUserJson = extractCreatedUserJson(inputLine);

                    ChatUser createdChatUser = jsonToChatUserConverter.convert(createdUserJson);

                    clientChatUser.set(createdChatUser);
                    createdChatUserRetrievalLatch.countDown();
                }

                while (inputLine != null) {
                    inputLine = in.readLine();
                    log.info("Received server message: {}", inputLine);
                    messagePrinter.printlnMessage(inputLine);
                    messagePrinter.printPrompt(clientChatUser.get());

                    if (inputLine.contains(UPDATE_CHAT_USER_COMMAND)) {
                        String updatedUserJson = extractUpdateUserJson(inputLine);

                        ChatUser updatedChatUser = jsonToChatUserConverter.convert(updatedUserJson);
                        log.info("Updating chatUser with {}", updatedChatUser);
                        clientChatUser.set(updatedChatUser);
                        log.info("Updated chatUser is now {}", clientSocket);
                    } else if (inputLine.contains(DISCONNECT_CHAT_USER_COMMAND)) {
                        log.info("Disconnect user: {}", clientChatUser.get());
                        messagePrinter.printlnMessage("Disconnected");
                        isClientDisconnected.set(true);
                        break;
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        chatUserReceiverThread.start();
//        while(true) {
////            messagePrinter.printPrompt(chatUser);
////            String command = in.nextLine();
////            if (command.equalsIgnoreCase(QUIT_COMMAND)) {
////                break;
////            }
//        }

        try {
            chatUserSenderThread.join();
            chatUserReceiverThread.join();
            log.info("User has been disconnected. Shutdown the client application.");
        } catch (InterruptedException e) {
            log.error("Couldn't join server threads.", e);
            throw new RuntimeException(e);
        }
    }

    private void awaitForCreatedChatUserRetrievalFromServer(CountDownLatch createdChatUserRetrievalLatch) {
        try {
            createdChatUserRetrievalLatch.await();
        } catch (InterruptedException e) {
            log.error("Couldn't await for created chat user retrieving.", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private static BufferedReader getBufferedReader(Socket clientSocket) {
        try {
            return new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            log.error("Couldn't obtain socket input stream reader", e);
            throw new RuntimeException(e);
        }
    }

    private PrintWriter getPrintWriter(Socket clientSocket) {
        try {
            return new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            log.error("Couldn't obtain output stream of chat user socket.", e);
            throw new RuntimeException(e);
        }
    }

    private String extractCreatedUserJson(String inputLine) {
        return extractUserJsonFromCommand(CREATED_CHAT_USER_COMMAND, inputLine);
    }

    private String extractUpdateUserJson(String inputLine) {
        return extractUserJsonFromCommand(UPDATE_CHAT_USER_COMMAND, inputLine);
    }

    private String extractUserJsonFromCommand(String command, String inputLine) {
        return inputLine.substring(inputLine.indexOf(command) + command.length());
    }

    private String promptNicknameIfNotSpecified(ExtractedParams params) {
        Map<String, Object> paramsMap = params.getParamsMap();

        String nickname;
        if (paramsMap.containsKey(NICKNAME_PARAMS_FLAG)) {
            nickname = (String) paramsMap.get(NICKNAME_PARAMS_FLAG);
        } else {
            nickname = promptNickName();
        }
        return nickname;
    }

    private String promptNickName() {
        String nickname;
        Scanner in = new Scanner(System.in);
        messagePrinter.printPromptMessage("client",1, "Enter name: ");
        nickname = in.nextLine();
        return nickname;
    }
}