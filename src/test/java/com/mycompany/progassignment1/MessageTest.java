package com.mycompany.progassignment1;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MessageTest {
    private static int totalMessages = 0;

    private static final ArrayList<String> sentMessages = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();
    private static final ArrayList<String> storedMessages = new ArrayList<>();
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIDs = new ArrayList<>();
    private static final ArrayList<String> recipients = new ArrayList<>();
    private static final ArrayList<String> senders = new ArrayList<>();

    private String messageID;
    private int messageNum;
    private String recipient;
    private String messageText;
    private String messageHash;

    public MessageTest(int messageNum, String recipient, String messageText) {
        this.messageNum = messageNum;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random random = new Random();
        return String.format("%010d", random.nextInt(1_000_000_000));
    }

    public boolean checkMessageID() {
        return this.messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        return recipient.matches("^\\+\\d{1,3}\\d{7,10}$");
    }

    public String validateMessageText() {
        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
        return "Message ready to send.";
    }

    public String createMessageHash() {
        String[] words = messageText.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return (messageID.substring(0, 2) + ":" + messageNum + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sendMessageOption(int option, String sender) {
        switch (option) {
            case 1:
                sentMessages.add(messageText);
                addToArrays("sent", sender);
                totalMessages++;
                return "Message successfully sent.";
            case 2:
                disregardedMessages.add(messageText);
                addToArrays("disregarded", sender);
                return "Message disregarded.";
            case 3:
                storeMessage();
                storedMessages.add(messageText);
                addToArrays("stored", sender);
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    private void addToArrays(String flag, String sender) {
        messageHashes.add(messageHash);
        messageIDs.add(messageID);
        recipients.add(recipient);
        senders.add(sender);
    }

    public void storeMessage() {
        JSONObject msgObj = new JSONObject();
        msgObj.put("id", messageID);
        msgObj.put("recipient", recipient);
        msgObj.put("text", messageText);
        msgObj.put("hash", messageHash);

        try {
            JSONParser parser = new JSONParser();
            JSONArray existingMessages = new JSONArray();

            try (FileReader reader = new FileReader("storedMessages.json")) {
                Object obj = parser.parse(reader);
                existingMessages = (JSONArray) obj;
            } catch (Exception ignored) {
                // File might not exist yet
            }

            existingMessages.add(msgObj);

            try (FileWriter file = new FileWriter("storedMessages.json")) {
                file.write(existingMessages.toJSONString());
                file.flush();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage());
        }
    }

    public static String printMessages() {
        StringBuilder output = new StringBuilder();
        for (String msg : sentMessages) {
            output.append(msg).append("\n");
        }
        return output.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageDetails() {
        return "Message ID: " + messageID +
               "\nMessage Hash: " + messageHash +
               "\nRecipient: " + recipient +
               "\nMessage: " + messageText;
    }

    public static String displaySendersAndRecipients() {
        StringBuilder result = new StringBuilder("Sender and Recipient List:\n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            String sender = (i < senders.size()) ? senders.get(i) : "Unknown";
            String recipient = (i < recipients.size()) ? recipients.get(i) : "Unknown";
            result.append("Sender: ").append(sender)
                  .append(" | Recipient: ").append(recipient)
                  .append("\nMessage: ").append(sentMessages.get(i)).append("\n\n");
        }
        return result.toString();
    }

    public static String displayLongestSentMessage() {
        String longest = "";
        for (String msg : sentMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return longest.isEmpty() ? "No sent messages yet." : "Longest message: " + longest;
    }

    public static String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                String recipient = (i < recipients.size()) ? recipients.get(i) : "Unknown";
                String message = (i < storedMessages.size()) ? storedMessages.get(i) : "Message not found.";
                return "Recipient: " + recipient + "\nMessage: " + message;
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String recipientToFind) {
        StringBuilder found = new StringBuilder();
        try {
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(new FileReader("storedMessages.json"));
            for (Object obj : arr) {
                JSONObject msg = (JSONObject) obj;
                if (msg.get("recipient").toString().equals(recipientToFind)) {
                    found.append("- ").append(msg.get("text")).append("\n");
                }
            }
        } catch (Exception e) {
            return "Error reading JSON: " + e.getMessage();
        }
        return found.isEmpty() ? "No messages found for recipient." : found.toString();
    }

    public static String deleteMessageByHash(String hashToDelete) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(new FileReader("storedMessages.json"));
            JSONArray updated = new JSONArray();

            for (Object obj : arr) {
                JSONObject msg = (JSONObject) obj;
                if (!msg.get("hash").toString().equals(hashToDelete)) {
                    updated.add(msg);
                }
            }

            try (FileWriter writer = new FileWriter("storedMessages.json")) {
                writer.write(updated.toJSONString());
                writer.flush();
            }

            return "Message with hash " + hashToDelete + " successfully deleted.";
        } catch (Exception e) {
            return "Error deleting message: " + e.getMessage();
        }
    }

    public static String displayMessageReport() {
        StringBuilder report = new StringBuilder("Sent Message Report:\n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            String hash = (i < messageHashes.size()) ? messageHashes.get(i) : "N/A";
            String id = (i < messageIDs.size()) ? messageIDs.get(i) : "N/A";
            String recipient = (i < recipients.size()) ? recipients.get(i) : "N/A";
            report.append("Message Hash: ").append(hash)
                  .append("\nMessage ID: ").append(id)
                  .append("\nRecipient: ").append(recipient)
                  .append("\nMessage: ").append(sentMessages.get(i)).append("\n\n");
        }
        return report.toString();
    }
}
