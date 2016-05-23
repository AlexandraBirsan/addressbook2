package com.addressbook;

import com.addressbook.dto.ContactDto;
import com.addressbook.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birsan on 5/20/2016.
 */
public class NewSolver {
    public List<ContactDto> getContacts(){
        return buildContactDtos();
    }

    public void createContact(Contact contact){}

    private List<ContactDto> buildContactDtos() {
        return new ArrayList<>();
    }
}
