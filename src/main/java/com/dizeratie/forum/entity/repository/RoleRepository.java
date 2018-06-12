/**
 *  on 22.04.2018
 */
package com.dizeratie.forum.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizeratie.forum.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Role findByName(String name);
    
}
