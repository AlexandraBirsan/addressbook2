package com.addressbook.dao;

import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * Created by birsan on 4/26/2016.
 */
@Component("contactsDao")
@Transactional
public class ContactsDaoImpl implements ContactsDao {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Properties queriesProperties;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void createContact(Contact contact) {
        Session session = sessionFactory.getCurrentSession();
        session.save(contact);
        session.flush();
        createPhoneNumbers(contact, session);
    }

    @Override
    public void updateContact(Contact contact) {
        Session session = sessionFactory.getCurrentSession();
        Contact contactDB = getContact(contact.getId());
        buildContactUpdate(contactDB, contact);
        session.update(contactDB);
        session.flush();
        Criteria criteria = session.createCriteria(PhoneNumber.class).add(Restrictions.eq("contactId", contact.getId()));
        List<PhoneNumber> phoneNumbers = criteria.list();
        for (PhoneNumber number : phoneNumbers) {
            session.delete(number);
        }
        createPhoneNumbers(contact, session);
    }

    private void createPhoneNumbers(Contact contact, Session session) {
        Integer contactId = contact.getId();
        for (int i = 0; i < contact.getPhoneNumbers().size(); i++) {
            PhoneNumber number = contact.getPhoneNumbers().get(i);
            number.setContactId(contactId);
            session.save(number);
            if (i % 50 == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    private void buildContactUpdate(Contact contactNew, Contact contactOld) {
        contactNew.setFirstName(contactOld.getFirstName());
        contactNew.setLastName(contactOld.getLastName());
        contactNew.setCompany(contactOld.getCompany());
        contactNew.setContentType(contactOld.getContentType());
        contactNew.setPhoto(contactOld.getPhoto());
        contactNew.setPhoneNumbers(contactOld.getPhoneNumbers());
    }

    @Override
    public Contact getContact(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Contact.class).add(Restrictions.eq("id", id));
        Contact contact = (Contact) criteria.list().get(0);
        Criteria criteriaContactPhone = session.createCriteria(PhoneNumber.class).add(Restrictions.eq("contactId", id));
        List<PhoneNumber> phoneNumbers = criteriaContactPhone.list();
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }

    @Override
    public void deleteContact(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(getContact(id));
    }

    @Override
    public List<Contact> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = criteria.list();
        for (Contact contact : contacts) {
            Criteria criteriaContactPhone = session.createCriteria(PhoneNumber.class).add(Restrictions.eq("contactId", contact.getId()));
            List<PhoneNumber> phoneNumbers = criteriaContactPhone.list();
            contact.setPhoneNumbers(phoneNumbers);
        }
        return contacts;
    }

}
