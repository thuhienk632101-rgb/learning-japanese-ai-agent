package com.learnjapanese.application.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConversationSessionManager {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_PREFIX = "chat:session:";
    private static final Duration SESSION_TTL = Duration.ofHours(2);
    private static final int MAX_HISTORY = 20; // Keep last 20 messages

    @SuppressWarnings("unchecked")
    public List<Message> getHistory(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        List<Object> rawHistory = redisTemplate.opsForList().range(key, 0, -1);
        if (rawHistory == null) return new ArrayList<>();

        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < rawHistory.size(); i += 2) {
            if (i + 1 < rawHistory.size()) {
                messages.add(new UserMessage((String) rawHistory.get(i)));
                messages.add(new AssistantMessage((String) rawHistory.get(i + 1)));
            }
        }
        return messages;
    }

    public void addMessage(String sessionId, String userMessage, String assistantMessage) {
        String key = SESSION_PREFIX + sessionId;
        redisTemplate.opsForList().rightPushAll(key, userMessage, assistantMessage);

        // Trim to keep only the last MAX_HISTORY messages
        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size > MAX_HISTORY * 2L) {
            redisTemplate.opsForList().trim(key, size - MAX_HISTORY * 2L, -1);
        }

        redisTemplate.expire(key, SESSION_TTL);
        log.debug("Session {} updated, total messages: {}", sessionId, size);
    }

    public void clearSession(String sessionId) {
        redisTemplate.delete(SESSION_PREFIX + sessionId);
    }
}
