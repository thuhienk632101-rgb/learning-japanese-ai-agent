package com.learnjapanese.application.agent;

import com.learnjapanese.domain.user.JlptLevel;
import com.learnjapanese.interfaces.rest.dto.ChatRequest;
import com.learnjapanese.interfaces.rest.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JapaneseAIAgentService {

    private final ChatClient chatClient;
    private final ConversationSessionManager sessionManager;

    private static final String SYSTEM_PROMPT_TEMPLATE = """
            Bạn là một giáo viên tiếng Nhật AI chuyên nghiệp và thân thiện.
            Học viên đang học ở trình độ JLPT %s.

            Nguyên tắc:
            1. Luôn sử dụng cả tiếng Nhật (Hiragana/Katakana/Kanji) và phiên âm Romaji khi giới thiệu từ mới.
            2. Giải thích bằng tiếng Việt khi học viên cần giải thích.
            3. Đưa ra ví dụ thực tế, dễ nhớ phù hợp với trình độ %s.
            4. Khuyến khích và tạo môi trường học tập tích cực.
            5. Nếu học viên mắc lỗi ngữ pháp, nhẹ nhàng sửa và giải thích.
            """;

    public ChatResponse chat(String sessionId, ChatRequest request, JlptLevel level) {
        log.info("AI Agent processing message for session: {}, level: {}", sessionId, level);

        List<Message> history = sessionManager.getHistory(sessionId);
        String systemPrompt = SYSTEM_PROMPT_TEMPLATE.formatted(level.name(), level.name());

        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));
        messages.addAll(history);
        messages.add(new UserMessage(request.message()));

        ChatResponse response = chatClient.prompt()
                .messages(messages)
                .call()
                .chatResponse();

        String assistantContent = response.getResult().getOutput().getContent();
        sessionManager.addMessage(sessionId, request.message(), assistantContent);

        return response;
    }
}
