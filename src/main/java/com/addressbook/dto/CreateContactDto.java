package com.addressbook.dto;

import com.addressbook.model.PhoneNumber;

import java.util.List;

/**
 * Created by gifei on 6/11/2016.
 */
public class CreateContactDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String company;
    private String contentType;
    private String photo;
    private List<PhoneNumber> phoneNumbers;

    public Integer getId() {
        return id;
    }

    public CreateContactDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public CreateContactDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CreateContactDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public CreateContactDto setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public CreateContactDto setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public CreateContactDto setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public CreateContactDto setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }
}
