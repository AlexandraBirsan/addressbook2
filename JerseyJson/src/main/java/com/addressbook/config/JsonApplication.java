package com.addressbook.config;

import com.addressbook.service.contacts.ContactsWebService;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JsonApplication extends ResourceConfig {
	
	public JsonApplication(){
		super(
				ContactsWebService.class,
				JacksonFeature.class
				);
	}

}
