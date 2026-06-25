package hms.model;

/**
 * Administrator account with full system access.
 */
public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password, UserRole.ADMIN);
    }

    @Override
    public boolean canManageDoctors() {
        return true;
    }

    @Override
    public boolean canManagePatients() {
        return true;
    }

    @Override
    public boolean canManageAppointments() {
        return true;
    }

    @Override
    public boolean canViewReports() {
        return true;
    }
}
