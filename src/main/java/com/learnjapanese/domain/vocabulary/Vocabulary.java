package com.learnjapanese.domain.vocabulary;

import com.learnjapanese.domain.user.JlptLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vocabulary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String word;

    @Column(nullable = false, length = 100)
    private String reading; // hiragana/katakana

    @Column(nullable = false)
    private String meaning;

    @Column(name = "example_jp")
    private String exampleJp;

    @Column(name = "example_vi")
    private String exampleVi;

    @Enumerated(EnumType.STRING)
    @Column(name = "jlpt_level", nullable = false)
    private JlptLevel jlptLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "word_type")
    private WordType wordType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum WordType {
        NOUN, VERB_U, VERB_RU, I_ADJECTIVE, NA_ADJECTIVE, ADVERB, PARTICLE, EXPRESSION
    }
}
