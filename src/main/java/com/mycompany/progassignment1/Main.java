package com.mycompany.progassignment1;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat (Basic Demo)");

        // Registration
        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        if (firstName == null) return;

        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        if (lastName == null) return;

        Login login = new Login(firstName, lastName);

        String username = JOptionPane.showInputDialog("Create a username:");
        if (username == null) return;

        String password = JOptionPane.showInputDialog("Create a password:");
        if (password == null) return;

        String phone = JOptionPane.showInputDialog("Enter your phone number (with +countrycode):");
        if (phone == null) return;

        String regResult = login.registerUser(username, password, phone);
        JOptionPane.showMessageDialog(null, regResult);

        if (!regResult.equals("User registered successfully.")) return;

        // Login
        String loginUsername = JOptionPane.showInputDialog("Login - Enter your username:");
        if (loginUsername == null) return;

        String loginPassword = JOptionPane.showInputDialog("Login - Enter your password:");
        if (loginPassword == null) return;

        boolean success = login.loginUser(loginUsername, loginPassword);
        JOptionPane.showMessageDialog(null, login.returnLoginStatus(success));

        if (!success) return;

        // Optional: Send one message
        int sendNow = JOptionPane.showConfirmDialog(null, "Do you want to send a message now?", "Send Message", JOptionPane.YES_NO_OPTION);
        if (sendNow == JOptionPane.YES_OPTION) {
            String recipient = JOptionPane.showInputDialog("Enter recipient phone number (with +countrycode):");
            if (recipient == null) return;

            String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
            if (messageText == null) return;

            Message message = new Message(1, recipient, messageText);

            if (!message.checkRecipientCell()) {
                JOptionPane.showMessageDialog(null, "Invalid phone number format.");
                return;
            }

            String validation = message.validateMessageText();
            if (!validation.equals("Message ready to send.")) {
                JOptionPane.showMessageDialog(null, validation);
                return;
            }

            JOptionPane.showMessageDialog(null, message.getMessageDetails());

            String optionInput = JOptionPane.showInputDialog("""
                Choose an option:
                1) Send Message
                2) Disregard Message
                3) Store Message to send later
                """);

            if (optionInput != null) {
                try {
                    int option = Integer.parseInt(optionInput);
                    String result = message.sendMessageOption(option);
                    JOptionPane.showMessageDialog(null, result);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input.");
                }
            }

            JOptionPane.showMessageDialog(null, "Total messages sent: " + Message.returnTotalMessages());
        }
    }
}
