package ui;

import models.Contact;
import services.CallLogService;
import storage.DataHandler;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DialerPanel extends JPanel {
    private JTextField numberDisplay;
    private JTextField searchBar;
    private JList<String> searchResultsList;
    private DefaultListModel<String> searchResultsModel;
    private List<Contact> contacts;
    private final String CONTACTS_FILE = "data/contacts.json";

    public DialerPanel(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());

        // ---- Top bar with Back button and Number Display (Stays at the top) ----
        JPanel topBar = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        topBar.add(backBtn, BorderLayout.WEST);

        numberDisplay = new JTextField();
        numberDisplay.setEditable(false);
        numberDisplay.setFont(new Font("Arial", Font.BOLD, 36)); // Larger font
        numberDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(numberDisplay, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);

        // ---- Action buttons (Stays at the bottom) ----
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JButton callBtn = new JButton("Call");
        callBtn.setFont(new Font("Arial", Font.BOLD, 20));
        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("Arial", Font.BOLD, 20));

        callBtn.addActionListener(e -> {
            String number = numberDisplay.getText();
            if (!number.isEmpty()) {
                CallLogService.addLog(number, "Outgoing");
                CallingScreen callScreen = new CallingScreen(cardLayout, mainPanel, number);
                mainPanel.add(callScreen, "Calling");
                cardLayout.show(mainPanel, "Calling");
            } else {
                JOptionPane.showMessageDialog(this, "Enter a number first!");
            }
        });

        clearBtn.addActionListener(e -> numberDisplay.setText(""));

        actionPanel.add(callBtn);
        actionPanel.add(clearBtn);
        add(actionPanel, BorderLayout.SOUTH);

        // ---- Create Panels for the Center Area ----
        // 1. Search Panel (for the top half)
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchBar = new JTextField("Search contacts...");
        searchResultsModel = new DefaultListModel<>();
        searchResultsList = new JList<>(searchResultsModel);

        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchContacts(searchBar.getText());
            }
        });

        searchResultsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = searchResultsList.getSelectedValue();
                    if (selected != null) {
                        String phone = selected.split(" - ")[1];
                        numberDisplay.setText(phone);
                    }
                }
            }
        });

        searchPanel.add(searchBar, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(searchResultsList), BorderLayout.CENTER);

        // 2. Number Keypad Panel (for the bottom half)
        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        String[] buttons = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"};

        for (String label : buttons) {
            JButton btn = new JButton(label);
            btn.setFont(new Font("Arial", Font.BOLD, 28)); // Larger font
            btn.addActionListener(e -> numberDisplay.setText(numberDisplay.getText() + label));
            buttonPanel.add(btn);
        }

        // ---- NEW: Combine Search and Keypad into a single vertical panel ----
        JPanel centerContentPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // 2 rows, 1 column
        centerContentPanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
        centerContentPanel.add(searchPanel);    // Top half
        centerContentPanel.add(buttonPanel);    // Bottom half

        // Add the combined panel to the main layout
        add(centerContentPanel, BorderLayout.CENTER);

        // Load contacts
        loadContacts();
    }

    private void loadContacts() {
        Type contactListType = new TypeToken<ArrayList<Contact>>() {}.getType();
        contacts = DataHandler.loadData(CONTACTS_FILE, contactListType);
        // Added this check to prevent crashes if the file doesn't exist yet
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
    }

    private void searchContacts(String query) {
        searchResultsModel.clear();
        if (query.isEmpty() || query.equals("Search contacts...")) return;

        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(query.toLowerCase()) || c.getPhoneNumber().contains(query)) {
                searchResultsModel.addElement(c.getName() + " - " + c.getPhoneNumber());
            }
        }
    }
}