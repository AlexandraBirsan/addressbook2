package com.addressbook.model;

import java.util.List;

/**
 * Created by birsan on 4/11/2016.
 */
public class Contact {
    private Integer id;
    private String firstName;
    private String lastName;
    private String company;
    private String contentType;
    private byte[] photo;
    private List<PhoneNumber> phoneNumbers;

    public String getContentType() {
        return contentType;
    }

    public Contact setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Contact setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Contact setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Contact setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public Contact setCompany(String company) {
        this.company = company;
        return this;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Contact setPhoto(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public Contact setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }
}
