package com.vntu.console.chat.app.component.input.params;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * Class designed for extracting command and command params from the user input.
 * @author Roman_Kovalchuk
 * */
public class InputParamsExtractor {

    public String extractCommand(String input){
        return input.substring(0, input.indexOf(' '));
    }

    public ExtractedParams extractParams(String input) {
        SpacesInsideQuotesSanitizer sanitizer = new SpacesInsideQuotesSanitizer('\u0000');
        input = sanitizer.sanitize(input);

        String[] params = input.substring(input.indexOf(' ')).trim().split("\\s");
        params = Arrays.stream(params).map(sanitizer::desanitize).toArray(String[]::new);

        return extractedParams(params);
    }

    public ExtractedParams extractedParams(String[] params) {
        Map<String, Object> paramsMap = Arrays.stream(params)
                .map(String::trim)
                .filter(not(String::isEmpty))
                .map(param -> param.split("=", 2))
                .collect(Collectors.toMap(p -> p[0], p -> p.length == 2 ? trimQuotes(p[1]) : p[0]));

        return new ExtractedParams(
                paramsMap,
                Arrays.stream(params).map(this::trimParam).collect(Collectors.toList())
        );
    }

    private String trimParam(String param) {
        return trimQuotes(param.trim());
    }

    private String trimQuotes(String str) {
        return str.replaceAll("^\"|\"$", "");
    }
}