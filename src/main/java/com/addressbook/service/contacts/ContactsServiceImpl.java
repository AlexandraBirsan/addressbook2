package com.addressbook.service.contacts;

import com.addressbook.dao.ContactsDao;
import com.addressbook.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by birsan on 4/12/2016.
 */
@Component("contactsService")
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private ContactsDao contactsDao;

    @Override
    @Transactional()
    public void createContact(Contact contact) {
        contactsDao.createContact(contact);
    }

    @Override
    @Transactional
    public void updateContact(Contact contact) {
        contactsDao.updateContact(contact);
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
