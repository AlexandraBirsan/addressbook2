package com.addressbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by birsan on 4/11/2016.
 */
@Entity
@Table(name = "ADDRESSBOOK_PHONENUMBERS")
public class PhoneNumber {
    @Column(name = "AP_CONTACT_ID")
    private long contactId;
    @Column(name = "AP_NUMBER")
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