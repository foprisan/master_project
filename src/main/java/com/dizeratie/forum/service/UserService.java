/**
 * Created by Dawid Stankiewicz on 11.07.2016
 */
package com.dizeratie.forum.service;

import java.util.List;

import com.dizeratie.forum.entity.User;


public interface UserService {

    List<User> findAll();

    User findOne(int id);

    User findByUsername(String username);

    User findByEmail(String email);

    User save(User user);

    void create(User user);

    void remove(int id);

    void remove(User user);

    void remove(User user,
        String password);
}
