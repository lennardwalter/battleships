package com.lennardwalter.battleships;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {
    public static Map<String, String> parseQuery(String query) {
        return Arrays.stream(query.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a[1]));

    }
}
