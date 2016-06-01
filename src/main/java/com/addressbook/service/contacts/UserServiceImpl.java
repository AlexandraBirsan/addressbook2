package com.addressbook.service.contacts;

import com.addressbook.dao.UserDao;
import com.addressbook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by birsan on 5/31/2016.
 */
@Component("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User getUser(User user) {
        return userDao.getUser(user);
    }
}
