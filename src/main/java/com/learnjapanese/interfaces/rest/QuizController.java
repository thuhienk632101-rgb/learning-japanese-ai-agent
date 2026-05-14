package com.learnjapanese.interfaces.rest;

import com.learnjapanese.application.quiz.QuizGeneratorService;
import com.learnjapanese.domain.user.JlptLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
@Tag(name = "Quiz", description = "AI-generated quiz APIs")
public class QuizController {

    private final QuizGeneratorService quizGeneratorService;

    @GetMapping("/generate")
    @Operation(summary = "Generate a quiz by JLPT level")
    public ResponseEntity<String> generateQuiz(
            @RequestParam(defaultValue = "N5") JlptLevel level,
            @RequestParam(defaultValue = "10") int count) {

        String quiz = quizGeneratorService.generateQuiz(level, Math.min(count, 20));
        return ResponseEntity.ok(quiz);
    }
}
