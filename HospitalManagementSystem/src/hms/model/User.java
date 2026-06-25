package hms.model;

/**
 * Abstract user account used for authentication and role-based access.
 */
public abstract class User {

    private final String username;
    private final String password;
    private final UserRole role;

    protected User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean authenticate(String inputPassword) {
        return password.equals(inputPassword);
    }

    public abstract boolean canManageDoctors();

    public abstract boolean canManagePatients();

    public abstract boolean canManageAppointments();

    public abstract boolean canViewReports();
}
