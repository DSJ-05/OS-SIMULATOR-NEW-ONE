package ui;

import models.CallLogEntry;
import services.CallLogService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class CallLogsPanel extends JPanel {
    private DefaultListModel<CallLogEntry> logModel;
    private JList<CallLogEntry> logList;

    public CallLogsPanel(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());

        // ---- Top bar with Back button ----
        JPanel topBar = new JPanel(new BorderLayout());
        JButton backBtn = new JButton("← Back");
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Call Logs", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topBar.add(title, BorderLayout.CENTER);
        add(topBar, BorderLayout.NORTH);

        // ---- Logs list that gets data from the service ----
        logModel = new DefaultListModel<>();
        logList = new JList<>(logModel);
        logList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(logList), BorderLayout.CENTER);

        // Add a listener to refresh the list every time this panel is shown.
        // This is the magic that makes it update automatically!
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshLogList();
            }
        });
    }

    /**
     * Clears the list and re-populates it with the latest data from CallLogService.
     */
    private void refreshLogList() {
        logModel.clear();
        List<CallLogEntry> logs = CallLogService.getLogs();
        for (CallLogEntry entry : logs) {
            logModel.addElement(entry);
        }
    }
}