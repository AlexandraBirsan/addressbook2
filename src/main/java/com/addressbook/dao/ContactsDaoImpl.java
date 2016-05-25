package com.addressbook.dao;

import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import oracle.jdbc.OracleTypes;
import org.apache.commons.dbutils.DbUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
//@Component("contactsDao")
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

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Long createContact(Contact contact) {
        Statement sequenceValue = null;
        Long contactId = null;
        Connection connection = null;
        try {
            String createContactSQL = queriesProperties.getProperty(CREATE_CONTACT);
            String nextValSQL = queriesProperties.getProperty(NEXT_VAL);
            connection = dataSource.getConnection();
            sequenceValue = connection.createStatement();
            synchronized (this) {
                ResultSet rs = sequenceValue.executeQuery(nextValSQL);
                if (rs.next()) {
                    contactId = rs.getLong(1);
                }
            }
            InputStream photoInputStream = null;
            if (contact.getPhoto() != null) {
                photoInputStream = new ByteArrayInputStream(contact.getPhoto());
            }
            Object[] params = new Object[]{contactId, contact.getFirstName(), contact.getLastName(), contact.getCompany(), contact.getContentType(), photoInputStream};
            int[] types = new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BLOB};
            jdbcTemplate.update(createContactSQL, params, types);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(JDBC_ERROR_CREATE);
        } finally {
            DbUtils.closeQuietly(sequenceValue);
            DbUtils.closeQuietly(connection);
        }
        return contactId;
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
        Connection connection = null;
        CallableStatement statement = null;
        Contact contact = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareCall(queriesProperties.getProperty(GET_CONTACT));
            statement.setInt(1, id);
            setOutputParameters(statement);
            statement.execute();
            String firstName = statement.getString(FIRST_NAME_INDEX);
            String lastName = statement.getString(LAST_NAME_INDEX);
            String company = statement.getString(COMPANY_INDEX);
            Blob photo = statement.getBlob(PHOTO_INDEX);
            String contentType = statement.getString(CONTENT_TYPE_INDEX);
            ResultSet phoneResultSet = (ResultSet) statement.getObject(PHONE_CURSOR_INDEX);
            List<PhoneNumber> phoneNumbers = buildPhoneNumbersFromResultSet(id, phoneResultSet);
            contact = new Contact();
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setCompany(company);
            if (photo == null) {
                contact.setPhoto(null);
                contact.setContentType(null);
            } else {
                contact.setPhoto(photo.getBytes(1, (int) photo.length()));
                contact.setContentType(contentType);
            }
            if (phoneNumbers == null)
                phoneNumbers = new ArrayList<>();
            contact.setPhoneNumbers(phoneNumbers);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(JDBC_ERROR_GET);
        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }
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
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(queriesProperties.getProperty(DELETE));
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(JDBC_ERROR_DELETE);
        } finally {
            DbUtils.closeQuietly(statement);
            DbUtils.closeQuietly(connection);
        }

    }

    @Override
    public List<Contact> getAll() {
//        Connection connection = null;
//        Statement statement = null;
//        List<Contact> contacts = null;
//        try {
//            connection = dataSource.getConnection();
//            statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery(queriesProperties.getProperty(GET_ALL_CONTACTS));
//            contacts = new ArrayList<>();
//            while (rs.next()) {
//                Integer idContact = rs.getInt(1);
//                Contact contact = getContact(idContact);
//                contact.setId(idContact);
//                contacts.add(contact);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException(JDBC_ERROR_GET_ALL);
//        } finally {
//            DbUtils.closeQuietly(statement);
//            DbUtils.closeQuietly(connection);
//        }
//        return contacts;
        Session session = sessionFactory.getCurrentSession();
        // session.beginTransaction();
        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = (List<Contact>) criteria.list();
        //   session.getTransaction().commit();
        return contacts;
    }

}
