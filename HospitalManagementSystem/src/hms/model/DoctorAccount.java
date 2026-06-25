package hms.model;

/**
 * Doctor login account linked to a doctor record in the system.
 */
public class DoctorAccount extends User {

    private final String doctorId;

    public DoctorAccount(String username, String password, String doctorId) {
        super(username, password, UserRole.DOCTOR);
        this.doctorId = doctorId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    @Override
    public boolean canManageDoctors() {
        return false;
    }

    @Override
    public boolean canManagePatients() {
        return false;
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
