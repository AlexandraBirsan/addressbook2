package com.addressbook.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birsan on 5/12/2016.
 */
public class ValidationException extends RuntimeException {
    private List<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
