package hms.gui;

import hms.controller.HMSController;
import hms.model.Doctor;
import hms.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Panel for managing doctor profiles.
 */
public class DoctorPanel extends JPanel implements ActionListener {

    private final HMSController controller;
    private final JTextField nameField = new JTextField(18);
    private final JTextField specializationField = new JTextField(18);
    private final DefaultTableModel tableModel;
    private final JButton addButton = new JButton("Add Doctor");

    public DoctorPanel(HMSController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
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
        form.add(new JLabel("Doctor Name:"), gbc);
        gbc.gridx = 1;
        form.add(nameField, gbc);
        gbc.gridx = 2;
        form.add(new JLabel("Specialization:"), gbc);
        gbc.gridx = 3;
        form.add(specializationField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        addButton.addActionListener(this);
        form.add(addButton, gbc);

        return form;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Doctor doctor : controller.getDoctors()) {
            tableModel.addRow(new Object[]{
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != addButton) {
            return;
        }
        try {
            String name = nameField.getText();
            String specialization = specializationField.getText();
            if (!ValidationUtil.isValidDoctorInput(name, specialization)) {
                throw new IllegalArgumentException("Please fill in doctor name and specialization.");
            }
            controller.addDoctor(name, specialization);
            refreshTable();
            nameField.setText("");
            specializationField.setText("");
            JOptionPane.showMessageDialog(this, "Doctor added successfully.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Doctor Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
