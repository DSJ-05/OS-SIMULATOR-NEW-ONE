package ui;

import javax.swing.*;
import java.awt.*;

public class LockScreen extends JFrame {
    private final String PIN = "1234";
    private StringBuilder enteredPin;
    private JPasswordField pinField;

    public LockScreen() {
        setTitle("OS Simulator - Lock Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(true);   // fullscreen feel
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setOpacity(1f);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        enteredPin = new StringBuilder();

        JLabel titleLabel = new JLabel("Enter PIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        pinField = new JPasswordField();
        pinField.setEditable(false);
        pinField.setHorizontalAlignment(SwingConstants.CENTER);
        pinField.setFont(new Font("Arial", Font.BOLD, 28));
        pinField.setBackground(Color.BLACK);
        pinField.setForeground(Color.WHITE);
        pinField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(pinField, BorderLayout.CENTER);

        JPanel keypad = new JPanel(new GridLayout(4, 3, 15, 15));
        keypad.setBackground(Color.BLACK);
        String[] buttons = {
                "1", "2", "3",
                "4", "5", "6",
                "7", "8", "9",
                "C", "0", "OK"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            styleButton(btn);
            btn.addActionListener(e -> handleButton(text));
            keypad.add(btn);
        }

        JPanel keypadWrapper = new JPanel(new BorderLayout());
        keypadWrapper.setBackground(Color.BLACK);
        keypadWrapper.add(keypad, BorderLayout.CENTER);
        add(keypadWrapper, BorderLayout.SOUTH);

        // ✅ F11 toggle
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_F11) {
                    toggleFullscreen();
                }
            }
        });
        setFocusable(true);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(45, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
    }

    private void handleButton(String text) {
        switch (text) {
            case "C": enteredPin.setLength(0); pinField.setText(""); break;
            case "OK": checkPin(); break;
            default: enteredPin.append(text);
                pinField.setText("*".repeat(enteredPin.length()));
        }
    }

    private void checkPin() {
        if (enteredPin.toString().equals(PIN)) {
            dispose();
            new HomeScreen().display();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect PIN", "Error", JOptionPane.ERROR_MESSAGE);
            enteredPin.setLength(0);
            pinField.setText("");
        }
    }

    private void toggleFullscreen() {
        if (isUndecorated()) {
            dispose();
            setUndecorated(false);
            setSize(400, 500);
            setLocationRelativeTo(null);
            setVisible(true);
        } else {
            dispose();
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
    }

    public void display() { setVisible(true); }
}
