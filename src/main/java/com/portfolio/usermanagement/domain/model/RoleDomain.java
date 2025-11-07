package com.portfolio.usermanagement.domain.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Domain model for roles with business logic.
 */
@Getter
@Builder
public class RoleDomain {

    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_MODERATOR
    }

    private final Long id;
    private final RoleName name;
    private final String description;

    public boolean isAdmin() {
        return RoleName.ROLE_ADMIN.equals(name);
    }

    public boolean isUser() {
        return RoleName.ROLE_USER.equals(name);
    }

    public boolean isModerator() {
        return RoleName.ROLE_MODERATOR.equals(name);
    }

    public String getRoleNameString() {
        return name.name();
    }

    /** Hierarchy: ADMIN > MODERATOR > USER */
    public boolean hasHigherPrivilegesThan(RoleDomain other) {
        return getPrivilegeLevel() > other.getPrivilegeLevel();
    }

    private int getPrivilegeLevel() {
        return switch (name) {
            case ROLE_ADMIN -> 3;
            case ROLE_MODERATOR -> 2;
            case ROLE_USER -> 1;
        };
    }
}
