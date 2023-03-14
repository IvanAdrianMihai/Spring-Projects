package com.appsdeveloperblog.apps.ws.restfulwebservices.ui.controller;

import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.apps.ws.restfulwebservices.ui.model.response.UserRest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping
    public String getUsers(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "limit", defaultValue = "50") Integer limit,
                           @RequestParam(value = "sort", required = false) String sort) {
        return "get users was called with page = " + page + " and limit = " + limit + " and sort = " + sort;
    }

    @GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userId) {
        UserRest returnValue = new UserRest();
        returnValue.setEmail("my_email@gmail.com");
        returnValue.setFirstName("Adrian");
        returnValue.setLastName("Ivan");
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();
        returnValue.setEmail(userDetails.getEmail());
        returnValue.setFirstName(userDetails.getFirstName());
        returnValue.setLastName(userDetails.getLastName());
        return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
