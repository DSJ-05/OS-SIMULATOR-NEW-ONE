package ui;

import javax.swing.*;
import java.awt.*;

public class CallingScreen extends JPanel {
    private JLabel statusLabel;

    public CallingScreen(CardLayout cardLayout, JPanel mainPanel, String contactOrNumber) {
        setLayout(new BorderLayout());

        // ---- Top bar with Back (End Call) button ----
        JPanel topBar = new JPanel(new BorderLayout());
        JButton endCallBtn = new JButton("End Call");
        endCallBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dialer"));
        topBar.add(endCallBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Ongoing Call", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        topBar.add(title, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);

        // ---- Call Info ----
        JLabel contactLabel = new JLabel("Calling: " + contactOrNumber, SwingConstants.CENTER);
        contactLabel.setFont(new Font("Arial", Font.BOLD, 26));
        add(contactLabel, BorderLayout.CENTER);

        // ---- Status (Connecting... / In Call) ----
        statusLabel = new JLabel("Connecting...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        add(statusLabel, BorderLayout.SOUTH);

        // Simulate connection after 2 seconds
        new Timer(2000, e -> statusLabel.setText("In Call...")).start();
    }
}
