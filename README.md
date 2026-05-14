# 🇯🇵 Learning Japanese with AI Agent

> An AI-powered agent for learning Japanese — vocabulary, grammar, kanji, and conversation practice.

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.x |
| **AI / LLM** | Spring AI + OpenAI GPT-4o |
| **Database** | PostgreSQL + Flyway |
| **Cache** | Redis |
| **Security** | Spring Security + JWT |
| **API Docs** | SpringDoc OpenAPI 3 |
| **Container** | Docker + Docker Compose |
| **Build** | Maven |

## 🤖 Features

- 🗣️ **Conversation Partner** — AI luyện hội thoại tiếng Nhật
- 📖 **Grammar Explainer** — Giải thích ngữ pháp có ví dụ thực tế
- 🃏 **Smart Flashcard (SRS)** — Spaced Repetition System
- 🎯 **Quiz Generator** — Tạo câu hỏi theo chuẩn JLPT N5~N1
- 🈶 **Kanji Analyzer** — Phân tích và giải thích Kanji
- 📝 **Translation & Furigana** — Dịch + thêm furigana tự động

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Docker & Docker Compose
- OpenAI API Key

### Run with Docker
```bash
cp .env.example .env
# Điền OPENAI_API_KEY vào .env
docker-compose up -d
```

### Run locally
```bash
./mvnw spring-boot:run
```

API Docs: http://localhost:8080/swagger-ui.html

## 📁 Project Structure

```
src/main/java/com/learnjapanese/
├── domain/          # Domain Layer (DDD) — Entities, Value Objects, Repositories
├── application/     # Application Layer — Use Cases, Services
├── infrastructure/  # Infrastructure Layer — JPA, Redis, AI Client
└── interfaces/      # Interface Layer — REST Controllers, DTOs
```

## 📄 License

MIT License
