package com.addressbook.service.contacts;

import com.addressbook.dao.TokenDaoServiceImpl;
import com.addressbook.model.Credentials;
import com.addressbook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by birsan on 5/31/2016.
 */
@Component
@Path("/user")
public class UserWebService {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenDaoServiceImpl tokenService;

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {

        try {
            User user = getUser(credentials);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            String token = generateToken(user.getId());
            tokenService.getUserIdByToken().put(user.getId(), token);
            return Response.status(Response.Status.OK).entity(token).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private User getUser(Credentials credentials) throws Exception {
        User user = new User();
        user.setUsername(credentials.getUsername());
        user.setPassword(credentials.getPassword());
        return userService.getUser(user);
    }

    private String generateToken(Integer userId) {
        Random random = new SecureRandom();
        String randomString = new BigInteger(130, random).toString(32);
        return userId + ":" + randomString;
    }
}
