package com.vntu.console.chat.app.repository;

import com.vntu.console.chat.app.entity.ChatUser;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository {

    Optional<ChatUser> findByIdAndNickname(Integer id, String nickname);

    List<ChatUser> findAll();

    List<ChatUser> findAllByNickname(String nickname);

    List<ChatUser> findAllByLunaUser(boolean lunaUser);

    ChatUser save(ChatUser user);

    void delete(ChatUser user);

}
