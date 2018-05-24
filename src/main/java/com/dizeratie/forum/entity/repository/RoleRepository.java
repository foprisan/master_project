/**
 * Created by Dawid Stankiewicz on 22.07.2016
 */
package com.dizeratie.forum.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizeratie.forum.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Role findByName(String name);
    
}
