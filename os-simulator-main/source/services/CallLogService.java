package services;

import com.google.gson.reflect.TypeToken;
import models.CallLogEntry;
import storage.DataHandler;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogService {
    private static final String LOGS_FILE = "data/call_logs.json";
    private static List<CallLogEntry> callLogs;

    // A simple date formatter for the timestamp
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, dd MMM");

    // Load logs from the file when the service is first used
    static {
        loadLogs();
    }

    private static void loadLogs() {
        Type type = new TypeToken<ArrayList<CallLogEntry>>() {}.getType();
        callLogs = DataHandler.loadData(LOGS_FILE, type);
        if (callLogs == null) {
            callLogs = new ArrayList<>();
        }
    }

    private static void saveLogs() {
        DataHandler.saveData(LOGS_FILE, callLogs);
    }

    /**
     * Returns the list of all call logs.
     */
    public static List<CallLogEntry> getLogs() {
        return callLogs;
    }

    /**
     * Adds a new log entry and saves it to the file.
     * @param phoneNumber The number that was called.
     * @param callType The type of call (e.g., "Outgoing").
     */
    public static void addLog(String phoneNumber, String callType) {
        String currentTime = dateFormat.format(new Date());
        CallLogEntry newEntry = new CallLogEntry(phoneNumber, callType, currentTime);

        // Add to the top of the list so recent calls appear first
        callLogs.add(0, newEntry);

        saveLogs();
    }
}