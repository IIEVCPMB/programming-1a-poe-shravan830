package com.mycompany.progassignment1;

import com.mycompany.progassignment1.Message;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    Message message;

    @Before
    public void setup() {
        message = new Message(1, "+27838968976", "Hi Mike, can you join us for dinner tonight");
    }

    @Test
    public void testCheckMessageLengthSuccess() {
        String result = message.validateMessageText();
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testCheckMessageLengthFailure() {
        String longMsg = "a".repeat(251);
        Message msg = new Message(2, "+27838968976", longMsg);
        String result = msg.validateMessageText();
        assertTrue(result.contains("Message exceeds 250 characters by"));
    }

    @Test
    public void testCheckRecipientCellSuccess() {
        assertTrue(message.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCellFailure() {
        Message msg = new Message(3, "087654321", "Test message");
        assertFalse(msg.checkRecipientCell());
    }

    @Test
    public void testCreateMessageHashFormat() {
        String hash = message.createMessageHash();
        assertTrue(hash.contains(":1:"));
        assertEquals(hash, hash.toUpperCase());
    }

    @Test
    public void testCheckMessageIDLength() {
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testSendMessageOptionSend() {
        String res = message.sendMessageOption(1);
        assertEquals("Message successfully sent.", res);
    }

    @Test
    public void testSendMessageOptionDisregard() {
        String res = message.sendMessageOption(2);
        assertEquals("Press 0 to delete message.", res);
    }

    @Test
    public void testSendMessageOptionStore() {
        String res = message.sendMessageOption(3);
        assertEquals("Message successfully stored.", res);
    }
}
