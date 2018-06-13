
package com.dizeratie.forum.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dizeratie.forum.entity.Role;
import com.dizeratie.forum.entity.User;
import com.dizeratie.forum.entity.UserInfo;
import com.dizeratie.forum.entity.repository.UserRepository;
import com.dizeratie.forum.exception.IncorrectPasswordException;
import com.dizeratie.forum.exception.UserNotFoundException;
import com.dizeratie.forum.service.RoleService;
import com.dizeratie.forum.service.UserService;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(int id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void create(User user) {
        Set<Role> roles = new HashSet<>();
        Role role = roleService.findByName("USER");
        roles.add(role);
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        UserInfo info = new UserInfo();
        info.setUser(user);
        user.setInfo(info);
        userRepository.save(user);
    }

    @Override
    public void remove(int id) {
        remove(findOne(id));
    }

    @Override
    public void remove(User user) {
        userRepository.delete(user);
    }

    @Override
    public void remove(User user,
        String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("user: " + user.getPassword() + " pass: " + passwordEncoder.encode(
                password));
            throw new IncorrectPasswordException();
        }
        userRepository.delete(user);
    }
}
