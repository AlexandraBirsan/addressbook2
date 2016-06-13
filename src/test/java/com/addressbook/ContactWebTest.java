package com.addressbook;

import com.addressbook.dto.AllContactResponseDto;
import com.addressbook.dto.ContactDto;
import com.addressbook.dto.CreateContactDto;
import com.addressbook.exceptions.ValidationException;
import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import com.addressbook.service.contacts.ContactDtoUtils;
import com.addressbook.service.contacts.ContactsService;
import com.addressbook.service.contacts.ContactsWebService;
import com.addressbook.validators.ContactValidator;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by birsan on 5/20/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class ContactWebTest {
    @Autowired private ContactsWebService contactsWebService;
    @Autowired private ContactDtoUtils contactsDtoUtilsMock;
    @Autowired private ContactsService contactsServiceMock;
    @Autowired private ContactValidator contactsValidatorMock;

    private static final String VALID_NAME = "Name";
    private static final String VALID_COMPANY = "company";
    private static final String VALID_PHONE_NUMBER = "0000752028861";

    @Test
    public void getAllContactSuccessfully() {
        EasyMock.reset(contactsDtoUtilsMock);
        ArrayList<ContactDto> contacts = new ArrayList<>();
        contacts.add(new ContactDto());
        contacts.add(new ContactDto());
        EasyMock.expect(contactsDtoUtilsMock.getContacts()).andReturn(contacts);
        EasyMock.replay(contactsDtoUtilsMock);
        Assert.assertEquals(2, contactsWebService.getAll().getData().size());
    }

    @Test
    public void getAllContactsWithExceptionThrown() {
        EasyMock.reset(contactsDtoUtilsMock);
        EasyMock.expect(contactsDtoUtilsMock.getContacts()).andThrow(new RuntimeException("simulated exception!"));
        EasyMock.replay(contactsDtoUtilsMock);
        AllContactResponseDto contactResponseDto = contactsWebService.getAll();
        Assert.assertEquals(0, contactResponseDto.getData().size());
    }

    @Test
    public void createValidContact() {
        Contact contact = dummyContact();
        Response actualResponse = contactsWebService.create(dummyContactDto());
        Assert.assertEquals(200, actualResponse.getStatus());
    }

    @Test
    public void createInvalidContactWithExceptionThrown() {
        EasyMock.reset(contactsServiceMock);
        contactsServiceMock.createContact(EasyMock.anyObject());
        EasyMock.expectLastCall().andThrow(new RuntimeException("simulated Exception"));
        EasyMock.replay(contactsServiceMock);
        Response actualResponse = contactsWebService.create(dummyContactDto());
        Assert.assertEquals(500, actualResponse.getStatus());
    }


    @Test
    public void createInvalidContactWithValidationExceptionThrown() {
        EasyMock.reset(contactsValidatorMock);
        contactsValidatorMock.validateContact(EasyMock.anyObject());
        EasyMock.expectLastCall().andThrow(new ValidationException());
        EasyMock.replay(contactsValidatorMock);
        Response actualResp = contactsWebService.create(dummyContactDto());
        Assert.assertEquals(409, actualResp.getStatus());
    }

    @Test
    public void updateValidContact() {
        EasyMock.reset(contactsValidatorMock);
        contactsValidatorMock.validateContact(dummyContact());
        EasyMock.replay(contactsValidatorMock);
        EasyMock.reset(contactsServiceMock);
        contactsServiceMock.updateContact(dummyContact());
        EasyMock.replay(contactsServiceMock);
        Assert.assertEquals(200, contactsWebService.update(dummyContact()).getStatus());
    }

    @Test
    public void updateInvalidContact() {
        Contact contact = dummyContact();
        EasyMock.reset(contactsValidatorMock);
        contactsValidatorMock.validateContact(contact);
        EasyMock.expectLastCall().andThrow(new ValidationException());
        EasyMock.replay(contactsValidatorMock);
        Assert.assertEquals(409, contactsWebService.update(contact).getStatus());
    }

    @Test
    public void deleteValidContact() {
        contactsWebService.delete(null/*any*/);
        Assert.assertEquals(200, contactsWebService.update(dummyContact()).getStatus());
    }

    @Test
    public void deleteInvalidContact() {
        Integer id = null;
        EasyMock.reset(contactsServiceMock);
        contactsServiceMock.deleteContact(id);
        EasyMock.expectLastCall().andThrow(new RuntimeException("simulated Exception"));
        EasyMock.replay(contactsServiceMock);
        Assert.assertEquals(500, contactsWebService.delete(id).getStatus());
    }

    private Contact dummyContact() {
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

    private CreateContactDto dummyContactDto() {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        PhoneNumber number = new PhoneNumber();
        number.setNumber(VALID_PHONE_NUMBER);
        phoneNumbers.add(number);
        CreateContactDto contact = new CreateContactDto();
        contact.setLastName(VALID_NAME);
        contact.setFirstName(VALID_NAME);
        contact.setCompany(VALID_COMPANY);
        contact.setPhoneNumbers(phoneNumbers);
        contact.setPhoto("");
        contact.setContentType("");
        return contact;
    }
    private Contact buildContactFromDto(CreateContactDto createContactDto) {
        Contact contact = new Contact();
        contact.setId(createContactDto.getId());
        contact.setCompany(createContactDto.getCompany());
        contact.setFirstName(createContactDto.getFirstName());
        contact.setLastName(createContactDto.getLastName());
        contact.setPhoneNumbers(createContactDto.getPhoneNumbers());
        return contact;
    }
}
