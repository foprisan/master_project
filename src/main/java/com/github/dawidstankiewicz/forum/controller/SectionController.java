/**
 * Created by Dawid Stankiewicz on 19.07.2016
 */
package com.github.dawidstankiewicz.forum.controller;

import com.github.dawidstankiewicz.forum.service.SectionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.dawidstankiewicz.forum.controller.form.NewSectionForm;
import com.github.dawidstankiewicz.forum.entity.Role;
import com.github.dawidstankiewicz.forum.entity.Section;
import com.github.dawidstankiewicz.forum.entity.Topic;
import com.github.dawidstankiewicz.forum.entity.User;
import com.github.dawidstankiewicz.forum.service.RoleService;
import com.github.dawidstankiewicz.forum.service.TopicService;
import com.github.dawidstankiewicz.forum.service.UserService;
import com.github.dawidstankiewicz.forum.tools.HashMapSort;
import com.github.dawidstankiewicz.forum.tools.Recommender;


@Controller
@RequestMapping("/section/")
public class SectionController {
    
    @Autowired
    private SectionService sectionService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @RequestMapping("{id}")
    public String getTopicsFromSection(@PathVariable int id,
                                       Model model) {
        model.addAttribute("section", sectionService.findOne(id));
        model.addAttribute("topics", topicService.findBySection(id));
        return "section";
    }
    
    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String getNewSectionForm(Model model) {
        model.addAttribute("newSection", new NewSectionForm());
        return "new_section_form";
    }
    
    @RequestMapping(value = "new", method = RequestMethod.POST)
    public String processAndAddNewSection(
                                          @Valid @ModelAttribute("newSection") NewSectionForm newSection,
                                          BindingResult result) {
        
        if (result.hasErrors()) {
            return "new_section_form";
        }
        
        Section section = new Section();
        section.setName(newSection.getName());
        section.setDescription(newSection.getDescription());
        section = sectionService.save(section);
        return "redirect:/section/" + section.getId();
    }
    
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id,
                         Authentication authentication,
                         RedirectAttributes model) {
        User user = userService.findByUsername(authentication.getName());
        Role adminRole = roleService.findByName("ADMIN");
        if (!user.getRoles().contains(adminRole)) {
            return "redirect:/section/" + id;
        }
        sectionService.delete(id);
        
        model.addFlashAttribute("message", "section.successfully.deleted");
        return "redirect:/home";
    }
    @RequestMapping(value = "/{id}/recommend", method = RequestMethod.GET)
    public String recommendProjects(Authentication authentication,RedirectAttributes model){
    	Set<Topic> topics=topicService.findBySection(sectionService.findByName("Projects").getId());
    	HashMap<Integer,List<String>> projects= new HashMap<Integer,List<String>>();
    	for (Topic topic: topics){
    		ArrayList<String> topicContent= new ArrayList<String>(Arrays.asList(topic.getContent().split(" |\\.|,")));
    		projects.put(topic.getId(), topicContent);
    	}
    	ArrayList<String> userPref= new ArrayList<String>(Arrays.asList(userService.findByUsername(authentication.getName()).getInfo().getAboutMe().split(" |\\.|,")));
    	Recommender recommender= new Recommender(0, projects,userPref);
    	HashMapSort hsort = new HashMapSort(recommender.getRecommendations());
    	hsort.sortHashMap();
    	return "redirect:/section/" + sectionService.findByName("Projects").getId();
    }
}
