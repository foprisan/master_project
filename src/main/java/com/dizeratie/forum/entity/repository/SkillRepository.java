package com.dizeratie.forum.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizeratie.forum.entity.Post;
import com.dizeratie.forum.entity.Role;
import com.dizeratie.forum.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

	Skill findByName(String name);
	Skill findById(int id);
}
