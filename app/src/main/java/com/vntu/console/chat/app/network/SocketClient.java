package com.vntu.console.chat.app.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.component.input.params.InputParamsExtractor;
import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.component.output.PromptMessageProvider;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static com.vntu.console.chat.app.network.protocol.ProtocolMessages.CREATED_CHAT_USER_COMMAND;

@Slf4j
@RequiredArgsConstructor
public class SocketClient {

    private static final String NICKNAME_PARAMS_FLAG = "--nickname";
    private static final String QUIT_COMMAND = "QUIT";

    private final ChatUserService chatUserService;
    private final InputParamsExtractor paramsExtractor;
    private final ChatUserOutMessagePrinter messagePrinter;
    private final PromptMessageProvider promptMessageProvider;
    private final ObjectMapper objectMapper;

    private final AtomicReference<ChatUser> clientChatUser;

    public void startClient(Socket clientSocket, String[] args) {
        log.info("Start client application...");
        ExtractedParams params = paramsExtractor.extractedParams(args);

        CountDownLatch createdChatUserRetrievalLatch = new CountDownLatch(1);

        Thread chatUserSenderThread = new Thread(() -> {
            log.info("Start client send requests thread...");

            awaitForCreatedChatUserRetrievalFromServer(createdChatUserRetrievalLatch);
            ChatUser chatUser = clientChatUser.get();

            log.info("Retrieved chatUser {}", chatUser);

            PrintWriter out = getPrintWriter(clientSocket);
            Scanner in = new Scanner(System.in);
            while (true) {
                messagePrinter.printPrompt(chatUser.getNickname(), chatUser.getId());
                String messageLine = in.nextLine();
                out.println(messageLine);
                out.flush();
            }
        });
        chatUserSenderThread.start();
        //String nickname = promptNicknameIfNotSpecified(params);
        //ChatUser chatUser = chatUserService.createChatUser(nickname);


        BufferedReader in = getBufferedReader(clientSocket);
        Thread chatUserReceiverThread = new Thread(() -> {
            log.info("Start client receive requests thread...");
            try {
                String inputLine = in.readLine();

                System.out.println(promptMessageProvider.getServerPrompt() + inputLine);

                if (inputLine.contains(CREATED_CHAT_USER_COMMAND)) {
                    String createdUserJson = inputLine.substring(CREATED_CHAT_USER_COMMAND.length());
                    ObjectReader objectReader = objectMapper.readerFor(ChatUser.class);

                    ChatUser createdChatUser = objectReader.readValue(createdUserJson);

                    clientChatUser.set(createdChatUser);
                    createdChatUserRetrievalLatch.countDown();
                }

//                ServerOutMessagePrinter serverMessagePrinter = new ServerOutMessagePrinter();
                while (inputLine != null) {
//                    serverMessagePrinter.printlnMessage(inputLine);

                    inputLine = in.readLine();
                    System.out.println(promptMessageProvider.getServerPrompt() + inputLine);
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