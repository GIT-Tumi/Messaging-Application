/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package com.mycompany.app_part2;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author tumi
 */
 
public class MessageTest {
     
    @Before
    public void resetState() {
        // Each test should start from a clean slate (totalMessagesSent = 0, etc.)
        Message.resetAll();
    }
 
    // ------------------------------------------------------------------
    // Message length (section 9, "The message should not be more than 250 characters")
    // ------------------------------------------------------------------
 
    @Test
    public void testMessageLength_Success() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", message.checkMessageLength());
    }
 
    @Test
    public void testMessageLength_Failure() {
        String longText = "x".repeat(260); // 10 characters over the 250 limit
        Message message = new Message("+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", message.checkMessageLength());
    }
 
    // ------------------------------------------------------------------
    // Recipient cell number (section 9, "The recipient number is correctly formatted")
    // ------------------------------------------------------------------
 
    @Test
    public void testRecipientCell_Success() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }
 
    @Test
    public void testRecipientCell_Failure() {
        Message message = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", message.checkRecipientCell());
    }
 
    // ------------------------------------------------------------------
    // Message hash (section 9, "Message hash is correct")
    // ------------------------------------------------------------------
 
    @Test
    public void testMessageHash_TestCase1() {
        // The brief expects "00:0:HITONIGHT" for Test Case 1's data.
        // Message IDs are randomly generated, so this test only pins
        // down the "0:HITONIGHT" part that doesn't depend on the
        // random ID; your implementation's ID prefix will vary.
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String hash = message.createMessageHash();
        assertTrue(hash.endsWith(":0:HITONIGHT"));
    }
 
    // ------------------------------------------------------------------
    // Message ID (section 9, "Message ID is created")
    // ------------------------------------------------------------------
 
    @Test
    public void testMessageID_IsTenCharactersOrFewer() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(message.checkMessageID());
        assertTrue(message.getMessageID().length() <= 10);
    }
 
    // ------------------------------------------------------------------
    // MessageSent options
    // ------------------------------------------------------------------
 
    @Test
    public void testMessageSent_Send() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.sendMessage());
    }
 
    @Test
    public void testMessageSent_Disregard() {
        Message message = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", message.disregardMessage());
    }
 
    @Test
    public void testMessageSent_Store() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", message.storeMessage());
    }
 
    // ------------------------------------------------------------------
    // Total messages sent
    // ------------------------------------------------------------------
 
    @Test
    public void testReturnTotalMessages() {
        Message first = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        first.sendMessage();
        Message second = new Message("+27718693002", "Hi Mike, are we still on for tomorrow?");
        second.sendMessage();
        assertEquals(2, Message.returnTotalMessages());
    }
// ------------------------------------------------------------------
    // Part 3: Sent Messages array correctly populated
    //
    // Test data: message 1 ("Did you get the cake?", Sent) and
    // message 4 ("It is dinner time!", Sent) from the brief's five
    // test messages. Messages 2, 3 and 5 are Stored/Disregard, so they
    // should NOT appear in the sent-messages array.
    // ------------------------------------------------------------------

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        new Message("+27834557896", "Did you get the cake?").sendMessage();
        new Message("0838884567", "It is dinner time!").sendMessage();

        List<String> sent = Message.getSentMessagesArray();
        assertEquals(2, sent.size());
        assertEquals("Did you get the cake?", sent.get(0));
        assertEquals("It is dinner time!", sent.get(1));
    }

    // ------------------------------------------------------------------
    // Part 3: Display the longest Message
    //
    // The brief's unit-test table runs this check directly against
    // test messages 1-4 regardless of their original Sent/Stored/
    // Disregard flag, so this test loads all four into the stored
    // array (via storeMessage()) purely to exercise the search.
    // ------------------------------------------------------------------

    @Test
    public void testDisplayLongestMessage() {
        new Message("+27834557896", "Did you get the cake?").storeMessage();
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.").storeMessage();
        new Message("+27834484567", "Yohoooo, I am at your gate.").storeMessage();
        new Message("0838884567", "It is dinner time!").storeMessage();

        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.displayLongestStoredMessage());
    }

    // ------------------------------------------------------------------
    // Part 3: Search for messageID
    //
    // Message IDs are normally random, so this test uses the
    // test-support constructor to pin the ID to the brief's test
    // value and confirms the search finds it.
    // ------------------------------------------------------------------

    @Test
    public void testSearchForMessageID() {
        new Message("0838884567", "It is dinner time!", "0838884567").storeMessage();

        assertEquals("Recipient: 0838884567, Message: It is dinner time!",
                Message.searchByMessageID("0838884567"));
    }

    // ------------------------------------------------------------------
    // Part 3: Search all the messages sent or stored regarding a
    // particular recipient
    // ------------------------------------------------------------------

    @Test
    public void testSearchByRecipient() {
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.").storeMessage();
        new Message("+27838884567", "Ok, I am leaving without you.").storeMessage();

        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    // ------------------------------------------------------------------
    // Part 3: Delete a message using a message hash
    // ------------------------------------------------------------------

    @Test
    public void testDeleteByHash() {
        Message message = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message.storeMessage();
        String hash = message.getMessageHash();

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                Message.deleteByHash(hash));
    }

    // ------------------------------------------------------------------
    // Part 3: Display Report
    // ------------------------------------------------------------------

    @Test
    public void testDisplayStoredMessagesReport() {
        Message message = new Message("+27834557896", "Did you get the cake?");
        message.storeMessage();

        String report = Message.displayStoredMessagesReport();
        assertTrue(report.contains("Message ID: " + message.getMessageID()));
        assertTrue(report.contains("Message Hash: " + message.getMessageHash()));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
    }

    // ------------------------------------------------------------------
    // Part 3: reading stored messages back from JSON
    // ------------------------------------------------------------------

    @Test
    public void testLoadStoredMessagesFromJson() {
        new Message("+27834557896", "Did you get the cake?").storeMessage();
        List<String> before = Message.getStoredRecipients();

        Message.loadStoredMessagesFromJson();
        List<String> after = Message.getStoredRecipients();

        assertEquals(before.size(), after.size());
        assertTrue(after.contains("+27834557896"));
    }    
}
