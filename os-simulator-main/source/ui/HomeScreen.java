package ui;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeScreen extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public HomeScreen() {
        setTitle("OS Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(400, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel homeMenu = createHomeMenu();
        ContactsManager contactsManager = new ContactsManager(cardLayout, mainPanel);
        DialerPanel dialerPanel = new DialerPanel(cardLayout, mainPanel);
        MessagesPanel messagesPanel = new MessagesPanel(cardLayout, mainPanel);
        CallLogsPanel callLogsPanel = new CallLogsPanel(cardLayout, mainPanel);
        SettingsPanel settingsPanel = new SettingsPanel(cardLayout, mainPanel);
        GalleryPanel galleryPanel = new GalleryPanel(cardLayout, mainPanel);

        mainPanel.add(homeMenu, "Home");
        mainPanel.add(contactsManager, "Contacts");
        mainPanel.add(dialerPanel, "Dialer");
        mainPanel.add(messagesPanel, "Messages");
        mainPanel.add(callLogsPanel, "Call Logs");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(galleryPanel, "Gallery");

        add(mainPanel);
        cardLayout.show(mainPanel, "Home");

        setFocusable(true);
    }

    private JPanel createHomeMenu() {
        JPanel panel = new JPanel(new BorderLayout());

        // Top bar for time and date
        JPanel topBar = new JPanel(new BorderLayout());
        JLabel timeLabel = new JLabel();
        JLabel batteryLabel = new JLabel("🔋 85%");
        JLabel networkLabel = new JLabel("📶 Wi-Fi");

        Font topBarFont = new Font("Arial", Font.BOLD, 14);
        timeLabel.setFont(topBarFont);
        timeLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        batteryLabel.setFont(topBarFont);
        batteryLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        networkLabel.setFont(topBarFont);
        networkLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel leftStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftStatus.add(timeLabel);

        JPanel rightStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightStatus.add(networkLabel);
        rightStatus.add(batteryLabel);

        topBar.add(leftStatus, BorderLayout.WEST);
        topBar.add(rightStatus, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeLabel.setText(timeFormat.format(new Date()));
        });
        timer.start();

        // Center button panel with emojis only
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create buttons with emojis only
        JButton dialerBtn = createEmojiButton("📞");
        JButton contactsBtn = createEmojiButton("👤");
        JButton messagesBtn = createEmojiButton("💬");
        JButton callLogsBtn = createEmojiButton("📚");
        JButton settingsBtn = createEmojiButton("\u2699"); // ⚙
        JButton galleryBtn = createEmojiButton("🖼️");

        // Add buttons to the panel in desired order
        buttonPanel.add(dialerBtn);
        buttonPanel.add(contactsBtn);
        buttonPanel.add(messagesBtn);
        buttonPanel.add(callLogsBtn);
        buttonPanel.add(settingsBtn);
        buttonPanel.add(galleryBtn);

        // Add action listeners
        dialerBtn.addActionListener(e -> cardLayout.show(mainPanel, "Dialer"));
        contactsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Contacts"));
        messagesBtn.addActionListener(e -> cardLayout.show(mainPanel, "Messages"));
        callLogsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Call Logs"));
        settingsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Settings"));
        galleryBtn.addActionListener(e -> cardLayout.show(mainPanel, "Gallery"));

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Navigation bar at the bottom
        JPanel navBar = new JPanel(new GridLayout(1, 3, 5, 5));
        navBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JButton backNav = new JButton("◀");
        JButton homeNav = new JButton("⏺");
        JButton recentNav = new JButton("◼");

        Font navFont = new Font("Segoe UI Emoji", Font.PLAIN, 20);
        backNav.setFont(navFont);
        homeNav.setFont(navFont);
        recentNav.setFont(navFont);

        navBar.add(backNav);
        navBar.add(homeNav);
        navBar.add(recentNav);

        panel.add(navBar, BorderLayout.SOUTH);

        return panel;
    }

    // Helper method to create a button with an emoji only
    private JButton createEmojiButton(String emoji) {
        JButton button = new JButton();

        // Use a JLabel to display the emoji, making it large and centered
        JLabel emojiLabel = new JLabel(emoji, SwingConstants.CENTER);
        // Adjusted font size to fit well without text below it
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));

        button.setLayout(new BorderLayout()); // Use BorderLayout to put JLabel in CENTER
        button.add(emojiLabel, BorderLayout.CENTER);

        // Optional: Remove button borders and fill for a cleaner, icon-like look
        // button.setOpaque(false);
        // button.setContentAreaFilled(false);
        // button.setBorderPainted(false);
        // button.setFocusPainted(false);

        return button;
    }

    public void display() {
        setVisible(true);
    }
}