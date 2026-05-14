package com.learnjapanese.interfaces.rest.dto;

public record ChatResponse(
        String content,
        String sessionId
) {}
