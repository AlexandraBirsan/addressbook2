package com.addressbook.dao;

import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;

import java.util.List;

/**
 * Created by birsan on 4/27/2016.
 */

public interface PhoneNumberDao {
    void createPhoneNumbers(Long id, List<PhoneNumber> phoneNumbers);
    void updatePhoneNumber(Contact contact);
}
