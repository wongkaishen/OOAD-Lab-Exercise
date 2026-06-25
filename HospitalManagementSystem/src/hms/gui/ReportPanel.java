package hms.gui;

import hms.controller.HMSController;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Panel for displaying basic hospital reports.
 */
public class ReportPanel extends JPanel implements ActionListener {

    private final HMSController controller;
    private final JTextArea reportArea = new JTextArea();
    private final JButton refreshButton = new JButton("Generate Report");

    public ReportPanel(HMSController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));

        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);

        refreshButton.addActionListener(this);

        JPanel top = new JPanel();
        top.add(refreshButton);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        refreshReport();
    }

    private void refreshReport() {
        reportArea.setText(controller.generateReport());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {
            refreshReport();
        }
    }
}
