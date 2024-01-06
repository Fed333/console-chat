package com.vntu.console.chat.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {

    private Integer id;

    private String nickname;

    private boolean lunaUser;

    private Instant sessionStart;

    @JsonIgnore
    public String getUsername() {
        return nickname + "#" + id;
    }
}