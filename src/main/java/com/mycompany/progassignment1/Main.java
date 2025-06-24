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

        // Menu after login
        while (true) {
            String choice = JOptionPane.showInputDialog("""
                    Choose an option:
                    1) Send a message
                    2) View all sent messages
                    3) View sender and recipient list
                    4) View longest message
                    5) Search message by ID
                    6) Search messages by recipient
                    7) Delete stored message by hash
                    8) View sent message report
                    9) Quit
                    """);

            if (choice == null) return;

            switch (choice) {
                case "1" -> {
                    String recipient = JOptionPane.showInputDialog("Enter recipient phone number (with +countrycode):");
                    if (recipient == null) continue;

                    String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
                    if (messageText == null) continue;

                    Message message = new Message(1, recipient, messageText);

                    if (!message.checkRecipientCell()) {
                        JOptionPane.showMessageDialog(null, "Invalid phone number format.");
                        continue;
                    }

                    String validation = message.validateMessageText();
                    if (!validation.equals("Message ready to send.")) {
                        JOptionPane.showMessageDialog(null, validation);
                        continue;
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
                            String result = message.sendMessageOption(option, firstName); // sender = firstName
                            JOptionPane.showMessageDialog(null, result);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid input.");
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Total messages sent: " + Message.returnTotalMessages());
                }

                case "2" -> JOptionPane.showMessageDialog(null, Message.printMessages());

                case "3" -> JOptionPane.showMessageDialog(null, Message.displaySendersAndRecipients());

                case "4" -> JOptionPane.showMessageDialog(null, Message.displayLongestSentMessage());

                case "5" -> {
                    String id = JOptionPane.showInputDialog("Enter message ID to search:");
                    if (id != null) {
                        JOptionPane.showMessageDialog(null, Message.searchByMessageID(id));
                    }
                }

                case "6" -> {
                    String rec = JOptionPane.showInputDialog("Enter recipient number to search:");
                    if (rec != null) {
                        JOptionPane.showMessageDialog(null, Message.searchByRecipient(rec));
                    }
                }

                case "7" -> {
                    String hash = JOptionPane.showInputDialog("Enter hash to delete:");
                    if (hash != null) {
                        JOptionPane.showMessageDialog(null, Message.deleteMessageByHash(hash));
                    }
                }

                case "8" -> JOptionPane.showMessageDialog(null, Message.displayMessageReport());

                case "9" -> {
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    System.exit(0);
                }

                default -> JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }
}
