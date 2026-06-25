package hms.gui;

import hms.controller.HMSController;
import hms.model.User;
import hms.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Login window for role-based authentication.
 */
public class LoginFrame extends JFrame implements ActionListener {

    private final HMSController controller = new HMSController();
    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JButton loginButton = new JButton("Login");

    public LoginFrame() {
        setTitle("Hospital Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        buildLayout();
        loginButton.addActionListener(this);
    }

    private void buildLayout() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(20, 24, 20, 24));
        root.setBackground(Color.WHITE);

        JLabel title = new JLabel("Hospital Management System");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        form.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        form.add(loginButton, gbc);

        root.add(form, BorderLayout.CENTER);

        JLabel hint = new JLabel(
                "<html>Demo accounts: admin/admin123, doctor/doctor123, receptionist/recep123</html>");
        hint.setForeground(new Color(90, 90, 90));
        root.add(hint, BorderLayout.SOUTH);

        add(root);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() != loginButton) {
            return;
        }

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (!ValidationUtil.isValidLogin(username, password)) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = controller.login(username, password).orElse(null);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            return;
        }

        dispose();
        new MainFrame(controller).setVisible(true);
    }
}
