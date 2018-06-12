/**
 *  on 04.08.2016
 */
package com.dizeratie.forum.service.model.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dizeratie.forum.controller.model.UserProfile;
import com.dizeratie.forum.entity.User;
import com.dizeratie.forum.service.PostService;
import com.dizeratie.forum.service.TopicService;
import com.dizeratie.forum.service.UserService;
import com.dizeratie.forum.service.model.UserProfileService;


@Service
public class UserProfileServiceImpl implements UserProfileService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private TopicService topicService;
    
    @Override
    public UserProfile findOne(int userId) {
        UserProfile userProfile = new UserProfile();
        User user = userService.findOne(userId);
        userProfile.setUser(user);
        userProfile.setPosts(postService.findByUser(user));
        userProfile.setTopics(topicService.findByUser(user));
        return userProfile;
    }
    
    @Override
    public UserProfile findOne(String username) {
        return findOne(userService.findByUsername(username).getId());
    }
    
}
