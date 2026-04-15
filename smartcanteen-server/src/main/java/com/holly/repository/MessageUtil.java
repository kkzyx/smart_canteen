package com.holly.repository;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.holly.context.ToolContext;
import com.holly.tools.result.ToolResultHolder;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageUtil {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TOOL_NAME = "tool_name";
    private static final String ARGUMENTS = "arguments";

    public static String toJson(ChatMessage message) {
        ChatMessageType type = message.type();
        RedisMessage redisMessage = new RedisMessage();

        if (type == ChatMessageType.USER) {
            UserMessage userMessage = (UserMessage) message;
            Object originalUserMessage = ToolContext.get(ToolContext.ORIGINAL_USER_MESSAGE);
            String textContent = originalUserMessage instanceof String original && !original.isBlank()
                    ? original
                    : userMessage.singleText();
            redisMessage.setTextContent(textContent);
            redisMessage.setType(String.valueOf(ChatMessageType.USER));
        } else if (type == ChatMessageType.AI) {
            AiMessage aiMessage = (AiMessage) message;
            String text = aiMessage.text();
            redisMessage.setType(String.valueOf(ChatMessageType.AI));
            redisMessage.setTextContent(text);

            if ("".equals(text)) {
                MyAiMessage myAiMessage = (MyAiMessage) aiMessage;
                Map<String, Object> params = myAiMessage.getParams();
                if (ObjectUtil.isNotEmpty(params)) {
                    redisMessage.setParams(params);
                }
            }

            List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();
            if (toolExecutionRequests != null && !toolExecutionRequests.isEmpty()) {
                String messageId = toolExecutionRequests.get(0).id();
                var requestId = Convert.toStr(ToolResultHolder.get(messageId, ToolContext.REQUEST_ID));
                Map<String, Object> params = ToolResultHolder.get(requestId);
                String name = toolExecutionRequests.get(0).name();
                String arguments = toolExecutionRequests.get(0).arguments();
                redisMessage.setMetadata(Map.of(ID, messageId, NAME, name, ARGUMENTS, arguments));
                if (ObjectUtil.isNotEmpty(params)) {
                    redisMessage.setParams(params);
                }
                ToolResultHolder.remove(messageId);
            }
        } else if (type == ChatMessageType.TOOL_EXECUTION_RESULT) {
            ToolExecutionResultMessage toolExecutionResultMessage = (ToolExecutionResultMessage) message;
            String toolCallId = toolExecutionResultMessage.id();
            String toolName = toolExecutionResultMessage.toolName();
            String text = toolExecutionResultMessage.text();
            redisMessage.setTextContent(text);
            redisMessage.setMetadata(Map.of(ID, toolCallId, TOOL_NAME, toolName));
            redisMessage.setType(String.valueOf(ChatMessageType.TOOL_EXECUTION_RESULT));
        } else if (type == ChatMessageType.SYSTEM) {
            SystemMessage systemMessage = (SystemMessage) message;
            redisMessage.setTextContent(systemMessage.text());
            redisMessage.setType(String.valueOf(ChatMessageType.SYSTEM));
        }

        return JSONUtil.toJsonStr(redisMessage);
    }

    public static ChatMessage toMessage(RedisMessage redisMessage) {
        ChatMessageType messageType = ChatMessageType.valueOf(redisMessage.getType());
        switch (messageType) {
            case SYSTEM -> {
                return new SystemMessage(redisMessage.getTextContent());
            }
            case USER -> {
                return new UserMessage(redisMessage.getTextContent());
            }
            case AI -> {
                Map<String, Object> metadata = redisMessage.getMetadata();
                if (metadata.isEmpty()) {
                    return new MyAiMessage(redisMessage.getTextContent(), redisMessage.getParams());
                }
                String id = metadata.get(ID).toString();
                String name = metadata.get(NAME).toString();
                String arguments = metadata.get(ARGUMENTS).toString();
                ToolExecutionRequest toolExecutionRequest = ToolExecutionRequest.builder()
                        .id(id)
                        .name(name)
                        .arguments(arguments)
                        .build();
                List<ToolExecutionRequest> toolExecutionRequests = List.of(toolExecutionRequest);
                return new MyAiMessage(redisMessage.getTextContent(), toolExecutionRequests, redisMessage.getParams());
            }
            case TOOL_EXECUTION_RESULT -> {
                String toolCallId = redisMessage.getMetadata().get(ID).toString();
                String toolName = redisMessage.getMetadata().get(TOOL_NAME).toString();
                String textContent = redisMessage.getTextContent();
                return new ToolExecutionResultMessage(toolCallId, toolName, textContent);
            }
            default -> throw new RuntimeException("Message data conversion failed.");
        }
    }

    private static List<MyToolExecutionRequest> convertToMyToolExecutionRequests(List<ToolExecutionRequest> toolExecutionRequests) {
        if (toolExecutionRequests == null || toolExecutionRequests.isEmpty()) {
            return new ArrayList<>();
        }

        return toolExecutionRequests.stream()
                .map(request -> new MyToolExecutionRequest(
                        request.id(),
                        request.name(),
                        request.arguments()
                ))
                .collect(Collectors.toList());
    }

    private static List<ToolExecutionRequest> convertToToolExecutionRequests(List<MyToolExecutionRequest> myToolExecutionRequests) {
        if (myToolExecutionRequests == null
                || myToolExecutionRequests.isEmpty()
                || myToolExecutionRequests.stream().allMatch(Objects::isNull)) {
            return new ArrayList<>();
        }

        return myToolExecutionRequests.stream()
                .map(request -> ToolExecutionRequest.builder()
                        .id(request.id())
                        .name(request.name())
                        .arguments(request.arguments())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
