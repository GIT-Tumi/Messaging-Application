/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
 package com.mycompany.app_part2;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertTrue;
 import org.junit.Before;
 import org.junit.Test;
/**
 *
 * @author tumi
 */
 
public class Messagetest {
     
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
}
