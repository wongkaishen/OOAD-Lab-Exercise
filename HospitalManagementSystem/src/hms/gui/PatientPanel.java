package hms.gui;

import hms.controller.HMSController;
import hms.model.Patient;
import hms.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for adding and updating patient records.
 */
public class PatientPanel extends JPanel implements ActionListener {

    private final HMSController controller;
    private final JTextField idField = new JTextField(12);
    private final JTextField nameField = new JTextField(18);
    private final JTextField ageField = new JTextField(6);
    private final JTextField genderField = new JTextField(10);
    private final JTextArea historyArea = new JTextArea(4, 24);
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JButton addButton = new JButton("Add Patient");
    private final JButton updateButton = new JButton("Update Patient");
    private final JButton clearButton = new JButton("Clear");

    public PatientPanel(HMSController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));

        idField.setEditable(false);
        historyArea.setLineWrap(true);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Age", "Gender", "Medical History"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loadSelectedPatient();
            }
        });

        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
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
        form.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        form.add(idField, gbc);
        gbc.gridx = 2;
        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        form.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        form.add(ageField, gbc);
        gbc.gridx = 2;
        form.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 3;
        form.add(genderField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Medical History:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        form.add(new JScrollPane(historyArea), gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 3;
        addButton.addActionListener(this);
        updateButton.addActionListener(this);
        clearButton.addActionListener(this);
        form.add(addButton, gbc);
        gbc.gridx = 2;
        form.add(updateButton, gbc);
        gbc.gridx = 3;
        form.add(clearButton, gbc);

        return form;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Patient patient : controller.getPatients()) {
            tableModel.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getMedicalHistory()
            });
        }
    }

    private void loadSelectedPatient() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        ageField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        genderField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        historyArea.setText(String.valueOf(tableModel.getValueAt(row, 4)));
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderField.setText("");
        historyArea.setText("");
        table.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == addButton) {
                addPatient();
            } else if (source == updateButton) {
                updatePatient();
            } else if (source == clearButton) {
                clearForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Patient Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPatient() {
        int age = Integer.parseInt(ageField.getText().trim());
        String name = nameField.getText();
        String gender = genderField.getText();
        if (!ValidationUtil.isValidPatientInput(name, age, gender)) {
            throw new IllegalArgumentException("Please fill in valid patient details.");
        }
        controller.addPatient(name, age, gender, historyArea.getText());
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, "Patient added successfully.");
    }

    private void updatePatient() {
        String id = idField.getText().trim();
        if (ValidationUtil.isBlank(id)) {
            throw new IllegalArgumentException("Select a patient to update.");
        }
        int age = Integer.parseInt(ageField.getText().trim());
        controller.updatePatient(id, nameField.getText(), age,
                genderField.getText(), historyArea.getText());
        refreshTable();
        JOptionPane.showMessageDialog(this, "Patient updated successfully.");
    }
}
