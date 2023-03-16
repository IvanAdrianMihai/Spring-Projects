package com.appsdeveloperblog.apps.ws.restfulwebservices.userservice.impl;

import com.appsdeveloperblog.apps.ws.restfulwebservices.exceptions.UserServiceException;
import com.appsdeveloperblog.apps.ws.restfulwebservices.shared.Utils;
import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.request.UpdateUserDetailsRequestModel;
import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.response.UserRest;
import com.appsdeveloperblog.apps.ws.restfulwebservices.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    Map<String, UserRest> users;
    Utils utils;

    public UserServiceImpl() {
        users = new HashMap<>();
    }

    @Autowired
    public UserServiceImpl(Utils utils) {
        this.utils = utils;
        users = new HashMap<>();
    }

    @Override
    public UserRest createUser(UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();
        returnValue.setEmail(userDetails.getEmail());
        returnValue.setFirstName(userDetails.getFirstName());
        returnValue.setLastName(userDetails.getLastName());

        String userId = utils.generateUserId();
        returnValue.setUserId(userId);
        users.put(userId, returnValue);
        return returnValue;
    }

    @Override
    public UserRest updateUser(String userId, UpdateUserDetailsRequestModel userDetails) throws UserServiceException {
        if (!users.containsKey(userId)) {
            throw new UserServiceException("This user not exists", HttpStatus.NOT_FOUND);
        }

        UserRest storedUserDetails = users.get(userId);
        storedUserDetails.setFirstName(userDetails.getFirstName());
        storedUserDetails.setLastName(userDetails.getLastName());

        users.put(userId, storedUserDetails);
        return storedUserDetails;
    }

    @Override
    public void deleteUser(String userId) throws UserServiceException {
        if (!users.containsKey(userId)) {
            throw new UserServiceException("This user not exists", HttpStatus.NOT_FOUND);
        }
        users.remove(userId);
    }

    @Override
    public UserRest findUser(String userId) throws UserServiceException {
        if (!users.containsKey(userId)) {
            throw new UserServiceException("This user not exists", HttpStatus.NOT_FOUND);
        }
        return users.get(userId);
    }

    @Override
    public List<UserRest> getAllUsers(Integer page, Integer limit, String sort) throws UserServiceException {
        int startIndex = page * limit;
        int endIndex = Math.min(startIndex + limit, users.size());

        if (endIndex <= startIndex) {
            return Collections.emptyList();
        }

        List<UserRest> usersRest = sortList(new ArrayList(users.values()), sort);
        return usersRest.subList(startIndex, endIndex);
    }

    private List<UserRest> sortList(List<UserRest> userRests, String sort) throws UserServiceException {
        String type = sort.toUpperCase().trim();
        switch (type) {
            case "ASC":
                Collections.sort(userRests, Comparator.comparing(UserRest::getUserId));
                return userRests;
            case "DESC":
                Collections.sort(userRests, (user1, user2) -> user2.getUserId().compareTo(user1.getUserId()));
                return userRests;
            default:
                throw new UserServiceException("There is only ascending and descending sorting", HttpStatus.BAD_REQUEST);
        }
    }
}
