package com.vntu.console.chat.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ChatUser {

    private Integer id;

    private String nickname;

    private boolean lunaUser;

    private Instant sessionStart;

}