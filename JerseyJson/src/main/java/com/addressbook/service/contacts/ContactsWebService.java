package com.addressbook.service.contacts;

import com.addressbook.dto.AllContactResponseDto;
import com.addressbook.dto.ContactDto;
import com.addressbook.exceptions.ValidationException;
import com.addressbook.model.Contact;
import com.addressbook.validators.ContactValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by birsan on 5/9/2016.
 */
@Component
@Path("/api/contacts")
public class ContactsWebService {

    private static final String CONTACT_DELETED = "Contact successfully deleted.";
    private static final String CONTACT_NOT_DELETED = "Could not delete the contact.";
    private static final String CONTACT_NOT_UPDATED = "Could not update the contact.";
    private static final String CONTACT_UPDATED = "Contact successfully updated.";
    private static final String CONTACT_NOT_CREATED = "Could not create the contact!";
    private static final String CONTACT_CREATED = "Contact successfully created.";
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
    public Response create(Contact contact) {
        try {
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
}
