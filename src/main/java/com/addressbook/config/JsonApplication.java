package com.addressbook.config;

import com.addressbook.filters.AuthenticationFilter;
import com.addressbook.service.contacts.ContactsWebService;
import com.addressbook.service.contacts.UserWebService;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JsonApplication extends ResourceConfig {
	
	public JsonApplication(){
		super(
				ContactsWebService.class,
				UserWebService.class,
				JacksonFeature.class,
				AuthenticationFilter.class
				);
	}

}
