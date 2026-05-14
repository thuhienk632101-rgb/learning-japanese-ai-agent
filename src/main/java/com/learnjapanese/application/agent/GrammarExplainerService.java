package com.learnjapanese.application.agent;

import com.learnjapanese.domain.user.JlptLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrammarExplainerService {

    private final ChatClient chatClient;

    private static final String GRAMMAR_PROMPT = """
            Bạn là chuyên gia ngữ pháp tiếng Nhật.
            Hãy giải thích mẫu ngữ pháp sau cho học viên trình độ %s.

            Ngữ pháp: %s

            Hãy trả lời theo cấu trúc:
            1. 📌 Ý nghĩa và cách dùng
            2. 🔧 Cấu trúc (công thức)
            3. ✅ 3 ví dụ câu (có phiên âm và dịch nghĩa tiếng Việt)
            4. ⚠️ Lỗi hay mắc phải
            5. 💡 Mẹo ghi nhớ
            """;

    public String explainGrammar(String grammarPattern, JlptLevel level) {
        log.info("Explaining grammar: {} for level: {}", grammarPattern, level);

        return chatClient.prompt()
                .user(GRAMMAR_PROMPT.formatted(level.name(), grammarPattern))
                .call()
                .content();
    }
}
