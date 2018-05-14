package com.github.dawidstankiewicz.forum.service;

import java.util.List;

import com.github.dawidstankiewicz.forum.entity.Skill;

public interface SkillService {

	 List<Skill> findAll();
	    
	    Skill findOne(int id);
	    
	    Skill findByName(String name);
	    
	    Skill save(Skill Skill);
	    
	    void delete(int id);
	    
	    void delete(Skill Skill);
}
