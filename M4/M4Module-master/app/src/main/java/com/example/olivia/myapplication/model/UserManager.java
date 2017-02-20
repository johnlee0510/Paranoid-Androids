package com.example.olivia.myapplication.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Olivia on 2/12/2017.
 */

public class UserManager implements AuthenticationFacade, UserManagementFacade {
    User u;// Modified by Rayna to enable accessing the user from LoginActivity
    private static Map<String, User> users = new HashMap<>();


    public User findUserById(String id) {
        return users.get(id);
    }

    public void addUser(String name, String pass) {
        User user = new User(name, pass);
        users.put(name, user);
    }

    public boolean handleLoginRequest(String name, String pass) {
        u = findUserById(name);//Modified by Rayna
        return u!=null && u.checkPassword(pass);
    }
    public User getUser(){ // Added by Rayna
        return u;
    }





}
