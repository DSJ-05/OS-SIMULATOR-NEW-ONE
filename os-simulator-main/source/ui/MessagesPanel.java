package ui;

import models.Contact; // Assuming you have a Contact model, if not, this can be removed.
import storage.DataHandler;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesPanel extends JPanel {

    // Main data structure: A map where the key is the phone number (String)
    // and the value is a list of messages (List<String>).
    private Map<String, List<String>> conversations;

    private final String MESSAGES_FILE = "data/messages.json";

    // UI Components for switching between views
    private CardLayout internalCardLayout;
    private JPanel cardsPanel;

    // Components for the conversations list view
    private DefaultListModel<String> conversationListModel;
    private JList<String> conversationList;

    // Components for the individual message view
    private DefaultListModel<String> messageListModel;
    private JList<String> messageList;
    private JLabel messageViewTitle;
    private String currentOpenConversationNumber; // To track which conversation is open

    public MessagesPanel(CardLayout mainCardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        conversations = new HashMap<>();

        // ---- Main Panel that holds the different views ----
        internalCardLayout = new CardLayout();
        cardsPanel = new JPanel(internalCardLayout);

        // ---- 1. Create the Conversation List Panel (Master View) ----
        JPanel conversationPanel = createConversationsPanel(mainCardLayout, mainPanel);
        cardsPanel.add(conversationPanel, "CONVERSATIONS");

        // ---- 2. Create the Message Detail Panel (Detail View) ----
        JPanel messageDetailPanel = createMessageDetailPanel();
        cardsPanel.add(messageDetailPanel, "MESSAGES");

        add(cardsPanel, BorderLayout.CENTER);

        // ---- Load saved data ----
        loadMessages();
        refreshConversationList();

        // Show the list of conversations by default
        internalCardLayout.show(cardsPanel, "CONVERSATIONS");
    }

    /**
     * Creates the panel that shows the list of phone numbers (conversations).
     */
    private JPanel createConversationsPanel(CardLayout mainCardLayout, JPanel mainPanel) {
        JPanel panel = new JPanel(new BorderLayout());

        // --- Top Bar with Back and Title ---
        JPanel topBar = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> mainCardLayout.show(mainPanel, "Home"));
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Messages", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topBar.add(title, BorderLayout.CENTER);
        panel.add(topBar, BorderLayout.NORTH);

        // --- List of Conversations ---
        conversationListModel = new DefaultListModel<>();
        conversationList = new JList<>(conversationListModel);
        conversationList.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(new JScrollPane(conversationList), BorderLayout.CENTER);

        // --- Bottom Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton newMessageBtn = new JButton("New Message");
        JButton deleteConversationBtn = new JButton("Delete Conversation");
        buttonPanel.add(newMessageBtn);
        buttonPanel.add(deleteConversationBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Event Handlers ---

        // Double-click a conversation to open its messages
        conversationList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = conversationList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        String selectedNumber = conversationListModel.getElementAt(index);
                        openConversation(selectedNumber);
                    }
                }
            }
        });

        // "New Message" button action
        newMessageBtn.addActionListener(e -> createNewMessage());

        // "Delete Conversation" button action
        deleteConversationBtn.addActionListener(e -> deleteConversation());

        return panel;
    }

    /**
     * Creates the panel that shows messages for a single conversation.
     */
    private JPanel createMessageDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // --- Top Bar with Back and Number Title ---
        JPanel topBar = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← All Messages");
        backBtn.addActionListener(e -> internalCardLayout.show(cardsPanel, "CONVERSATIONS"));
        topBar.add(backBtn, BorderLayout.WEST);

        messageViewTitle = new JLabel("", SwingConstants.CENTER);
        messageViewTitle.setFont(new Font("Arial", Font.BOLD, 20));
        topBar.add(messageViewTitle, BorderLayout.CENTER);
        panel.add(topBar, BorderLayout.NORTH);

        // --- List of Messages ---
        messageListModel = new DefaultListModel<>();
        messageList = new JList<>(messageListModel);
        panel.add(new JScrollPane(messageList), BorderLayout.CENTER);

        // --- Bottom Input and Button Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        JButton sendBtn = new JButton("Send");
        JButton deleteMessageBtn = new JButton("Delete Message");

        JPanel buttonSubPanel = new JPanel();
        buttonSubPanel.add(sendBtn);
        buttonSubPanel.add(deleteMessageBtn);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(buttonSubPanel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // --- Event Handlers ---

        // "Send" button action
        sendBtn.addActionListener(e -> {
            String messageText = messageField.getText();
            if (currentOpenConversationNumber != null && !messageText.isBlank()) {
                conversations.get(currentOpenConversationNumber).add(messageText);
                saveMessages();
                refreshMessageList();
                messageField.setText("");
            }
        });

        // "Delete Message" button action
        deleteMessageBtn.addActionListener(e -> {
            int selectedIndex = messageList.getSelectedIndex();
            if (currentOpenConversationNumber != null && selectedIndex != -1) {
                conversations.get(currentOpenConversationNumber).remove(selectedIndex);
                saveMessages();
                refreshMessageList();
            }
        });

        return panel;
    }

    // --- Data and UI Logic Methods ---

    private void loadMessages() {
        Type type = new TypeToken<HashMap<String, List<String>>>() {}.getType();
        // Use DataHandler similar to how Contacts are loaded
        conversations = DataHandler.loadData(MESSAGES_FILE, type);
        // If the file was empty or didn't exist, loadData should return an empty map
        if (conversations == null) {
            conversations = new HashMap<>();
        }
    }

    private void saveMessages() {
        DataHandler.saveData(MESSAGES_FILE, conversations);
    }

    private void refreshConversationList() {
        conversationListModel.clear();
        for (String phoneNumber : conversations.keySet()) {
            conversationListModel.addElement(phoneNumber);
        }
    }

    private void refreshMessageList() {
        messageListModel.clear();
        if (currentOpenConversationNumber != null && conversations.containsKey(currentOpenConversationNumber)) {
            for (String msg : conversations.get(currentOpenConversationNumber)) {
                messageListModel.addElement(msg);
            }
        }
    }

    private void openConversation(String phoneNumber) {
        currentOpenConversationNumber = phoneNumber;
        messageViewTitle.setText(phoneNumber);
        refreshMessageList();
        internalCardLayout.show(cardsPanel, "MESSAGES");
    }

    private void createNewMessage() {
        String number = JOptionPane.showInputDialog(this, "Enter phone number:");
        if (number != null && !number.isBlank()) {
            String message = JOptionPane.showInputDialog(this, "Enter message:");
            if (message != null && !message.isBlank()) {
                // If conversation doesn't exist, create a new list for it
                conversations.putIfAbsent(number.trim(), new ArrayList<>());
                // Add the new message
                conversations.get(number.trim()).add(message.trim());
                saveMessages();
                refreshConversationList();
                // Optionally, open the new conversation right away
                openConversation(number.trim());
            }
        }
    }

    private void deleteConversation() {
        int selectedIndex = conversationList.getSelectedIndex();
        if (selectedIndex != -1) {
            String numberToDelete = conversationListModel.getElementAt(selectedIndex);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the entire conversation with " + numberToDelete + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                conversations.remove(numberToDelete);
                saveMessages();
                refreshConversationList();
            }
        }
    }
}