package com.addressbook.service.contacts;

import com.addressbook.dao.ContactsDao;
import com.addressbook.dao.PhoneNumberDao;
import com.addressbook.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by birsan on 4/12/2016.
 */
@Component("contactService")
public class ContactsServiceImpl implements ContactsService {

    @Autowired private ContactsDao contactsDao;
    @Autowired private PhoneNumberDao phoneNumberDao;

    @Override
    public void createContact(Contact contact) {
        Long id = contactsDao.createContact(contact);
        phoneNumberDao.createPhoneNumbers(id, contact.getPhoneNumbers());
    }

    @Override
    public void updateContact(Contact contact) {
        contactsDao.updateContact(contact);
        phoneNumberDao.updatePhoneNumber(contact);
    }

    @Override
    public Contact getContact(Integer id) {
        return contactsDao.getContact(id);
    }

    @Override
    public void deleteContact(Integer id) {
        contactsDao.deleteContact(id);
    }

    @Override
    public List<Contact> getAll() {
        return contactsDao.getAll();
    }
}
