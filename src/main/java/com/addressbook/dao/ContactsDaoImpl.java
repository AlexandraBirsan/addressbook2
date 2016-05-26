package com.addressbook.dao;

import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import oracle.jdbc.OracleTypes;
import org.apache.commons.dbutils.DbUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by birsan on 4/26/2016.
 */
@Component("contactsDao")
@Transactional
public class ContactsDaoImpl implements ContactsDao {

    private static final int FIRST_NAME_INDEX = 2;
    private static final int LAST_NAME_INDEX = 3;
    private static final int COMPANY_INDEX = 4;
    private static final int CONTENT_TYPE_INDEX = 5;
    private static final int PHOTO_INDEX = 6;
    private static final int PHONE_CURSOR_INDEX = 7;
    private static final String UPDATE_CONTACT = "updateContact";
    private static final String CREATE_CONTACT = "createContact";
    private static final String GET_CONTACT = "getContact";
    private static final String DELETE = "delete";
    private static final String GET_ALL_CONTACTS = "getAllContacts";
    private static final String NEXT_VAL = "nextVal";
    private static final String JDBC_ERROR_CREATE = "JDBC error!Could not create the contact";
    private static final String JDBC_ERROR_UPDATE = "JDBC error!Could not update the contact.";
    private static final String JDBC_ERROR_GET = "JDBC error!Could not get the contact.";
    private static final String JDBC_ERROR_DELETE = "JDBC error!Could not delete the contact.";
    private static final String JDBC_ERROR_GET_ALL = "JDBC error! Could not get all the contacts.";
    @Autowired
    private DataSource dataSource;
    @Autowired
    private Properties queriesProperties;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Long createContact(Contact contact) {
        Session session=sessionFactory.getCurrentSession();
        session.save(contact);
        session.flush();
        Integer contactId = contact.getId();
        for(int i=0;i<contact.getPhoneNumbers().size();i++){
            PhoneNumber number=contact.getPhoneNumbers().get(i);
            number.setContactId(contactId);
            session.save(number);
            if(i%50==0){
                session.flush();
                session.clear();
            }
        }
       // session.flush();
        return new Long("12");
    }

    @Override
    public void updateContact(Contact contact) {
        Connection connection = null;
        PreparedStatement updateStatement = null;
        try {
            connection = dataSource.getConnection();
            updateStatement = connection.prepareStatement(queriesProperties.getProperty(UPDATE_CONTACT));
            updateStatement.setString(FIRST_NAME_INDEX - 1, contact.getFirstName());
            updateStatement.setString(LAST_NAME_INDEX - 1, contact.getLastName());
            updateStatement.setString(COMPANY_INDEX - 1, contact.getCompany());
            updateStatement.setString(CONTENT_TYPE_INDEX - 1, contact.getContentType());
            if (contact.getPhoto() != null) {
                InputStream photoInputStream = new ByteArrayInputStream(contact.getPhoto());
                updateStatement.setBlob(PHOTO_INDEX - 1, photoInputStream);
            } else {
                updateStatement.setBlob(PHOTO_INDEX - 1, (InputStream) null);
            }
            updateStatement.setLong(6, contact.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(JDBC_ERROR_UPDATE);
        } finally {
            DbUtils.closeQuietly(updateStatement);
            DbUtils.closeQuietly(connection);
        }
    }

    @Override
    public Contact getContact(Integer id) {
        Session session=sessionFactory.getCurrentSession();
        Criteria criteria=session.createCriteria(Contact.class).add(Restrictions.eq("id",id));
        Contact contact=(Contact)criteria.list().get(0);
        Criteria criteriaContactPhone=session.createCriteria(PhoneNumber.class).add(Restrictions.eq("contactId",id));
        List<PhoneNumber> phoneNumbers=criteriaContactPhone.list();
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }

    private List<PhoneNumber> buildPhoneNumbersFromResultSet(Integer contactId, ResultSet resultSet) throws SQLException {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        while (resultSet.next()) {
            PhoneNumber number = new PhoneNumber();
            number.setContactId(contactId);
            number.setNumber(resultSet.getString(1));
            phoneNumbers.add(number);
        }
        return phoneNumbers;
    }

    private void setOutputParameters(CallableStatement statement) throws SQLException {
        statement.registerOutParameter(FIRST_NAME_INDEX, Types.VARCHAR);
        statement.registerOutParameter(LAST_NAME_INDEX, Types.VARCHAR);
        statement.registerOutParameter(COMPANY_INDEX, Types.VARCHAR);
        statement.registerOutParameter(PHOTO_INDEX, Types.BLOB);
        statement.registerOutParameter(CONTENT_TYPE_INDEX, Types.VARCHAR);
        statement.registerOutParameter(PHONE_CURSOR_INDEX, OracleTypes.CURSOR);
    }

    @Override
    public void deleteContact(Integer id) {
        Session session=sessionFactory.getCurrentSession();
        session.delete(getContact(id));
    }

    @Override
    public List<Contact> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts=criteria.list();
       for (Contact contact:contacts){
           Criteria criteriaContactPhone=session.createCriteria(PhoneNumber.class).add(Restrictions.eq("contactId",contact.getId()));
           List<PhoneNumber> phoneNumbers=criteriaContactPhone.list();
           contact.setPhoneNumbers(phoneNumbers);
       }
        return contacts;
    }

}
