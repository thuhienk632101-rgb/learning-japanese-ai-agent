package com.learnjapanese.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JlptLevel {
    N1("Advanced"),
    N2("Upper-Intermediate"),
    N3("Intermediate"),
    N4("Elementary"),
    N5("Beginner");

    private final String description;
}
