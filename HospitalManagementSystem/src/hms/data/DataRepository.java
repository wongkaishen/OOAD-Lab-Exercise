package hms.data;

import hms.model.Admin;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.DoctorAccount;
import hms.model.Patient;
import hms.model.Receptionist;
import hms.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * In-memory data store for the hospital management system.
 */
public class DataRepository {

    private final List<User> users = new ArrayList<>();
    private final List<Patient> patients = new ArrayList<>();
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();

    private int patientCounter = 3;
    private int doctorCounter = 3;
    private int appointmentCounter = 1;

    public DataRepository() {
        seedData();
    }

    private void seedData() {
        users.add(new Admin("admin", "admin123"));
        users.add(new DoctorAccount("doctor", "doctor123", "D001"));
        users.add(new Receptionist("receptionist", "recep123"));

        patients.add(new Patient("P001", "Alice Tan", 28, "Female", "Asthma"));
        patients.add(new Patient("P002", "Bob Lee", 45, "Male", "Hypertension"));

        doctors.add(new Doctor("D001", "Dr. Sarah Lim", "Cardiology"));
        doctors.add(new Doctor("D002", "Dr. James Wong", "Pediatrics"));

        appointments.add(new Appointment("A001", "P001", "D001",
                LocalDateTime.of(2026, 6, 25, 10, 0), "Scheduled"));
    }

    public Optional<User> findUser(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public List<Patient> getPatients() {
        return Collections.unmodifiableList(patients);
    }

    public List<Doctor> getDoctors() {
        return Collections.unmodifiableList(doctors);
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public Optional<Patient> findPatientById(String id) {
        return patients.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Optional<Doctor> findDoctorById(String id) {
        return doctors.stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    public Patient addPatient(String name, int age, String gender, String medicalHistory) {
        String id = String.format("P%03d", patientCounter++);
        Patient patient = new Patient(id, name, age, gender, medicalHistory);
        patients.add(patient);
        return patient;
    }

    public void updatePatient(Patient patient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId().equals(patient.getId())) {
                patients.set(i, patient);
                return;
            }
        }
    }

    public Doctor addDoctor(String name, String specialization) {
        String id = String.format("D%03d", doctorCounter++);
        Doctor doctor = new Doctor(id, name, specialization);
        doctors.add(doctor);
        return doctor;
    }

    public Appointment addAppointment(String patientId, String doctorId, LocalDateTime dateTime) {
        String id = String.format("A%03d", appointmentCounter++);
        Appointment appointment = new Appointment(id, patientId, doctorId, dateTime, "Scheduled");
        appointments.add(appointment);
        return appointment;
    }

    public boolean hasDoctorSlotConflict(String doctorId, LocalDateTime dateTime, String excludeId) {
        return appointments.stream()
                .filter(a -> excludeId == null || !a.getId().equals(excludeId))
                .anyMatch(a -> a.getDoctorId().equals(doctorId)
                        && a.getDateTime().equals(dateTime));
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(doctorId)) {
                result.add(appointment);
            }
        }
        return result;
    }
}
