package com.learnjapanese.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LearningProfile {

    @Enumerated(EnumType.STRING)
    @Column(name = "jlpt_level", nullable = false)
    private JlptLevel jlptLevel;

    public static LearningProfile defaultProfile() {
        return new LearningProfile(JlptLevel.N5);
    }

    public LearningProfile upgrade() {
        if (this.jlptLevel.ordinal() > 0) {
            JlptLevel[] levels = JlptLevel.values();
            return new LearningProfile(levels[this.jlptLevel.ordinal() - 1]);
        }
        return this;
    }
}
