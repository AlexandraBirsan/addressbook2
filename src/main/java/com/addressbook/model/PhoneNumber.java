package com.addressbook.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by birsan on 4/11/2016.
 */
@Entity
@Table(name = "ADDRESSBOOK_PHONENUMBERS")
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "phonenumbers_sequence")
    @Column(name = "AP_ID")
    private Integer phoneId;
    @Column(name = "AP_CONTACT_ID")
    private Integer contactId;
    @Column(name = "AP_NUMBER")
    private String number;

    public long getContactId() {
        return contactId;
    }

    public PhoneNumber setContactId(Integer contactId) {
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