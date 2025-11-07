package com.portfolio.usermanagement.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

/**
 * Value object for usernames with validation.
 */
@Getter
@EqualsAndHashCode
public class Username {

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]{3,50}$"
    );

    private final String value;

    private Username(String value) {
        this.value = value;
    }

    public static Username of(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        String trimmed = username.trim();

        if (trimmed.length() < 3 || trimmed.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }

        if (!USERNAME_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(
                "Username can only contain letters, numbers, dots, underscores, and hyphens"
            );
        }

        return new Username(trimmed);
    }

    public boolean isAlphanumeric() {
        return value.matches("^[a-zA-Z0-9]+$");
    }

    public int length() {
        return value.length();
    }

    @Override
    public String toString() {
        return value;
    }
}
