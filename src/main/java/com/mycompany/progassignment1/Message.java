package com.mycompany.progassignment1;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Message {
    private static int totalMessages = 0;
    private static final ArrayList<String> sentMessages = new ArrayList<>();
    private static final JSONArray storedMessages = new JSONArray();

    private String messageID;
    private int messageNum;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(int messageNum, String recipient, String messageText) {
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
        String lastWord = words.length > 1 ? words[words.length - 1] : "";
        return (messageID.substring(0, 2) + ":" + messageNum + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sendMessageOption(int option) {
        switch (option) {
            case 1:
                sentMessages.add(messageText);
                totalMessages++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    public void storeMessage() {
        JSONObject msgObj = new JSONObject();
        msgObj.put("id", messageID);
        msgObj.put("recipient", recipient);
        msgObj.put("text", messageText);
        msgObj.put("hash", messageHash);

        storedMessages.add(msgObj);

        try (FileWriter file = new FileWriter("storedMessages.json")) {
            file.write(storedMessages.toJSONString());
            file.flush();
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
}
