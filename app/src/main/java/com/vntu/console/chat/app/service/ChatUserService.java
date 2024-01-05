package com.vntu.console.chat.app.service;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository repository;

    public ChatUser createChatUser(String nickname) {
        ChatUser chatUser = ChatUser.builder()
                .nickname(nickname)
                .lunaUser(true).build();

        return repository.save(chatUser);
    }

    public List<ChatUser> findAllLunaChatUsers() {
        return repository.findAllByLunaUser(true);
    }
}