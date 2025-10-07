package ui;

import models.Contact;
import storage.DataHandler;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactsManager extends JPanel {
    private DefaultListModel<String> contactListModel;
    private JList<String> contactList;
    private List<Contact> contacts;
    private final String CONTACTS_FILE = "data/contacts.json";

    public ContactsManager(CardLayout cardLayout, JPanel mainPanel) { // ✅ accept navigation context
        setLayout(new BorderLayout());

        // ---- Top bar with Back button ----
        JPanel topBar = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Contacts", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topBar.add(title, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);

        // ---- Contacts list ----
        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);
        add(new JScrollPane(contactList), BorderLayout.CENTER);

        // ---- Bottom buttons ----
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Contact");
        JButton deleteBtn = new JButton("Delete Contact");

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // ---- Load saved data ----
        loadData();
        refreshContactList();

        // ---- Event handlers ----
        addBtn.addActionListener(e -> addContact());
        deleteBtn.addActionListener(e -> deleteContact());
    }

    private void loadData() {
        Type contactListType = new TypeToken<ArrayList<Contact>>() {}.getType();
        contacts = DataHandler.loadData(CONTACTS_FILE, contactListType);

        if (contacts == null) {
            contacts = new ArrayList<>();
        }
    }

    private void saveData() {
        DataHandler.saveData(CONTACTS_FILE, contacts);
    }

    private void refreshContactList() {
        contactListModel.clear();
        for (Contact c : contacts) {
            contactListModel.addElement(c.getName() + " - " + c.getPhoneNumber());
        }
    }

    private void addContact() {
        String name = JOptionPane.showInputDialog(this, "Enter Name:");
        String phone = JOptionPane.showInputDialog(this, "Enter Phone Number:");
        if (name != null && phone != null && !name.isBlank() && !phone.isBlank()) {
            contacts.add(new Contact(name.trim(), phone.trim()));
            saveData();
            refreshContactList();
        }
    }

    private void deleteContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex != -1) {
            contacts.remove(selectedIndex);
            saveData();
            refreshContactList();
        }
    }
}
