package com.learnjapanese.infrastructure.persistence;

import com.learnjapanese.domain.user.JlptLevel;
import com.learnjapanese.domain.vocabulary.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VocabularyJpaRepository extends JpaRepository<Vocabulary, UUID> {

    List<Vocabulary> findByJlptLevel(JlptLevel level);

    @Query("SELECT v FROM Vocabulary v WHERE LOWER(v.word) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(v.meaning) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Vocabulary> searchByWord(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM vocabulary WHERE jlpt_level = :level ORDER BY RANDOM() LIMIT :limit",
           nativeQuery = true)
    List<Vocabulary> findRandomByJlptLevel(@Param("level") String level, @Param("limit") int limit);
}
