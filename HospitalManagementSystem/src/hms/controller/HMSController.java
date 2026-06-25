package hms.controller;

import hms.data.DataRepository;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.Patient;
import hms.model.User;
import hms.service.BasicReportService;
import hms.service.ReportService;
import hms.util.ValidationUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Central controller coordinating business logic for the HMS.
 */
public class HMSController {

    private final DataRepository repository;
    private final ReportService reportService;
    private User currentUser;

    public HMSController() {
        this.repository = new DataRepository();
        this.reportService = new BasicReportService(this);
    }

    public Optional<User> login(String username, String password) {
        if (!ValidationUtil.isValidLogin(username, password)) {
            return Optional.empty();
        }

        Optional<User> userOptional = repository.findUser(username.trim());
        if (userOptional.isPresent() && userOptional.get().authenticate(password)) {
            currentUser = userOptional.get();
            return Optional.of(currentUser);
        }
        return Optional.empty();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public List<Patient> getPatients() {
        return repository.getPatients();
    }

    public List<Doctor> getDoctors() {
        return repository.getDoctors();
    }

    public List<Appointment> getAppointments() {
        return repository.getAppointments();
    }

    public Optional<Patient> findPatientById(String id) {
        return repository.findPatientById(id);
    }

    public Optional<Doctor> findDoctorById(String id) {
        return repository.findDoctorById(id);
    }

    public Patient addPatient(String name, int age, String gender, String medicalHistory) {
        if (!ValidationUtil.isValidPatientInput(name, age, gender)) {
            throw new IllegalArgumentException("Invalid patient details.");
        }
        return repository.addPatient(name.trim(), age, gender.trim(),
                medicalHistory == null ? "" : medicalHistory.trim());
    }

    public void updatePatient(String id, String name, int age, String gender, String medicalHistory) {
        if (!ValidationUtil.isValidPatientInput(name, age, gender)) {
            throw new IllegalArgumentException("Invalid patient details.");
        }
        Patient patient = repository.findPatientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found."));
        patient.setName(name.trim());
        patient.setAge(age);
        patient.setGender(gender.trim());
        patient.setMedicalHistory(medicalHistory == null ? "" : medicalHistory.trim());
        repository.updatePatient(patient);
    }

    public Doctor addDoctor(String name, String specialization) {
        if (!ValidationUtil.isValidDoctorInput(name, specialization)) {
            throw new IllegalArgumentException("Invalid doctor details.");
        }
        return repository.addDoctor(name.trim(), specialization.trim());
    }

    public Appointment bookAppointment(String patientId, String doctorId, LocalDateTime dateTime) {
        if (repository.findPatientById(patientId).isEmpty()) {
            throw new IllegalArgumentException("Patient not found.");
        }
        if (repository.findDoctorById(doctorId).isEmpty()) {
            throw new IllegalArgumentException("Doctor not found.");
        }
        if (repository.hasDoctorSlotConflict(doctorId, dateTime, null)) {
            throw new IllegalArgumentException("Doctor already has an appointment at this time.");
        }
        return repository.addAppointment(patientId, doctorId, dateTime);
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        return repository.getAppointmentsForDoctor(doctorId);
    }

    public String generateReport() {
        return reportService.generateSummaryReport();
    }
}
