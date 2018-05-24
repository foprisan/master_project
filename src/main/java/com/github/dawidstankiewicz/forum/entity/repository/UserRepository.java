
 
package com.github.dawidstankiewicz.forum.entity.repository;

import com.github.dawidstankiewicz.forum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findByUsername(String username);
    
    User findByEmail(String email);
    
}
