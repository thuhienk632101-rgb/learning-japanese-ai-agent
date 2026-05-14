package com.learnjapanese.domain.vocabulary;

import com.learnjapanese.domain.user.JlptLevel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VocabularyRepository {
    Vocabulary save(Vocabulary vocabulary);
    Optional<Vocabulary> findById(UUID id);
    List<Vocabulary> findByJlptLevel(JlptLevel level);
    List<Vocabulary> searchByWord(String keyword);
    List<Vocabulary> findRandomByJlptLevel(JlptLevel level, int limit);
}
