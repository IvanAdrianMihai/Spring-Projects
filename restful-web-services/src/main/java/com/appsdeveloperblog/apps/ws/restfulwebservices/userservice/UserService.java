package com.appsdeveloperblog.apps.ws.restfulwebservices.userservice;

import com.appsdeveloperblog.apps.ws.restfulwebservices.exceptions.UserServiceException;
import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.request.UpdateUserDetailsRequestModel;
import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.response.UserRest;

import java.util.List;

public interface UserService {
    UserRest createUser(UserDetailsRequestModel userDetails);

    UserRest updateUser(String userId, UpdateUserDetailsRequestModel userDetails) throws UserServiceException;

    void deleteUser(String userId) throws UserServiceException;

    UserRest findUser(String userId) throws UserServiceException;

    List<UserRest> getAllUsers(Integer page, Integer limit, String sort) throws UserServiceException;
}
