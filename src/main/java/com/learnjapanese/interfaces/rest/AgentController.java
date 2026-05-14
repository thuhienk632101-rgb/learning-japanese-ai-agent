package com.learnjapanese.interfaces.rest;

import com.learnjapanese.application.agent.GrammarExplainerService;
import com.learnjapanese.application.agent.JapaneseAIAgentService;
import com.learnjapanese.domain.user.JlptLevel;
import com.learnjapanese.interfaces.rest.dto.ChatRequest;
import com.learnjapanese.interfaces.rest.dto.ChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
@Tag(name = "AI Agent", description = "Japanese AI Learning Agent APIs")
public class AgentController {

    private final JapaneseAIAgentService agentService;
    private final GrammarExplainerService grammarExplainerService;

    @PostMapping("/chat/{sessionId}")
    @Operation(summary = "Chat with AI Japanese teacher")
    public ResponseEntity<ChatResponse> chat(
            @PathVariable String sessionId,
            @Valid @RequestBody ChatRequest request,
            @RequestParam(defaultValue = "N5") JlptLevel level,
            @AuthenticationPrincipal UserDetails userDetails) {

        var response = agentService.chat(sessionId, request, level);
        String content = response.getResult().getOutput().getContent();
        return ResponseEntity.ok(new ChatResponse(content, sessionId));
    }

    @GetMapping("/grammar/explain")
    @Operation(summary = "Explain a Japanese grammar pattern")
    public ResponseEntity<ChatResponse> explainGrammar(
            @RequestParam String pattern,
            @RequestParam(defaultValue = "N5") JlptLevel level) {

        String explanation = grammarExplainerService.explainGrammar(pattern, level);
        return ResponseEntity.ok(new ChatResponse(explanation, null));
    }
}
