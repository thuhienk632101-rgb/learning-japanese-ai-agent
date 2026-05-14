package com.learnjapanese.application.quiz;

import com.learnjapanese.domain.user.JlptLevel;
import com.learnjapanese.domain.vocabulary.Vocabulary;
import com.learnjapanese.domain.vocabulary.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizGeneratorService {

    private final ChatClient chatClient;
    private final VocabularyRepository vocabularyRepository;

    private static final String QUIZ_PROMPT = """
            Dựa trên các từ vựng sau của JLPT %s, hãy tạo %d câu hỏi trắc nghiệm.
            Từ vựng: %s

            Yêu cầu:
            - Mỗi câu có 4 lựa chọn (A, B, C, D), chỉ 1 đáp án đúng
            - Đa dạng loại câu: điền từ, chọn nghĩa, chọn cách đọc, chọn kanji
            - Trả về JSON array theo format:
            [
              {
                "question": "...",
                "options": {"A": "...", "B": "...", "C": "...", "D": "..."},
                "answer": "A",
                "explanation": "..."
              }
            ]
            """;

    public String generateQuiz(JlptLevel level, int questionCount) {
        log.info("Generating {} quiz questions for level: {}", questionCount, level);

        List<Vocabulary> vocabularies = vocabularyRepository.findRandomByJlptLevel(level, questionCount * 2);
        String wordList = vocabularies.stream()
                .map(v -> v.getWord() + "(" + v.getReading() + ")=" + v.getMeaning())
                .reduce("", (a, b) -> a + ", " + b);

        return chatClient.prompt()
                .user(QUIZ_PROMPT.formatted(level.name(), questionCount, wordList))
                .call()
                .content();
    }
}
