package com.vntu.console.chat.app.network;

import com.vntu.console.chat.app.component.input.params.ExtractedParams;
import com.vntu.console.chat.app.component.input.params.InputParamsExtractor;
import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public void startClient(Socket socket, String[] args) {
        log.info("Start client application...");
        ExtractedParams params = paramsExtractor.extractedParams(args);

        String nickname = promptNicknameIfNotSpecified(params);
        ChatUser chatUser = chatUserService.createChatUser(nickname);

        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.printf("%s#%s> ", chatUser.getNickname(), chatUser.getId());
            String command = in.nextLine();
            if (command.equalsIgnoreCase(QUIT_COMMAND)) {
                break;
            }
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
        System.out.print("client#1> Enter name: ");
        nickname = in.nextLine();
        return nickname;
    }
}