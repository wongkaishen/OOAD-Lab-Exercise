package hms.model;

/**
 * Receptionist account for front-desk operations.
 */
public class Receptionist extends User {

    public Receptionist(String username, String password) {
        super(username, password, UserRole.RECEPTIONIST);
    }

    @Override
    public boolean canManageDoctors() {
        return false;
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
