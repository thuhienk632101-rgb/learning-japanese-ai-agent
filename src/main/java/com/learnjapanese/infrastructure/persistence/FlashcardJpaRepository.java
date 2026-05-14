package com.learnjapanese.infrastructure.persistence;

import com.learnjapanese.domain.flashcard.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardJpaRepository extends JpaRepository<Flashcard, UUID> {

    @Query("SELECT f FROM Flashcard f WHERE f.user.id = :userId AND f.nextReviewAt <= :now")
    List<Flashcard> findDueByUserId(@Param("userId") UUID userId,
                                    @Param("now") LocalDateTime now);

    boolean existsByUserIdAndVocabularyId(UUID userId, UUID vocabularyId);
}
