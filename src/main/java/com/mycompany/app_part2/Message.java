/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_part2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author tumi
 */
public class Message {
    // Keeps track of every message that has actually been sent while
    private static final List<Message> sentMessages = new ArrayList<>();
 
    // Messages the user chose to "store to send later".
    private static final List<Message> storedMessages = new ArrayList<>();
 
    // Total number of messages sent so far. This also becomes a new
    // message's "message number" before it is sent.
    private static int totalMessagesSent = 0;
 
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private String messageHash;
    
    public Message(String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageNumber = totalMessagesSent;
    }
 
    /** Generates a random ten-digit message ID, e.g. "0012345678". */
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }
    
     /** A message ID is valid if it is no more than ten characters long. */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
 
    /**
     * A recipient cell number is correctly formatted if it starts with
     * the South African international code "+27" followed by no more
     * than ten digits (same rule as Login.checkCellPhoneNumber() in
     * Part 1).
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27[0-9]{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }
 
    /** A message must not be more than 250 characters long. */
    public String checkMessageLength() {
        if (messageText != null && messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = messageText.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }
 
    public boolean isRecipientValid() {
        return recipient != null && recipient.matches("^\\+27[0-9]{1,10}$");
    }
 
    public boolean isLengthValid() {
        return messageText != null && messageText.length() <= 250;
    }
 
    // ------------------------------------------------------------------
    // Message hash
    // ------------------------------------------------------------------
 
    /**
     * Creates the message hash: the first two digits of the message
     * ID, a colon, the message number, a colon, then the first and
     * last words of the message (punctuation stripped), all in caps.
     * e.g. messageID "00..." , messageNumber 0, "Hi Mike, ... tonight?"
     * -> "00:0:HITONIGHT"
     */
    public String createMessageHash() {
        String idPrefix = messageID.substring(0, Math.min(2, messageID.length()));
        String[] words = messageText.trim().split("\\s+");
        String firstWord = stripToLetters(words[0]);
        String lastWord = stripToLetters(words[words.length - 1]);
        messageHash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return messageHash;
    }
 
    private String stripToLetters(String word) {
        return word.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }
 
    // ------------------------------------------------------------------
    // Send / Disregard / Store
    // ------------------------------------------------------------------
 
    /** Call once the user chooses "Send Message". */
    public String sendMessage() {
        if (messageHash == null) {
            createMessageHash();
        }
        totalMessagesSent++;
        sentMessages.add(this);
        return "Message successfully sent.";
    }
 
    /** Call once the user chooses "Disregard Message". */
    public String disregardMessage() {
        return "Press 0 to delete the message.";
    }
 
    /** Call once the user chooses "Store Message to send later". */
    public String storeMessage() {
        if (messageHash == null) {
            createMessageHash();
        }
        storedMessages.add(this);
        appendToJsonFile();
        return "Message successfully stored.";
    }
 
    /**
     * Appends this message to messages.json as a simple JSON array.
     * Hand-rolled (no external JSON library) so the project needs no
     * extra NetBeans library setup beyond JUnit.
     */
    private void appendToJsonFile() {
        List<Message> all = new ArrayList<>(storedMessages);
        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write("[\n");
            for (int i = 0; i < all.size(); i++) {
                Message m = all.get(i);
                writer.write("  {\n");
                writer.write("    \"messageID\": \"" + escape(m.messageID) + "\",\n");
                writer.write("    \"messageNumber\": " + m.messageNumber + ",\n");
                writer.write("    \"recipient\": \"" + escape(m.recipient) + "\",\n");
                writer.write("    \"message\": \"" + escape(m.messageText) + "\",\n");
                writer.write("    \"messageHash\": \"" + escape(m.messageHash) + "\"\n");
                writer.write(i == all.size() - 1 ? "  }\n" : "  },\n");
            }
            writer.write("]\n");
        } catch (IOException e) {
            System.out.println("Could not write messages.json: " + e.getMessage());
        }
    }
 
    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
 
    // ------------------------------------------------------------------
    // Reporting
    // ------------------------------------------------------------------
 
    /** Returns the full details of every message sent so far. */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipient).append("\n");
            sb.append("Message: ").append(m.messageText).append("\n\n");
        }
        return sb.toString();
    }
 
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
 
    /** Resets all static state. Handy between JUnit tests / app runs. */
    public static void resetAll() {
        sentMessages.clear();
        storedMessages.clear();
        totalMessagesSent = 0;
    }
 
    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
 
    public String getMessageID() {
        return messageID;
    }
 
    public int getMessageNumber() {
        return messageNumber;
    }
 
    public String getRecipient() {
        return recipient;
    }
 
    public String getMessageText() {
        return messageText;
    }
 
    public String getMessageHash() {
        return messageHash;
    }
}
