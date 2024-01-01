package com.vntu.console.chat.app.service;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository repository;

    public ChatUser createChatUser(String nickname) {
        if (repository.findAllByNickname(nickname).size() > 1) {
            log.error("Attempt to create the user with existing nickname.");
            throw new RuntimeException("ChatUsers with same nicknames aren't allowed. The nickname" + nickname + " has been already taken.");
        }
        ChatUser chatUser = ChatUser.builder()
                .nickname(nickname).build();

        return repository.save(chatUser);

    }
}