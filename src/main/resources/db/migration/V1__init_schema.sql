-- ============================================================
-- V1: Initial Schema for Learning Japanese AI Agent
-- ============================================================

-- Users
CREATE TABLE users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username    VARCHAR(50)  NOT NULL UNIQUE,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    jlpt_level  VARCHAR(5)   NOT NULL DEFAULT 'N5', -- N5, N4, N3, N2, N1
    role        VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER',
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Vocabulary
CREATE TABLE vocabulary (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    word         VARCHAR(100)  NOT NULL,
    reading      VARCHAR(100)  NOT NULL,  -- hiragana/katakana reading
    meaning      TEXT          NOT NULL,
    example_jp   TEXT,
    example_vi   TEXT,
    jlpt_level   VARCHAR(5)    NOT NULL,
    word_type    VARCHAR(20),             -- noun, verb, adjective...
    created_at   TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_vocabulary_jlpt_level ON vocabulary(jlpt_level);
CREATE INDEX idx_vocabulary_word ON vocabulary(word);

-- Kanji
CREATE TABLE kanji (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character    CHAR(1)      NOT NULL UNIQUE,
    onyomi       VARCHAR(100),
    kunyomi      VARCHAR(100),
    meaning      TEXT         NOT NULL,
    stroke_count INTEGER,
    jlpt_level   VARCHAR(5)   NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_kanji_jlpt_level ON kanji(jlpt_level);

-- Lessons
CREATE TABLE lessons (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(200) NOT NULL,
    content     TEXT         NOT NULL,
    jlpt_level  VARCHAR(5)   NOT NULL,
    lesson_type VARCHAR(30)  NOT NULL, -- VOCABULARY, GRAMMAR, KANJI, CONVERSATION
    order_index INTEGER      NOT NULL DEFAULT 0,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- User Lesson Progress
CREATE TABLE user_lesson_progress (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id    UUID         NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    status       VARCHAR(20)  NOT NULL DEFAULT 'NOT_STARTED', -- NOT_STARTED, IN_PROGRESS, COMPLETED
    score        INTEGER,
    completed_at TIMESTAMP,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, lesson_id)
);

-- Flashcards (SRS - Spaced Repetition System)
CREATE TABLE flashcards (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    vocabulary_id   UUID      NOT NULL REFERENCES vocabulary(id) ON DELETE CASCADE,
    ease_factor     FLOAT     NOT NULL DEFAULT 2.5,
    interval_days   INTEGER   NOT NULL DEFAULT 1,
    repetitions     INTEGER   NOT NULL DEFAULT 0,
    next_review_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    last_reviewed_at TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, vocabulary_id)
);

CREATE INDEX idx_flashcards_next_review ON flashcards(user_id, next_review_at);

-- Conversation Sessions
CREATE TABLE conversation_sessions (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    topic      VARCHAR(200),
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    ended_at   TIMESTAMP
);

-- Conversation Messages
CREATE TABLE conversation_messages (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id   UUID         NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    role         VARCHAR(20)  NOT NULL, -- USER, ASSISTANT
    content      TEXT         NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_messages_session ON conversation_messages(session_id, created_at);
