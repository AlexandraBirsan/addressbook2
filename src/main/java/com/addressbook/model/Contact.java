package com.addressbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by birsan on 4/11/2016.
 */
@Entity
@Table(name = "ADDRESSBOOK_CONTACTS")
public class Contact{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "CONTACTS_SEQUENCE")
    @Column(name = "AC_CONTACT_ID")
    private Integer id;
    @Column(name = "AC_FIRST_NAME")
    private String firstName;
    @Column(name = "AC_LAST_NAME")
    private String lastName;
    @Column(name = "AC_COMPANY")
    private String company;
    @Column(name = "AC_CONTENT_TYPE")
    private String contentType;
    @Column(name = "AC_PHOTO")
    private byte[] photo;
    @Transient
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
