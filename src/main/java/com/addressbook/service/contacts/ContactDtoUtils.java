package com.addressbook.service.contacts;

import com.addressbook.dto.ContactDto;
import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birsan on 5/10/2016.
 */
@Component
public class ContactDtoUtils {

    @Autowired private ContactsService contactsService;

    public List<ContactDto> getContacts() {
        List<Contact> contacts = contactsService.getAll();
        List<ContactDto> contactDtos = new ArrayList<>(contacts.size());
        contacts.stream().forEach(contact -> {
            ContactDto contactDto = new ContactDto();
            contactDto.setCompany(contact.getCompany());
            contactDto.setLastName(contact.getLastName());
            contactDto.setFirstName(contact.getFirstName());
            contactDto.setPhoneNumber(buildPhoneNumber(contact));
            contactDto.setId(contact.getId());
            contactDtos.add(contactDto);
        });
        return contactDtos;
    }

    private static String buildPhoneNumber(Contact contact) {
        String phoneNum = "";
        for (PhoneNumber number : contact.getPhoneNumbers()) {
            phoneNum += number.getNumber() + ", ";
        }
        if (phoneNum.length() > 2) {
            phoneNum = phoneNum.substring(0, phoneNum.length() - 2);
        }
        return phoneNum;
    }
}