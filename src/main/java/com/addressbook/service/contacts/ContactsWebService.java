package com.addressbook.service.contacts;

import com.addressbook.dto.AllContactResponseDto;
import com.addressbook.dto.ContactDto;
import com.addressbook.dto.CreateContactDto;
import com.addressbook.exceptions.ValidationException;
import com.addressbook.model.Contact;
import com.addressbook.validators.ContactValidator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Base64;
import java.util.List;

/**
 * Created by birsan on 5/9/2016.
 */
@Component
@Path("/api/contacts")
public class ContactsWebService {

    public static final String FILE_NAME = "allContacts";
    private static final String CONTACT_DELETED = "Contact successfully deleted.";
    private static final String CONTACT_NOT_DELETED = "Could not delete the contact.";
    private static final String CONTACT_NOT_UPDATED = "Could not update the contact.";
    private static final String CONTACT_UPDATED = "Contact successfully updated.";
    private static final String CONTACT_NOT_CREATED = "Could not create the contact!";
    private static final String CONTACT_CREATED = "Contact successfully created.";
    public static final String FILE_EXTENSION = ".txt";
    public static final String FIRST_NAME = "\tFirst name: ";
    public static final String LAST_NAME = "\tLast name: ";
    public static final String COMPANY = "\tCompany: ";
    public static final String PHONE_NUMBERS = "\t PhoneNumbers: ";
    public static final String CONTACT_NR = "Contact nr ";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String ATTACHMENT_FILENAME = "attachment; filename=";
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ContactDtoUtils contactDtoUtils;
    @Autowired
    private ContactValidator contactValidator;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AllContactResponseDto getAll() {
        try {
            List<ContactDto> contacts = contactDtoUtils.getContacts();
            AllContactResponseDto responseDTO = new AllContactResponseDto();
            responseDTO.setData(contacts);
            return responseDTO;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new AllContactResponseDto();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(CreateContactDto createContactDto) {
        try {
            Contact contact = buildContactFromDto(createContactDto);
            contactValidator.validateContact(contact);
            contactsService.createContact(contact);
            return Response.status(Response.Status.OK).entity(CONTACT_CREATED).build();
        } catch (ValidationException validationException) {
            validationException.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity(validationException.getMessages().toString()).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CONTACT_NOT_CREATED + ex.getMessage()).build();
        }
    }

    private Contact buildContactFromDto(CreateContactDto createContactDto) {
        Contact contact = new Contact();
        contact.setId(createContactDto.getId());
        contact.setCompany(createContactDto.getCompany());
        contact.setContentType(createContactDto.getContentType());
        contact.setFirstName(createContactDto.getFirstName());
        contact.setLastName(createContactDto.getLastName());
        contact.setPhoneNumbers(createContactDto.getPhoneNumbers());
        if (createContactDto.getPhoto() != null) {
            contact.setPhoto(Base64.getDecoder().decode(createContactDto.getPhoto()));
        }
        return contact;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response update(Contact contact) {
        try {
            contactValidator.validateContact(contact);
            contactsService.updateContact(contact);
            return Response.status(Response.Status.OK).entity(CONTACT_UPDATED).build();
        } catch (ValidationException validationException) {
            validationException.printStackTrace();
            return Response.status(Response.Status.CONFLICT).entity(validationException.getMessages().toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CONTACT_NOT_UPDATED + e.getMessage()).build();
        }
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        try {
            contactsService.deleteContact(id);
            return Response.status(Response.Status.OK).entity(CONTACT_DELETED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CONTACT_NOT_DELETED + e.getMessage()).build();
        }
    }

    @GET
    @Path("/file")
    //@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile() {
        try {
            File temp = File.createTempFile(FILE_NAME, FILE_EXTENSION);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp));
            List<ContactDto> contacts = contactDtoUtils.getContacts();
            for (ContactDto contact : contacts) {
                String contactText = CONTACT_NR + contacts.indexOf(contact) + ": " + FIRST_NAME + contact.getFirstName() +
                        LAST_NAME + contact.getLastName() + COMPANY + contact.getCompany() + PHONE_NUMBERS + contact.getPhoneNumber();
                bufferedWriter.write(contactText);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            //  Response.ResponseBuilder response = Response.ok(temp);
            byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(temp));
            Response.ResponseBuilder response = Response.ok(encoded);
            //response.header(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + temp.getName());
            return response.build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
