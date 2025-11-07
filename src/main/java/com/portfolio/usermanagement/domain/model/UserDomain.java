package com.portfolio.usermanagement.domain.model;

import com.portfolio.usermanagement.domain.valueobject.Email;
import com.portfolio.usermanagement.domain.valueobject.FullName;
import com.portfolio.usermanagement.domain.valueobject.Username;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Domain model for User containing business logic and domain rules.
 * Separate from JPA entity to maintain clean architecture.
 */
@Getter
@Builder
public class UserDomain {

    private final UUID id;
    private final Username username;
    private final Email email;
    private final FullName fullName;
    private final String encryptedPassword;

    @Builder.Default
    private final Boolean enabled = true;

    @Builder.Default
    private final Boolean accountNonLocked = true;

    @Builder.Default
    private final Boolean accountNonExpired = true;

    @Builder.Default
    private final Boolean credentialsNonExpired = true;

    @Builder.Default
    private final Set<String> roleNames = new HashSet<>();

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public boolean hasRole(String roleName) {
        return roleNames.contains(roleName);
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isRegularUser() {
        return hasRole("ROLE_USER") && !isAdmin();
    }

    public boolean isAccountActive() {
        return Boolean.TRUE.equals(enabled)
            && Boolean.TRUE.equals(accountNonLocked)
            && Boolean.TRUE.equals(accountNonExpired)
            && Boolean.TRUE.equals(credentialsNonExpired);
    }

    public boolean canLogin() {
        return isAccountActive();
    }

    public int getRoleCount() {
        return roleNames.size();
    }

    public boolean hasRoles() {
        return !roleNames.isEmpty();
    }

    public boolean isEmailFromDomain(String domain) {
        return email.isFromDomain(domain);
    }

    public String getDisplayName() {
        if (fullName != null) {
            return fullName.getFullName();
        }
        return username.getValue();
    }

    public String getInitials() {
        if (fullName != null) {
            return fullName.getInitials();
        }
        String usernameStr = username.getValue();
        return usernameStr.length() >= 2
            ? usernameStr.substring(0, 2).toUpperCase()
            : usernameStr.toUpperCase();
    }

    public boolean isNewAccount() {
        if (createdAt == null) {
            return false;
        }
        return createdAt.isAfter(LocalDateTime.now().minusDays(1));
    }

    public boolean isRecentlyModified() {
        if (updatedAt == null) {
            return false;
        }
        return updatedAt.isAfter(LocalDateTime.now().minusHours(1));
    }

    /** @throws IllegalStateException if user is not an admin */
    public void requireAdmin() {
        if (!isAdmin()) {
            throw new IllegalStateException("User does not have admin privileges");
        }
    }

    /** @throws IllegalStateException if account is not active */
    public void requireActiveAccount() {
        if (!isAccountActive()) {
            throw new IllegalStateException("User account is not active");
        }
    }
}
