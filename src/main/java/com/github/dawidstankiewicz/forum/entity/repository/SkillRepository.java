package com.github.dawidstankiewicz.forum.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.dawidstankiewicz.forum.entity.Post;
import com.github.dawidstankiewicz.forum.entity.Role;
import com.github.dawidstankiewicz.forum.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

	Skill findByName(String name);
	Skill findById(int id);
}
