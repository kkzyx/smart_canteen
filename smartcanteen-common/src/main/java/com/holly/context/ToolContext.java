package com.holly.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ToolContext {

    private static final Map<String, Object> CONTEXT = new ConcurrentHashMap<>();

    public static final String USER_ID = "userId";
    public static final String REQUEST_ID = "requestId";
    public static final String ORIGINAL_USER_MESSAGE = "originalUserMessage";

    public static void put(String key, Object value) {
        CONTEXT.put(key, value);
    }

    public static Object get(String key) {
        return CONTEXT.get(key);
    }

    public static void clear() {
        CONTEXT.clear();
    }
}
