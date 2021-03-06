package com.addressbook.dao;

import com.addressbook.model.Contact;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by birsan on 4/26/2016.
 */

public interface ContactsDao {
    void createContact(Contact contact);
    void updateContact(Contact contact);
    Contact getContact(Integer id);
    void deleteContact(Integer id);
    List<Contact> getAll();
}
