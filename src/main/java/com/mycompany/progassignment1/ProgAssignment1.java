package com.mycompany.progassignment1;

import javax.swing.*;

public class ProgAssignment1 {

    static Login loginSystem;
    static boolean loggedIn = false;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");

        while (true) {
            String authInput = JOptionPane.showInputDialog("""
                Please select an option:
                1) Register
                2) Login
                3) Quit
                """);
            if (authInput == null) break;

            switch (authInput) {
                case "1" -> handleRegistration();
                case "2" -> {
                    if (handleLogin()) {
                        loggedIn = true;
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        showMainMenu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login failed. Please try again.");
                    }
                }
                case "3" -> {
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    System.exit(0);
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Please try again.");
            }
        }
    }

    private static void handleRegistration() {
        String fname = JOptionPane.showInputDialog("Enter your first name:");
        if (fname == null) return;

        String lname = JOptionPane.showInputDialog("Enter your last name:");
        if (lname == null) return;

        loginSystem = new Login(fname, lname);

        String username = JOptionPane.showInputDialog("Create username:");
        if (username == null) return;

        String password = JOptionPane.showInputDialog("Create password:");
        if (password == null) return;

        String phone = JOptionPane.showInputDialog("Enter phone number (with +countrycode):");
        if (phone == null) return;

        String regMsg = loginSystem.registerUser(username, password, phone);
        JOptionPane.showMessageDialog(null, regMsg);
    }

    private static boolean handleLogin() {
        if (loginSystem == null) {
            JOptionPane.showMessageDialog(null, "No registered user found. Please register first.");
            return false;
        }

        String username = JOptionPane.showInputDialog("Enter username:");
        if (username == null) return false;

        String password = JOptionPane.showInputDialog("Enter password:");
        if (password == null) return false;

        boolean success = loginSystem.loginUser(username, password);
        JOptionPane.showMessageDialog(null, loginSystem.returnLoginStatus(success));
        return success;
    }

    private static void showMainMenu() {
        while (true) {
            String input = JOptionPane.showInputDialog("""
                Please select an option:
                1) Send Messages
                2) Show recently sent messages
                3) Quit
                """);

            if (input == null) break;

            switch (input) {
                case "1" -> sendMessagesFlow();
                case "2" -> {
                    String sentMsgs = Message.printMessages();
                    if (sentMsgs.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No messages sent yet.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Recently sent messages:\n" + sentMsgs);
                    }
                }
                case "3" -> {
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    System.exit(0);
                }
                default -> JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
            }
        }
    }

    private static void sendMessagesFlow() {
        String numInput = JOptionPane.showInputDialog("How many messages do you want to send?");
        if (numInput == null) return;

        int numMessages;
        try {
            numMessages = Integer.parseInt(numInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number entered.");
            return;
        }

        for (int i = 1; i <= numMessages; i++) {
            String recipient = JOptionPane.showInputDialog("Enter recipient phone number (with +countrycode):");
            if (recipient == null) return;

            String messageText = JOptionPane.showInputDialog("Enter your message (max 250 chars):");
            if (messageText == null) return;

            Message message = new Message(i, recipient, messageText);

            if (!message.checkRecipientCell()) {
                JOptionPane.showMessageDialog(null, "Cell phone number is incorrectly formatted or does not contain international code. Please correct the number and try again.");
                i--; // redo this message
                continue;
            }

            String messageValidation = message.validateMessageText();
            if (!messageValidation.equals("Message ready to send.")) {
                JOptionPane.showMessageDialog(null, messageValidation);
                i--; // redo this message
                continue;
            }

            JOptionPane.showMessageDialog(null, message.getMessageDetails());

            while (true) {
                String optionInput = JOptionPane.showInputDialog("""
                    Choose an option:
                    1) Send Message
                    2) Disregard Message
                    3) Store Message to send later
                    4) Exit message menu
                    """);
                if (optionInput == null) return;

                int option;
                try {
                    option = Integer.parseInt(optionInput);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid option, please enter 1, 2, 3, or 4.");
                    continue;
                }

                if (option == 4) {
                    JOptionPane.showMessageDialog(null, "Exiting message menu.");
                    return; // Exit sending messages and return to main menu
                }

                if (option >= 1 && option <= 3) {
                    String result = message.sendMessageOption(option);
                    JOptionPane.showMessageDialog(null, result);
                    break; // Exit option input loop and continue with next message
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid option, please enter 1, 2, 3, or 4.");
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Total messages sent: " + Message.returnTotalMessages());
    }
}
