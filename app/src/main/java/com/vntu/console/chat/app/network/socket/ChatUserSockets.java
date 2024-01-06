package com.vntu.console.chat.app.network.socket;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.service.ChatUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ChatUserSockets {

    private final Map<Integer, Socket> chatUsersSockets;

    private final ChatUserService chatUserService;

    public void addSocket(Integer chatUserId, Socket socket) {
        chatUsersSockets.put(chatUserId, socket);
    }

    public Socket getSocket(Integer chatUserId) {
        return chatUsersSockets.get(chatUserId);
    }

    public Socket getSocket(String chatUserName) {
        String[] chatUserParams = chatUserName.split("#");

        if (chatUserParams.length < 2) {
            return null;
        }

        //TODO probably need the nickname check.
        String nickname = chatUserParams[0];
        Integer id = Integer.parseInt(chatUserParams[1]);

        return chatUsersSockets.get(id);
    }

    public List<Socket> getLunaUserSockets() {
        List<ChatUser> lunaChatUsers = chatUserService.findAllLunaChatUsers();

        return lunaChatUsers.stream()
                .map(ChatUser::getId)
                .map(chatUsersSockets::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void closeSocket(Integer chatUserId) {
        Socket removed = chatUsersSockets.remove(chatUserId);
        try {
            removed.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
