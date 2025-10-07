package ui;

import javax.swing.*;
import java.awt.*;

public class GalleryPanel extends JPanel {

    public GalleryPanel(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());

        // ---- Top bar with Back button ----
        JPanel topBar = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Gallery", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topBar.add(title, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);

        // ---- Placeholder message for the center ----
        JLabel emptyLabel = new JLabel("Your gallery is empty.", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        add(emptyLabel, BorderLayout.CENTER);
    }
}