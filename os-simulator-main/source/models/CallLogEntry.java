package models;

public class CallLogEntry {
    private String phoneNumber;
    private String callType; // e.g., "Outgoing", "Incoming", "Missed"
    private String timestamp;

    public CallLogEntry(String phoneNumber, String callType, String timestamp) {
        this.phoneNumber = phoneNumber;
        this.callType = callType;
        this.timestamp = timestamp;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCallType() {
        return callType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // This toString() method will control how the entry is displayed in the JList
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", callType, phoneNumber, timestamp);
    }
}