package hms.service;

import hms.controller.HMSController;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.Patient;
import java.util.List;

/**
 * Default report generator implementation.
 */
public class BasicReportService implements ReportService {

    private final HMSController controller;

    public BasicReportService(HMSController controller) {
        this.controller = controller;
    }

    @Override
    public String generateSummaryReport() {
        StringBuilder builder = new StringBuilder();
        builder.append("=== Hospital Management Report ===\n\n");
        builder.append("Total Patients: ").append(controller.getPatients().size()).append("\n");
        builder.append("Total Appointments: ").append(controller.getAppointments().size()).append("\n\n");
        builder.append("--- Doctor Schedules ---\n");

        for (Doctor doctor : controller.getDoctors()) {
            builder.append("\n").append(doctor.getSummary()).append("\n");
            List<Appointment> schedule = controller.getAppointmentsForDoctor(doctor.getId());
            if (schedule.isEmpty()) {
                builder.append("  No appointments scheduled.\n");
            } else {
                for (Appointment appointment : schedule) {
                    Patient patient = controller.findPatientById(appointment.getPatientId()).orElse(null);
                    String patientName = patient != null ? patient.getName() : appointment.getPatientId();
                    builder.append("  - ")
                            .append(appointment.getFormattedDateTime())
                            .append(" | Patient: ")
                            .append(patientName)
                            .append(" | Status: ")
                            .append(appointment.getStatus())
                            .append("\n");
                }
            }
        }

        return builder.toString();
    }
}
