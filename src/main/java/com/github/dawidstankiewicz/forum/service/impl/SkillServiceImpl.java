package com.github.dawidstankiewicz.forum.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.dawidstankiewicz.forum.entity.Skill;
import com.github.dawidstankiewicz.forum.entity.repository.SkillRepository;
import com.github.dawidstankiewicz.forum.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {
    
    @Autowired
    private SkillRepository SkillRepository;
    
    @Override
    public List<Skill> findAll() {
        return SkillRepository.findAll();
    }
    
    @Override
    public Skill findOne(int id) {
        return SkillRepository.findOne(id);
    }
    
    @Override
    public Skill findByName(String name) {
        return SkillRepository.findByName(name);
    }
    
    @Override
    public Skill save(Skill Skill) {
        return SkillRepository.save(Skill);
    }
    
    @Override
    public void delete(int id) {
        delete(findOne(id));
    }
    
    @Override
    public void delete(Skill Skill) {
        SkillRepository.delete(Skill);
    }
    
}

