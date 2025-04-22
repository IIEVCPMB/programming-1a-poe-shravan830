package com.mycompany.progassignment1;

import java.util.Scanner;

public class ProgAssignment1 {

    static Scanner scanner = new Scanner(System.in);
    static Login loginSystem;

    public static void main(String[] args) {
        int choice;

        System.out.println("==== Welcome to my ChatApp ====");

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> handleRegistration();
                case 2 -> handleLogin();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    static void handleRegistration() {
        System.out.print("Enter your first name: ");
        String fname = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lname = scanner.nextLine();

        loginSystem = new Login(fname, lname);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter phone number (with +countrycode): ");
        String phone = scanner.nextLine();

        String response = loginSystem.registerUser(username, password, phone);
        System.out.println(response);
    }

    static void handleLogin() {
        if (loginSystem == null) {
            System.out.println("Please register first.");
            return;
        }

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = loginSystem.loginUser(username, password);
        System.out.println(loginSystem.returnLoginStatus(success));
    }
}
