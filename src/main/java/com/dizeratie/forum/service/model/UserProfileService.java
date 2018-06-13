
package com.dizeratie.forum.service.model;

import com.dizeratie.forum.controller.model.UserProfile;


public interface UserProfileService {
    
    public UserProfile findOne(int userId);
    
    public UserProfile findOne(String username);
    
}
