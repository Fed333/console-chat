package com.vntu.console.chat.app.service;

import com.vntu.console.chat.app.entity.ChatUser;
import com.vntu.console.chat.app.entity.dto.StatChatUser;
import com.vntu.console.chat.app.network.socket.ChatUserSockets;
import com.vntu.console.chat.app.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ChatUserService {

    private final ChatUserRepository repository;

    public List<ChatUser> findAll() {
        return repository.findAll();
    }

    public Optional<ChatUser> findByIdAndNickname(Integer id, String nickname) {
        return repository.findByIdAndNickname(id, nickname);
    }

    public ChatUser save(ChatUser chatUser) {
        return repository.save(chatUser);
    }

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