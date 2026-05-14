package com.learnjapanese.domain.flashcard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlashcardRepository {
    Flashcard save(Flashcard flashcard);
    Optional<Flashcard> findById(UUID id);
    List<Flashcard> findDueByUserId(UUID userId);
    boolean existsByUserIdAndVocabularyId(UUID userId, UUID vocabularyId);
}
