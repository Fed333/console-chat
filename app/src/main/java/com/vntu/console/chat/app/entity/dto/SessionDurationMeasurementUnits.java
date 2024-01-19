package com.vntu.console.chat.app.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDurationMeasurementUnits {

    private long seconds;
    private long minutes;
    private long hours;

}
