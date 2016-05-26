package com.addressbook.dao;

import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import org.apache.commons.dbutils.DbUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by birsan on 4/27/2016.
 */
@Component("phoneNumberDao")
@Transactional
public class PhoneNumberDaoImpl implements PhoneNumberDao {
    private static final String UPDATE_PHONE_NUMBERS = "updatePhoneNumbers";
    private static final String DELETE_UPDATE_PHONE_NUMBERS = "deleteBeforeUpdatePhoneNumbers";
    private static final String CREATE_PHONE_NUMBER = "createPhoneNumber";
    @Autowired
    private DataSource dataSource;
    @Autowired
    private Properties queriesProperties;
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void createPhoneNumbers(Long id, List<PhoneNumber> phoneNumbers) {
        Connection connection = null;
        PreparedStatement createStatement = null;
        try {
            connection = dataSource.getConnection();
            createStatement = connection.prepareStatement(queriesProperties.getProperty(CREATE_PHONE_NUMBER));
            for (PhoneNumber number : phoneNumbers) {
                createStatement.setLong(1, id);
                createStatement.setString(2, number.getNumber());
                createStatement.addBatch();
            }
            createStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            DbUtils.closeQuietly(createStatement);
            DbUtils.closeQuietly(connection);
        }
    }

    @Override
    public void updatePhoneNumber(Contact contact) {
        Connection connection = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement updateStatement = null;
        try {
            connection = dataSource.getConnection();
            deleteStatement = connection.prepareStatement(queriesProperties.getProperty(DELETE_UPDATE_PHONE_NUMBERS));
            deleteStatement.setLong(1, contact.getId());
            deleteStatement.executeUpdate();
            updateStatement = connection.prepareStatement(queriesProperties.getProperty(DELETE_UPDATE_PHONE_NUMBERS));
            for (int i = 0; i < contact.getPhoneNumbers().size(); i++) {
                updateStatement.setLong(1, contact.getId());
                updateStatement.setString(2, contact.getPhoneNumbers().get(i).getNumber());
                updateStatement.addBatch();
            }
            updateStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(deleteStatement);
            DbUtils.closeQuietly(updateStatement);
            DbUtils.closeQuietly(connection);
        }
    }
}
