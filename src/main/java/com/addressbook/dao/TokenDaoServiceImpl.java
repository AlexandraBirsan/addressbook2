package com.addressbook.dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by birsan on 5/31/2016.
 */
@Component("tokenService")
public class TokenDaoServiceImpl {

    private Map<Integer, String> userIdByToken = new HashMap<>();

    public Map<Integer, String> getUserIdByToken() {
        return userIdByToken;
    }

    public void setUserIdByToken(Map<Integer, String> userIdByToken) {
        this.userIdByToken = userIdByToken;
    }


}
