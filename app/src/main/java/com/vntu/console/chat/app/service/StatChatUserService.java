package com.vntu.console.chat.app.service;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.entity.dto.StatChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import lombok.RequiredArgsConstructor;

import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StatChatUserService {

    private final ChatUserSockets chatUserSockets;

    private final ChatUserService chatUserService;

    public List<StatChatUser> getStatChatUsers() {
        return chatUserService.findAll().stream()
                .map(this::mapChatUserToStat).collect(Collectors.toList());
    }

    private StatChatUser mapChatUserToStat(ChatUser chatUser) {
        String chatUserName = chatUser.getNickname() + "#" + chatUser.getId();

        Socket socket = chatUserSockets.getSocket(chatUser.getId());
        String host = socket.getInetAddress() + ":" + socket.getPort();

        return new StatChatUser(chatUserName, host);
    }

}
