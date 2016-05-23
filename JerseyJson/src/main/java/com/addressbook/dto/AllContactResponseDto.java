package com.addressbook.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birsan on 5/10/2016.
 */
public class AllContactResponseDto {
    private List<ContactDto> data = new ArrayList<>();

    public List<ContactDto> getData() {
        return data;
    }

    public void setData(List<ContactDto> data) {
        this.data = data;
    }
}
