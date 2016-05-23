package com.addressbook;

import com.addressbook.exceptions.ValidationException;
import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import com.addressbook.validators.ContactValidator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birsan on 5/16/2016.
 */
public class ContactValidatorTest {
    private static final String VALID_NAME = "Name";
    private static final String VALID_COMPANY = "company";
    private static final String VALID_PHONE_NUMBER = "0000752028861";
    private static final String INVALID_NAME_VALID_LENGTH = "name";
    private static final String INVALID_NAME_VALID_CASE = "A01234567890123456789012345678";
    private static final String INVALID_PHONE_NUMBER_VALID_LENGTH = "A01234";
    private static final String INVALID_PHONE_NUMBER_VALID_DIGITS = "0";
    private static final String INVALID_COMPANY_VALID_CONTENT = "A01234567890123456789012345678";
    private static final String EMPTY_STRING = "";
    private static final String EMPTY_NAME_EXCEPTION_MESSAGE = "cannot be empty!";
    private static final String TOO_LONG_NAME_EXCEPTION_MESSAGE = "cannot contain more than";
    private static final String NON_UPPERCASE_EXCEPTION_MESSAGE = "must begin with an uppercase";
    private static final String EMPTY_COMPANY_EXCEPTION_MESSAGE = "cannot be empty!";
    private static final String TOO_LONG_COMPANY_EXCEPTION_MESSAGE = "cannot contain more than";
    private static final String INVALID_PHONE_NUMBER_EXCEPTION_MESSAGE = " elements must contain exactly 13 digits!";

    ContactValidator contactValidator = new ContactValidator();

    @Test
    public void createContactWithEmptyName() {
        // given
        // when
        // then
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithEmptyName();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(ex.getMessages().size(), 1);
            Assert.assertTrue(ex.getMessages().get(0).contains(EMPTY_NAME_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }

    private Contact givenContactWithEmptyName() {
        Contact dummyValidContact = dummyValidContact();
        dummyValidContact.setLastName(EMPTY_STRING);
        return dummyValidContact;
    }

    @Test
    public void createContactWithTooLongFirstName() {
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithTooLongFirstName();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(ex.getMessages().size(), 1);
            Assert.assertTrue(ex.getMessages().get(0).contains(TOO_LONG_NAME_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }


    private Contact givenContactWithTooLongFirstName() {
        Contact dummyValidContact = dummyValidContact();
        dummyValidContact.setFirstName(INVALID_NAME_VALID_CASE);
        return dummyValidContact;
    }

    @Test
    public void createContactWithNonUppercaseFirstName() {
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithNonUppercaseFirstName();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(ex.getMessages().size(), 1);
            Assert.assertTrue(ex.getMessages().get(0).contains(NON_UPPERCASE_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }

    private Contact givenContactWithNonUppercaseFirstName() {
        Contact contact = dummyValidContact();
        contact.setLastName(INVALID_NAME_VALID_LENGTH);
        return contact;
    }

    @Test
    public void createContactWithEmptyCompany() {
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithEmptyCompany();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(1, ex.getMessages().size());
            Assert.assertEquals(true, ex.getMessages().get(0).contains(EMPTY_COMPANY_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }

    private Contact givenContactWithEmptyCompany() {
        Contact contact = dummyValidContact();
        contact.setCompany(EMPTY_STRING);
        return contact;
    }

    @Test
    public void createContactWithTooLongCompany() {
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithTooLongCompany();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(ex.getMessages().size(), 1);
            Assert.assertTrue(ex.getMessages().get(0).contains(TOO_LONG_COMPANY_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }

    private Contact givenContactWithTooLongCompany() {
        Contact contact = dummyValidContact();
        contact.setCompany(INVALID_COMPANY_VALID_CONTENT);
        return contact;
    }

    @Test
    public void createContactWithEmptyPhoneNumbers() {
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithEmptyPhoneNumbers();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(ex.getMessages().size(), 1);
            Assert.assertTrue(ex.getMessages().get(0).contains(INVALID_PHONE_NUMBER_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }

    private Contact givenContactWithEmptyPhoneNumbers() {
        Contact contact = dummyValidContact();
        PhoneNumber number = new PhoneNumber();
        number.setNumber("");
        List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
        phoneNumbers.add(number);
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }

    @Test
    public void createContactWithTooLongPhoneNumbers() {
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithTooLongPhoneNumbers();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(ex.getMessages().size(), 1);
            Assert.assertTrue(ex.getMessages().get(0).contains(INVALID_PHONE_NUMBER_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }

    private Contact givenContactWithTooLongPhoneNumbers() {
        Contact contact = dummyValidContact();
        PhoneNumber number = new PhoneNumber();
        number.setNumber(INVALID_PHONE_NUMBER_VALID_DIGITS);
        List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
        phoneNumbers.add(number);
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }

    @Test
    public void createContactWithNonDigitPhoneNumbers() {
        boolean exceptionThrown = false;
        try {
            Contact contact = givenContactWithNonDigitPhoneNumbers();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertEquals(ex.getMessages().size(), 1);
            Assert.assertTrue(ex.getMessages().get(0).contains(INVALID_PHONE_NUMBER_EXCEPTION_MESSAGE));
        } finally {
            Assert.assertEquals(exceptionThrown, true);
        }
    }

    private Contact givenContactWithNonDigitPhoneNumbers() {
        Contact contact = dummyValidContact();
        PhoneNumber number = new PhoneNumber();
        number.setNumber(INVALID_PHONE_NUMBER_VALID_LENGTH);
        List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
        phoneNumbers.add(number);
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }

    @Test
    public void createValidContact() {
        boolean exceptionThrown = false;
        try {
            Contact contact = dummyValidContact();
            contactValidator.validateContact(contact);
        } catch (ValidationException ex) {
            exceptionThrown = true;
            Assert.assertTrue(ex.getMessages().size() == 0);
        } finally {
            Assert.assertEquals(exceptionThrown, false);
        }
    }


    private Contact dummyValidContact() {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        PhoneNumber number = new PhoneNumber();
        number.setNumber(VALID_PHONE_NUMBER);
        phoneNumbers.add(number);
        Contact contact = new Contact();
        contact.setLastName(VALID_NAME);
        contact.setFirstName(VALID_NAME);
        contact.setCompany(VALID_COMPANY);
        contact.setPhoneNumbers(phoneNumbers);
        return contact;
    }
}
