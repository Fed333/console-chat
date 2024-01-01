package com.vntu.console.chat.app.component.input.params;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExtractedParams {

    private final Map<String, Object> paramsMap;

    private final List<Object> paramsList;

}