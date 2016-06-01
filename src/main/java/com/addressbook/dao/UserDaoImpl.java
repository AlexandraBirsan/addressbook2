package com.addressbook.dao;

import com.addressbook.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by birsan on 5/31/2016.
 */
@Component("userDao")
public class UserDaoImpl implements UserDao {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private Properties queriesProperties;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public User getUser(User user) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("username", user.getUsername()));
        User userDb = (User) criteria.list().get(0);
        return userDb;
    }
}
