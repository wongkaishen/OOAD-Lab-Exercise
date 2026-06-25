package hms.gui;

import hms.controller.HMSController;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.DoctorAccount;
import hms.model.Patient;
import hms.model.User;
import hms.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for booking and viewing appointments.
 */
public class AppointmentPanel extends JPanel implements ActionListener {

    private final HMSController controller;
    private final JComboBox<String> patientCombo = new JComboBox<>();
    private final JComboBox<String> doctorCombo = new JComboBox<>();
    private final JTextField dateTimeField = new JTextField(16);
    private final DefaultTableModel tableModel;
    private final JButton bookButton = new JButton("Book Appointment");
    private final JButton refreshButton = new JButton("Refresh List");

    public AppointmentPanel(HMSController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));

        dateTimeField.setToolTipText("Format: " + ValidationUtil.dateTimeFormatHint());

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Patient", "Doctor", "Date & Time", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);

        bookButton.addActionListener(this);
        refreshButton.addActionListener(this);
        loadComboBoxes();
        refreshTable();
    }

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        form.add(patientCombo, gbc);
        gbc.gridx = 2;
        form.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 3;
        form.add(doctorCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Date & Time:"), gbc);
        gbc.gridx = 1;
        form.add(dateTimeField, gbc);
        gbc.gridx = 2;
        form.add(new JLabel("(" + ValidationUtil.dateTimeFormatHint() + ")"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        form.add(bookButton, gbc);
        gbc.gridx = 2;
        form.add(refreshButton, gbc);

        User user = controller.getCurrentUser();
        if (user instanceof DoctorAccount) {
            doctorCombo.setEnabled(false);
        }

        return form;
    }

    private void loadComboBoxes() {
        DefaultComboBoxModel<String> patientModel = new DefaultComboBoxModel<>();
        for (Patient patient : controller.getPatients()) {
            patientModel.addElement(patient.getId() + " - " + patient.getName());
        }
        patientCombo.setModel(patientModel);

        DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();
        for (Doctor doctor : controller.getDoctors()) {
            doctorModel.addElement(doctor.getId() + " - " + doctor.getName());
        }
        doctorCombo.setModel(doctorModel);

        User user = controller.getCurrentUser();
        if (user instanceof DoctorAccount doctorAccount) {
            String linkedId = doctorAccount.getDoctorId();
            for (int i = 0; i < doctorModel.getSize(); i++) {
                if (doctorModel.getElementAt(i).startsWith(linkedId + " -")) {
                    doctorCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        User user = controller.getCurrentUser();

        for (Appointment appointment : controller.getAppointments()) {
            if (user instanceof DoctorAccount doctorAccount
                    && !appointment.getDoctorId().equals(doctorAccount.getDoctorId())) {
                continue;
            }

            Patient patient = controller.findPatientById(appointment.getPatientId()).orElse(null);
            Doctor doctor = controller.findDoctorById(appointment.getDoctorId()).orElse(null);
            String patientName = patient != null ? patient.getName() : appointment.getPatientId();
            String doctorName = doctor != null ? doctor.getName() : appointment.getDoctorId();

            tableModel.addRow(new Object[]{
                appointment.getId(),
                patientName,
                doctorName,
                appointment.getFormattedDateTime(),
                appointment.getStatus()
            });
        }
    }

    private String extractId(String comboValue) {
        return comboValue.split(" - ")[0].trim();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == refreshButton) {
            loadComboBoxes();
            refreshTable();
            return;
        }
        if (source != bookButton) {
            return;
        }

        try {
            if (patientCombo.getSelectedItem() == null || doctorCombo.getSelectedItem() == null) {
                throw new IllegalArgumentException("Select both patient and doctor.");
            }

            String patientId = extractId(patientCombo.getSelectedItem().toString());
            String doctorId = extractId(doctorCombo.getSelectedItem().toString());
            LocalDateTime dateTime = ValidationUtil.parseDateTime(dateTimeField.getText());

            controller.bookAppointment(patientId, doctorId, dateTime);
            refreshTable();
            dateTimeField.setText("");
            JOptionPane.showMessageDialog(this, "Appointment booked successfully.");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date/time. Use format: " + ValidationUtil.dateTimeFormatHint(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Appointment Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
