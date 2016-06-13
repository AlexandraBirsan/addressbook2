package com.addressbook.dto;

/**
 * Created by birsan on 4/20/2016.
 */
public class ContactDto extends AbstractContactDTO {
    private String photo;
    private String contentType;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContentType() {
        return contentType;
    }

    public ContactDto setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
