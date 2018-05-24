/**
 * Created by Dawid Stankiewicz on 17.07.2016
 */
package com.dizeratie.forum.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizeratie.forum.entity.Section;


public interface SectionRepository extends JpaRepository<Section, Integer> {
    
    Section findByName(String name);
    
}
