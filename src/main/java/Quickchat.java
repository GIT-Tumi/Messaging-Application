/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchat;

import com.mycompany.app_part1.Login;
 import com.mycompany.app_part2.Message;
 import java.util.Scanner;
/**
 *
 * @author tumi
 */
public class Quickchat {
   public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
 
        Login login = registerUser(scanner);
        if (login == null) {
            return; // registration failed too many times
        }
        if (!logInUser(scanner, login)) {
            return; // login failed
        }
 
        System.out.println("Welcome to QuickChat.");
 
        System.out.print("How many messages would you like to enter this session? ");
        int messageLimit = readInt(scanner);
        int messagesEntered = 0;
 
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
 
            switch (choice) {
                case "1":
                    if (messagesEntered >= messageLimit) {
                        System.out.println("You've reached your limit of " + messageLimit + " messages for this session.");
                        break;
                    }
                    messagesEntered += sendMessageFlow(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Please choose 1, 2, or 3.");
            }
        }
 
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        System.out.println(Message.printMessages());
        System.out.println("Goodbye!");
    }
 
    // ------------------------------------------------------------------
    // Registration / Login (Part 1)
    // ------------------------------------------------------------------
 
    private static Login registerUser(Scanner scanner) {
        for (int attempt = 0; attempt < 3; attempt++) {
            System.out.print("First name: ");
            String firstName = scanner.nextLine().trim();
            System.out.print("Last name: ");
            String lastName = scanner.nextLine().trim();
            System.out.print("Username (contains '_', 5 characters or fewer): ");
            String userName = scanner.nextLine().trim();
            System.out.print("Password (8+ chars, a capital letter, a number, a special character): ");
            String password = scanner.nextLine().trim();
            System.out.print("Cell number (e.g. +27838968976): ");
            String cellPhoneNumber = scanner.nextLine().trim();
 
            Login login = new Login(firstName, lastName, userName, password, cellPhoneNumber);
            String result = login.registerUser();
            System.out.println(result);
 
            if (login.checkUserName() && login.checkPasswordComplexity() && login.checkCellPhoneNumber()) {
                return login;
            }
            System.out.println("Let's try registering again.\n");
        }
        System.out.println("Too many failed registration attempts.");
        return null;
    }
 
    private static boolean logInUser(Scanner scanner, Login login) {
        for (int attempt = 0; attempt < 3; attempt++) {
            System.out.print("\nUsername: ");
            String userName = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
 
            if (login.loginUser(userName, password)) {
                System.out.println(login.returnLoginStatus());
                return true;
            }
            System.out.println(login.returnLoginStatus());
        }
        System.out.println("Too many failed login attempts.");
        return false;
    }
 
    // ------------------------------------------------------------------
    // Messaging (Part 2)
    // ------------------------------------------------------------------
 
    private static void printMenu() {
        System.out.println("\nPlease choose an option:");
        System.out.println("1) Send Messages");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");
        System.out.print("> ");
    }
 
    /** Runs one "compose a message" flow. Returns 1 if a message was counted against the limit. */
    private static int sendMessageFlow(Scanner scanner) {
        System.out.print("Recipient (e.g. +27838968976): ");
        String recipient = scanner.nextLine().trim();
        System.out.print("Message (max 250 characters): ");
        String text = scanner.nextLine().trim();
 
        Message message = new Message(recipient, text);
 
        String recipientResult = message.checkRecipientCell();
        System.out.println(recipientResult);
 
        String lengthResult = message.checkMessageLength();
        System.out.println(lengthResult);
 
        if (!message.isRecipientValid() || !message.isLengthValid()) {
            System.out.println("Fix the issue above before this message can be sent.");
        }
 
        System.out.println("Message hash: " + message.createMessageHash());
 
        System.out.println("\n1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("> ");
        String option = scanner.nextLine().trim();
 
        switch (option) {
            case "1":
                System.out.println(message.sendMessage());
                break;
            case "2":
                System.out.println(message.disregardMessage());
                break;
            case "3":
                System.out.println(message.storeMessage());
                break;
            default:
                System.out.println("No valid option chosen; message discarded.");
        }
        return 1;
    }
 
    private static int readInt(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a whole number: ");
            }
        }
    }  
}
