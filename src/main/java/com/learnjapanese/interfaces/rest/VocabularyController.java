package com.learnjapanese.interfaces.rest;

import com.learnjapanese.domain.user.JlptLevel;
import com.learnjapanese.domain.vocabulary.Vocabulary;
import com.learnjapanese.domain.vocabulary.VocabularyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vocabulary")
@RequiredArgsConstructor
@Tag(name = "Vocabulary", description = "Japanese vocabulary management APIs")
public class VocabularyController {

    private final VocabularyRepository vocabularyRepository;

    @GetMapping
    @Operation(summary = "Get vocabulary by JLPT level")
    public ResponseEntity<List<Vocabulary>> getByLevel(
            @RequestParam(defaultValue = "N5") JlptLevel level) {
        return ResponseEntity.ok(vocabularyRepository.findByJlptLevel(level));
    }

    @GetMapping("/search")
    @Operation(summary = "Search vocabulary by keyword")
    public ResponseEntity<List<Vocabulary>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(vocabularyRepository.searchByWord(keyword));
    }
}
