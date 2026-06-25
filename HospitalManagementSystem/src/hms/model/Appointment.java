package hms.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Appointment linking a patient to a doctor at a specific date and time.
 */
public class Appointment {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String id;
    private String patientId;
    private String doctorId;
    private LocalDateTime dateTime;
    private String status;

    public Appointment(String id, String patientId, String doctorId,
                       LocalDateTime dateTime, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormattedDateTime() {
        return dateTime.format(FORMATTER);
    }
}
