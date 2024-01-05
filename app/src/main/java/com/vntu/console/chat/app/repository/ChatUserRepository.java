package com.vntu.console.chat.app.repository;

import com.vntu.console.chat.app.entity.ChatUser;

import java.util.List;

public interface ChatUserRepository {

    List<ChatUser> findAll();

    List<ChatUser> findAllByNickname(String nickname);

    List<ChatUser> findAllByLunaUser(boolean lunaUser);

    ChatUser save(ChatUser user);

    void delete(ChatUser user);

}
