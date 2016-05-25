package com.addressbook.model;

/**
 * Created by birsan on 4/11/2016.
 */
public class PhoneNumber {
    private long contactId;
    private String number;

    public long getContactId() {
        return contactId;
    }

    public PhoneNumber setContactId(long contactId) {
        this.contactId = contactId;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}