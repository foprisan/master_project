/**
 *  on 18.04.2018
 */
package com.dizeratie.forum.entity.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizeratie.forum.entity.Section;
import com.dizeratie.forum.entity.Topic;
import com.dizeratie.forum.entity.User;


public interface TopicRepository extends JpaRepository<Topic, Integer> {
    
    Set<Topic> findBySection(Section section);
    
    Set<Topic> findByUser(User user);
    
    Set<Topic> findAllByOrderByCreationDateDesc();
    
    Set<Topic> findTop5ByOrderByCreationDateDesc();
    
    
}
