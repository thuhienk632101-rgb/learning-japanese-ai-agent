package com.learnjapanese.application.flashcard;

import com.learnjapanese.domain.flashcard.Flashcard;
import com.learnjapanese.domain.flashcard.FlashcardRepository;
import com.learnjapanese.domain.user.User;
import com.learnjapanese.domain.vocabulary.Vocabulary;
import com.learnjapanese.domain.vocabulary.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final VocabularyRepository vocabularyRepository;

    /**
     * Get all flashcards due for review today (SM-2 SRS Algorithm)
     */
    @Transactional(readOnly = true)
    public List<Flashcard> getDueFlashcards(UUID userId) {
        return flashcardRepository.findDueByUserId(userId);
    }

    /**
     * Add a vocabulary word to the user's flashcard deck
     */
    @Transactional
    public Flashcard addToFlashcardDeck(User user, UUID vocabularyId) {
        Vocabulary vocabulary = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new IllegalArgumentException("Vocabulary not found: " + vocabularyId));

        if (flashcardRepository.existsByUserIdAndVocabularyId(user.getId(), vocabularyId)) {
            throw new IllegalStateException("Flashcard already exists for this vocabulary");
        }

        Flashcard flashcard = Flashcard.builder()
                .user(user)
                .vocabulary(vocabulary)
                .build();

        log.info("Adding flashcard for user: {}, vocabulary: {}", user.getId(), vocabularyId);
        return flashcardRepository.save(flashcard);
    }

    /**
     * Submit a review result and update the SM-2 scheduling
     */
    @Transactional
    public Flashcard submitReview(UUID flashcardId, int quality) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new IllegalArgumentException("Flashcard not found: " + flashcardId));

        flashcard.applyReview(quality);
        Flashcard saved = flashcardRepository.save(flashcard);

        log.info("Flashcard {} reviewed with quality {}, next review in {} days",
                flashcardId, quality, saved.getIntervalDays());
        return saved;
    }
}
