package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsPanel extends JPanel {

    public SettingsPanel(CardLayout mainCardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());

        // ---- Top bar with Back button ----
        JPanel topBar = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> mainCardLayout.show(mainPanel, "Home"));
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Settings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topBar.add(title, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);

        // ---- Main Content using a Split Pane ----
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(150);

        // --- 1. Left Navigation List ---
        DefaultListModel<String> settingsListModel = new DefaultListModel<>();
        settingsListModel.addElement("Network & Internet");
        settingsListModel.addElement("Personalization");
        settingsListModel.addElement("Accounts");
        settingsListModel.addElement("Time & Language");
        settingsListModel.addElement("Screen Time");
        settingsListModel.addElement("Privacy & Security");
        settingsListModel.addElement("About");

        JList<String> settingsList = new JList<>(settingsListModel);
        settingsList.setFont(new Font("Arial", Font.BOLD, 14));
        settingsList.setSelectedIndex(0);
        splitPane.setLeftComponent(new JScrollPane(settingsList));

        // --- 2. Right Content Panels with CardLayout ---
        CardLayout contentCardLayout = new CardLayout();
        JPanel rightContentPanel = new JPanel(contentCardLayout);

        rightContentPanel.add(createNetworkPanel(), "Network & Internet");
        rightContentPanel.add(createPersonalizationPanel(), "Personalization");
        rightContentPanel.add(createAccountsPanel(), "Accounts");
        rightContentPanel.add(createTimeLanguagePanel(), "Time & Language");
        rightContentPanel.add(createScreenTimePanel(), "Screen Time");
        rightContentPanel.add(createSecurityPanel(), "Privacy & Security");
        rightContentPanel.add(createAboutPanel(), "About");

        splitPane.setRightComponent(rightContentPanel);

        // --- Navigation Logic ---
        settingsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = settingsList.getSelectedValue();
                contentCardLayout.show(rightContentPanel, selected);
            }
        });

        add(splitPane, BorderLayout.CENTER);
    }

    // --- Panel Creation Methods for Each Setting ---

    private void applyTheme(String themeName) {
        try {
            if ("Dark".equals(themeName)) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                UIManager.put("Panel.background", Color.BLACK);
            } else { // "Light"
                UIManager.setLookAndFeel(new FlatLightLaf());
                // ---> THIS IS THE FIX <---
                // Reset the background property to use the theme's default white/grey
                UIManager.put("Panel.background", null);
            }
            // Update the UI of the entire application
            SwingUtilities.updateComponentTreeUI(this.getTopLevelAncestor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createNetworkPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Network & Internet");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel wifiPanel = new JPanel(new BorderLayout());
        wifiPanel.add(new JLabel("Wi-Fi"), BorderLayout.WEST);
        JToggleButton wifiToggle = new JToggleButton("ON");
        wifiToggle.setSelected(true);
        wifiPanel.add(wifiToggle, BorderLayout.EAST);
        panel.add(wifiPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Connected: Home_WiFi_5G"));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        return panel;
    }

    private JPanel createScreenTimePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Screen Time");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(new JLabel("Daily Average: 4h 10m"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel todayLabel = new JLabel("Today: 3h 45m");
        panel.add(todayLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JProgressBar usageBar = new JProgressBar(0, 100);
        usageBar.setValue(75);
        usageBar.setStringPainted(true);
        panel.add(usageBar);
        return panel;
    }

    private JPanel createPersonalizationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Theme:"));
        String[] themes = {"Dark", "Light"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);

        themeComboBox.setSelectedItem(UIManager.getLookAndFeel() instanceof FlatDarkLaf ? "Dark" : "Light");

        themeComboBox.addActionListener(e -> applyTheme((String) themeComboBox.getSelectedItem()));

        panel.add(themeComboBox);
        return panel;
    }

    private JPanel createAccountsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Account Details");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel userLabel = new JLabel("User: dhishaj123@outlook.com");
        JLabel statusLabel = new JLabel("Status: Signed In");
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(userLabel);
        panel.add(statusLabel);
        return panel;
    }

    private JPanel createTimeLanguagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Date & Time");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel timeLabel = new JLabel("Loading...");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy   hh:mm:ss a");
            timeLabel.setText("Current time: " + sdf.format(new Date()));
        });
        timer.start();
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(timeLabel);
        return panel;
    }

    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Privacy & Security");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        JCheckBox passcodeCheck = new JCheckBox("Enable Lock Screen Passcode");
        passcodeCheck.setSelected(true);
        JCheckBox twoFactorCheck = new JCheckBox("Two-Factor Authentication (Enabled)");
        twoFactorCheck.setSelected(true);
        twoFactorCheck.setEnabled(false);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(passcodeCheck);
        panel.add(twoFactorCheck);
        return panel;
    }

    private JScrollPane createAboutPanel() {
        JTextArea aboutText = new JTextArea();
        aboutText.setText(
                "🧭 About OS Simulator\n" +
                        "OS Simulator is an interactive desktop-based application that replicates the essential features of a modern smartphone operating system within a controlled simulation environment. Developed using Java Swing, this project demonstrates the integration of graphical user interface (GUI) programming, event-driven logic, and object-oriented principles in a single cohesive system. The simulator provides an intuitive and educational platform where users can experience core smartphone functionalities such as calling, messaging, contact management, and call logs — all without the need for mobile SDKs, emulators, or dedicated hardware.\n\n" +
                        "The system architecture is designed around key Object-Oriented Programming (OOP) concepts — encapsulation, inheritance, polymorphism, and abstraction. Each functional module, including the Home Screen, Dialer, Contacts Manager, Messaging Interface, and Call Logs, is implemented as an independent class hierarchy that extends a shared framework for smooth navigation and efficient data handling. The project employs JSON-based storage for maintaining persistent records of contacts, messages, and call histories, ensuring a lightweight, portable, and easily extendable data management approach.\n\n" +
                        "Beyond functionality, the OS Simulator emphasizes educational value by serving as a demonstrative model for students and developers to understand the internal structure of operating systems and user interface workflows. It bridges the gap between theoretical learning and hands-on application design, allowing users to visualize how different modules within an operating system interact seamlessly through well-structured code and modular design. The use of Java Swing ensures platform independence, providing a consistent experience across all systems that support the Java Runtime Environment.\n\n" +
                        "This project is a result of collaborative development by a team of Computer Science and Engineering students from Model Engineering College, Thrikkakara, Ernakulam — Dhishaj C, Don Zacc T P, Muhammed Rizwan, Rohan John, Stephno Robinson, and Viswas Govind — under the guidance of Ms. Athira, Assistant Professor, Department of Computer Science and Engineering. The OS Simulator stands as a representation of applied academic learning, technical creativity, and teamwork, reflecting the team’s commitment to innovation and practical software engineering."
        );
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setEditable(false);
        aboutText.setFocusable(false);
        aboutText.setOpaque(false);
        aboutText.setBorder(new EmptyBorder(10, 20, 10, 20));
        aboutText.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setBorder(null);
        return scrollPane;
    }
}