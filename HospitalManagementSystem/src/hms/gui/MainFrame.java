package hms.gui;

import hms.controller.HMSController;
import hms.model.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

/**
 * Main dashboard with role-based module access.
 */
public class MainFrame extends JFrame implements ActionListener {

    private final HMSController controller;
    private final JButton logoutButton = new JButton("Logout");

    public MainFrame(HMSController controller) {
        this.controller = controller;
        User user = controller.getCurrentUser();

        setTitle("Hospital Management System - " + user.getRole().getDisplayName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        if (user.canManagePatients()) {
            tabs.addTab("Patients", new PatientPanel(controller));
        }
        if (user.canManageDoctors()) {
            tabs.addTab("Doctors", new DoctorPanel(controller));
        }
        if (user.canManageAppointments()) {
            tabs.addTab("Appointments", new AppointmentPanel(controller));
        }
        if (user.canViewReports()) {
            tabs.addTab("Reports", new ReportPanel(controller));
        }

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBorder(new EmptyBorder(8, 12, 0, 12));
        topBar.add(new JLabel("Logged in as: " + user.getUsername()
                + " (" + user.getRole().getDisplayName() + ")"));
        topBar.add(logoutButton);
        logoutButton.addActionListener(this);

        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            controller.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
