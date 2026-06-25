package hms.model;

/**
 * Supported login roles in the hospital system.
 */
public enum UserRole {
    ADMIN("Administrator"),
    DOCTOR("Doctor"),
    RECEPTIONIST("Receptionist");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
