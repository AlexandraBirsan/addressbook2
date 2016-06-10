package com.addressbook.filters;


import com.addressbook.dao.TokenDaoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;


@Component
public class AuthenticationFilter implements ContainerRequestFilter {
    @Autowired private TokenDaoServiceImpl tokenDaoService;
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString("AuthToken");
        if ((!requestContext.getUriInfo().getRequestUri().getPath().equals("/addressbook/user/authenticate"))) {
            if (authorizationHeader == null) {
                throw new NotAuthorizedException("Missing header!");
            } else if (authorizationHeader.isEmpty()) {
                throw new NotAuthorizedException("Empty header!");
            }
            try {
                validateToken(authorizationHeader);
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }

    private void validateToken(String token) throws Exception {
        String[] data=token.split(":");
        if(tokenDaoService.getUserIdByToken().get(Integer.parseInt(data[0]))==null){
            throw new NotAuthorizedException("Invalid user id!");
        }else if(!tokenDaoService.getUserIdByToken().get(Integer.parseInt(data[0])).equals(token)){
            throw new NotAuthorizedException("Invalid token value!");
        }
    }
}
