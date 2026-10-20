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
}
