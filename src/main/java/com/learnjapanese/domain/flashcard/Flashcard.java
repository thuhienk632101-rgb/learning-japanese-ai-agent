package com.learnjapanese.domain.flashcard;

import com.learnjapanese.domain.user.User;
import com.learnjapanese.domain.vocabulary.Vocabulary;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Flashcard entity implementing SM-2 Spaced Repetition Algorithm.
 * Reference: https://www.supermemo.com/en/blog/application-of-a-computer-to-improve-the-results-obtained-in-working-with-the-supermemo-method
 */
@Entity
@Table(name = "flashcards")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @Column(name = "ease_factor", nullable = false)
    private double easeFactor; // default 2.5

    @Column(name = "interval_days", nullable = false)
    private int intervalDays; // default 1

    @Column(nullable = false)
    private int repetitions; // default 0

    @Column(name = "next_review_at", nullable = false)
    private LocalDateTime nextReviewAt;

    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.easeFactor = 2.5;
        this.intervalDays = 1;
        this.repetitions = 0;
        this.nextReviewAt = LocalDateTime.now();
    }

    /**
     * Apply SM-2 algorithm based on quality of answer (0-5).
     * 0-1: complete blackout -> reset
     * 2:   incorrect, but easy to recall
     * 3:   correct with serious difficulty
     * 4:   correct with some hesitation
     * 5:   perfect response
     */
    public void applyReview(int quality) {
        if (quality < 0 || quality > 5) {
            throw new IllegalArgumentException("Quality must be between 0 and 5");
        }

        if (quality < 3) {
            this.repetitions = 0;
            this.intervalDays = 1;
        } else {
            double newEaseFactor = this.easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
            this.easeFactor = Math.max(1.3, newEaseFactor);

            this.intervalDays = switch (this.repetitions) {
                case 0 -> 1;
                case 1 -> 6;
                default -> (int) Math.round(this.intervalDays * this.easeFactor);
            };
            this.repetitions++;
        }

        this.lastReviewedAt = LocalDateTime.now();
        this.nextReviewAt = LocalDateTime.now().plusDays(this.intervalDays);
    }

    public boolean isDueForReview() {
        return LocalDateTime.now().isAfter(this.nextReviewAt);
    }
}
