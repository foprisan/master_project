/**
 *  on 17.04.2018
 */
package com.dizeratie.forum.service;

import java.util.List;
import java.util.Set;

import com.dizeratie.forum.entity.Post;
import com.dizeratie.forum.entity.Topic;
import com.dizeratie.forum.entity.User;


public interface PostService {
    
    Post findOne(int id);
    
    List<Post> findAll();
    
    Set<Post> findRecent();
    
    Set<Post> findAllByOrderByCreationDateDesc();
    
    Set<Post> findByUser(User user);
    
    Set<Post> findByTopic(int idTopic);
    
    Set<Post> findByTopic(Topic topic);
    
    void save(Post post);
    
    void delete(int id);
    
    void delete(Post post);
    
    void save(String content,
              String username,
              int idTopic);
    
}
