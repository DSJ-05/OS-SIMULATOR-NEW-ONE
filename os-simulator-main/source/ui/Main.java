package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {

        // --- Step 1: Set the Dark Theme (must be done first) ---
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            // Set the default background for all panels to pure black
            UIManager.put("Panel.background", Color.BLACK);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // --------------------------------------------------------

        // --- Step 2: Start the UI on the correct thread ---
        SwingUtilities.invokeLater(() -> {
            // Starts your application with the LockScreen
            new LockScreen().display();
        });
    }
}