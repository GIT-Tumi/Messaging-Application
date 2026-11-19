/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author tumi
 */
public class Message {
       private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private String messageHash;

    // Total number of messages sent so far. Also used as a new
    // message's "message number" before it is sent.
    private static int totalMessagesSent = 0;

    // ------------------------------------------------------------------
    // Part 3 arrays (ArrayLists are used so the arrays can grow as the
    // user enters messages at runtime - nothing below is hard-coded).
    // ------------------------------------------------------------------

    // "Sent Message" array: contains all the messages sent.
    private static final List<String> sentMessagesArray = new ArrayList<>();

    // "Disregarded Messages" array: contains all the messages that were disregarded.
    private static final List<String> disregardedMessagesArray = new ArrayList<>();

    // "Message Hash" array: contains all the message hashes (every message
    // that is sent, stored, or disregarded gets a hash).
    private static final List<String> messageHashArray = new ArrayList<>();

    // "Message ID" array: contains all the message IDs.
    private static final List<String> messageIDArray = new ArrayList<>();

    // "Stored Messages": kept as four parallel arrays (same index = same
    // message) so that the sender/recipient/message/hash/ID of a stored
    // message can all be looked up together.
    private static final List<String> storedRecipients = new ArrayList<>();
    private static final List<String> storedMessageTexts = new ArrayList<>();
    private static final List<String> storedHashes = new ArrayList<>();
    private static final List<String> storedIDs = new ArrayList<>();

    // The logged-in user's name, used as the "sender" for feature (a).
    // Set once, from QuickChat, right after a successful login.
    private static String senderName = "Unknown Sender";

    private static final String JSON_FILE_NAME = "messages.json";

    // ------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------

    public Message(String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageNumber = totalMessagesSent;
    }

    /**
     * Test-support constructor: lets a unit test supply a fixed message
     * ID instead of a random one, so that tests which search for a
     * specific ID (see the Part 3 brief's "Search for messageID" test)
     * are deterministic.
     */
    public Message(String recipient, String messageText, String fixedMessageID) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = fixedMessageID;
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

    // ------------------------------------------------------------------
    // Validation methods (Part 2)
    // ------------------------------------------------------------------

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
    // Message hash (Part 2)
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
    // Send / Disregard / Store (Part 2, extended in Part 3 to populate arrays)
    // ------------------------------------------------------------------

    /** Call once the user chooses "Send Message". */
    public String sendMessage() {
        if (messageHash == null) {
            createMessageHash();
        }
        totalMessagesSent++;
        sentMessagesArray.add(messageText);
        messageHashArray.add(messageHash);
        messageIDArray.add(messageID);
        return "Message successfully sent.";
    }

    /** Call once the user chooses "Disregard Message". */
    public String disregardMessage() {
        if (messageHash == null) {
            createMessageHash();
        }
        disregardedMessagesArray.add(messageText);
        messageHashArray.add(messageHash);
        messageIDArray.add(messageID);
        return "Press 0 to delete the message.";
    }

    /** Call once the user chooses "Store Message to send later". */
    public String storeMessage() {
        if (messageHash == null) {
            createMessageHash();
        }
        storedRecipients.add(recipient);
        storedMessageTexts.add(messageText);
        storedHashes.add(messageHash);
        storedIDs.add(messageID);
        messageHashArray.add(messageHash);
        messageIDArray.add(messageID);
        appendToJsonFile();
        return "Message successfully stored.";
    }

