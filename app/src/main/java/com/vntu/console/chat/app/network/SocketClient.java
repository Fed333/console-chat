package com.vntu.console.chat.app.network;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.component.input.params.InputParamsExtractor;
import com.vntu.console.chat.app.component.output.ChatUserOutMessagePrinter;
import com.vntu.console.chat.app.component.output.ServerOutMessagePrinter;
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

@Slf4j
@RequiredArgsConstructor
public class SocketClient {

    private static final String NICKNAME_PARAMS_FLAG = "--nickname";
    private static final String QUIT_COMMAND = "QUIT";

    private final ChatUserService chatUserService;
    private final InputParamsExtractor paramsExtractor;
    private final ChatUserOutMessagePrinter messagePrinter;

    public void startClient(Socket clientSocket, String[] args) {
        log.info("Start client application...");
        ExtractedParams params = paramsExtractor.extractedParams(args);

        Thread chatUserSenderThread = new Thread(() -> {
            log.info("Start client send requests thread...");

            PrintWriter out = getPrintWriter(clientSocket);
            Scanner in = new Scanner(System.in);
            while(true) {
                messagePrinter.printPrompt("client", 1);
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

//                ServerOutMessagePrinter serverMessagePrinter = new ServerOutMessagePrinter();
                while (inputLine != null) {
//                    serverMessagePrinter.printlnMessage(inputLine);
                    System.out.println("server>" + inputLine);
                    inputLine = in.readLine();
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