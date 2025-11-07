package com.portfolio.usermanagement.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value object for person names with validation.
 */
@Getter
@EqualsAndHashCode
public class FullName {

    private final String firstName;
    private final String lastName;

    private FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static FullName of(String firstName, String lastName) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");

        return new FullName(
            capitalize(firstName.trim()),
            capitalize(lastName.trim())
        );
    }

    private static void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }

        String trimmed = name.trim();

        if (trimmed.length() > 50) {
            throw new IllegalArgumentException(fieldName + " must not exceed 50 characters");
        }

        if (!trimmed.matches("^[a-zA-Z\\s'-]+$")) {
            throw new IllegalArgumentException(
                fieldName + " can only contain letters, spaces, hyphens, and apostrophes"
            );
        }
    }

    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getInitials() {
        return firstName.substring(0, 1).toUpperCase()
             + lastName.substring(0, 1).toUpperCase();
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