    /**
     * Appends this message to messages.json as a simple JSON array.
     * Hand-rolled (no external JSON library) so the project needs no
     * extra NetBeans library setup beyond JUnit.
     */
    private void appendToJsonFile() {
        try (FileWriter writer = new FileWriter(JSON_FILE_NAME)) {
            writer.write("[\n");
            for (int i = 0; i < storedIDs.size(); i++) {
                writer.write("  {\n");
                writer.write("    \"messageID\": \"" + escape(storedIDs.get(i)) + "\",\n");
                writer.write("    \"recipient\": \"" + escape(storedRecipients.get(i)) + "\",\n");
                writer.write("    \"message\": \"" + escape(storedMessageTexts.get(i)) + "\",\n");
                writer.write("    \"messageHash\": \"" + escape(storedHashes.get(i)) + "\"\n");
                writer.write(i == storedIDs.size() - 1 ? "  }\n" : "  },\n");
            }
            writer.write("]\n");
        } catch (IOException e) {
            System.out.println("Could not write " + JSON_FILE_NAME + ": " + e.getMessage());
        }
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // ------------------------------------------------------------------
    // Part 3: read stored messages back from the JSON file
    // ------------------------------------------------------------------

    /**
     * Reads messages.json back into the stored-message arrays. This is a
     * small hand-rolled parser tailored to the exact format written by
     * storeMessage() above, so the project needs no extra library. If
     * your module wants a general-purpose library instead (e.g. org.json
     * or Gson, as the brief's "research online" hint suggests), add the
     * dependency and swap the body of this method for a few lines that
     * call it - the rest of the class doesn't need to change.
     */
    public static String loadStoredMessagesFromJson() {
        java.io.File file = new java.io.File(JSON_FILE_NAME);
        if (!file.exists()) {
            return "No " + JSON_FILE_NAME + " file found yet; nothing to load.";
        }

        storedRecipients.clear();
        storedMessageTexts.clear();
        storedHashes.clear();
        storedIDs.clear();

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
        } catch (IOException e) {
            return "Could not read " + JSON_FILE_NAME + ": " + e.getMessage();
        }

        // Matches each "field": "value" pair inside the file, allowing
        // for escaped quotes/backslashes inside the value.
        Pattern fieldPattern = Pattern.compile("\"(messageID|recipient|message|messageHash)\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = fieldPattern.matcher(content);

        String id = null, recipient = null, text = null, hash = null;
        while (matcher.find()) {
            String field = matcher.group(1);
            String value = unescape(matcher.group(2));
            switch (field) {
                case "messageID": id = value; break;
                case "recipient": recipient = value; break;
                case "message": text = value; break;
                case "messageHash": hash = value; break;
            }
            if (id != null && recipient != null && text != null && hash != null) {
                storedIDs.add(id);
                storedRecipients.add(recipient);
                storedMessageTexts.add(text);
                storedHashes.add(hash);
                id = null; recipient = null; text = null; hash = null;
            }
        }
        return storedIDs.size() + " stored message(s) loaded from " + JSON_FILE_NAME + ".";
    }

    private static String unescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // ------------------------------------------------------------------
    // Part 3: Stored Messages menu features (a-f)
    // ------------------------------------------------------------------

    /** (a) Display the sender and recipient of all stored messages. */
    public static String displayStoredSenderAndRecipient() {
        if (storedRecipients.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (String r : storedRecipients) {
            sb.append("Sender: ").append(senderName).append(", Recipient: ").append(r).append("\n");
        }
        return sb.toString().trim();
    }

    /** (b) Display the longest stored message. */
    public static String displayLongestStoredMessage() {
        if (storedMessageTexts.isEmpty()) {
            return "There are no stored messages.";
        }
        String longest = storedMessageTexts.get(0);
        for (String text : storedMessageTexts) {
            if (text.length() > longest.length()) {
                longest = text;
            }
        }
        return longest;
    }

    /** (c) Search for a message ID and display the corresponding recipient and message. */
    public static String searchByMessageID(String id) {
        int index = storedIDs.indexOf(id);
        if (index == -1) {
            return "No stored message found with ID " + id + ".";
        }
        return "Recipient: " + storedRecipients.get(index) + ", Message: " + storedMessageTexts.get(index);
    }

    /** (d) Search for all messages stored for a particular recipient. */
    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedRecipients.size(); i++) {
            if (storedRecipients.get(i).equals(recipient)) {
                sb.append("\"").append(storedMessageTexts.get(i)).append("\" ");
            }
        }
        if (sb.length() == 0) {
            return "No stored messages found for recipient " + recipient + ".";
        }
        return sb.toString().trim();
    }

    /** (e) Delete a message using the message hash. */
    public static String deleteByHash(String hash) {
        int index = storedHashes.indexOf(hash);
        if (index == -1) {
            return "No stored message found with hash " + hash + ".";
        }
        String deletedText = storedMessageTexts.get(index);
        storedRecipients.remove(index);
        storedMessageTexts.remove(index);
        storedHashes.remove(index);
        storedIDs.remove(index);
        return "Message: \"" + deletedText + "\" successfully deleted.";
    }

    /** (f) Display a report that lists the full details of all the stored messages. */
    public static String displayStoredMessagesReport() {
        if (storedIDs.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedIDs.size(); i++) {
            sb.append("Message ID: ").append(storedIDs.get(i)).append("\n");
            sb.append("Message Hash: ").append(storedHashes.get(i)).append("\n");
            sb.append("Recipient: ").append(storedRecipients.get(i)).append("\n");
            sb.append("Message: ").append(storedMessageTexts.get(i)).append("\n\n");
        }
        return sb.toString().trim();
    }

    // ------------------------------------------------------------------
    // Reporting (Part 2) and array access (Part 3)
    // ------------------------------------------------------------------

    /** Returns the full details of every message sent so far. */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessagesArray.size(); i++) {
            sb.append("Message: ").append(sentMessagesArray.get(i)).append("\n\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    public static void setSenderName(String name) {
        senderName = name;
    }

    public static List<String> getSentMessagesArray() {
        return new ArrayList<>(sentMessagesArray);
    }

    public static List<String> getDisregardedMessagesArray() {
        return new ArrayList<>(disregardedMessagesArray);
    }

    public static List<String> getMessageHashArray() {
        return new ArrayList<>(messageHashArray);
    }

    public static List<String> getMessageIDArray() {
        return new ArrayList<>(messageIDArray);
    }

    public static List<String> getStoredRecipients() {
        return new ArrayList<>(storedRecipients);
    }

    public static List<String> getStoredMessageTexts() {
        return new ArrayList<>(storedMessageTexts);
    }

    /** Resets all static state. Handy between JUnit tests / app runs. */
    public static void resetAll() {
        sentMessagesArray.clear();
        disregardedMessagesArray.clear();
        messageHashArray.clear();
        messageIDArray.clear();
        storedRecipients.clear();
        storedMessageTexts.clear();
        storedHashes.clear();
        storedIDs.clear();
        totalMessagesSent = 0;
        senderName = "Unknown Sender";
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
