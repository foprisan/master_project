
package com.dizeratie.forum.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.dizeratie.forum.controller.form.NewSectionForm;
import com.dizeratie.forum.entity.Role;
import com.dizeratie.forum.entity.Section;
import com.dizeratie.forum.entity.Topic;
import com.dizeratie.forum.entity.User;
import com.dizeratie.forum.service.RoleService;
import com.dizeratie.forum.service.SectionService;
import com.dizeratie.forum.service.TopicService;
import com.dizeratie.forum.service.UserService;
import com.dizeratie.forum.tools.HashMapSort;
import com.dizeratie.forum.tools.Recommender;


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
    public String recommendProjects(@PathVariable int id,Authentication authentication, Model model){
    	Set<Topic> topics=topicService.findBySection(sectionService.findByName("Projects").getId());
    	HashMap<Integer,List<String>> projects= new HashMap<Integer,List<String>>();
    	for (Topic topic: topics){
    		ArrayList<String> topicContent= new ArrayList<String>(Arrays.asList(topic.getContent().split(" |\\.|,")));
    		projects.put(topic.getId(), topicContent);
    	}
    	ArrayList<String> userPref= new ArrayList<String>(Arrays.asList(userService.findByUsername(authentication.getName()).getInfo().getAboutMe().split(" |\\.|,")));
    	Recommender recommender= new Recommender(0, projects,userPref);
    	HashMapSort hsort = new HashMapSort(recommender.getRecommendations());
    	HashMap<Integer, HashMap<String, Double>> termsTFIDF=recommender.getTFIDFforProjectTerms();
    	 System.out.println("termtfidf Map: " + Arrays.toString(termsTFIDF.entrySet().toArray()));
    	LinkedHashMap <Integer,Double> result=hsort.sortHashMap();
    	
    	List<Topic>recommendedProjects= new ArrayList<Topic>();
    	for (Integer projectId : result.keySet()){
    		if (result.get(projectId)>0.1 && !projectId.equals(99999999)){
    			Topic topic = topicService.findOne(projectId);
    			recommendedProjects.add(topic);
    	    	System.out.println(topic.getTitle());
    		}
    	}
    	Section projectSection = sectionService.findOne(id);
    	System.out.println(projectSection.getName());
    	
    	model.addAttribute("section",projectSection);
    	
    	model.addAttribute("recommendedProjects",recommendedProjects);
    	return "recommended_projects";
    }
    @RequestMapping(value = "chat", method = RequestMethod.GET)
    public String getChat(Authentication authentication,Model model) {
    	User user = userService.findByUsername(authentication.getName());
    	 model.addAttribute("user",user);
        return "chat";
    }
    @RequestMapping(value = "apply/{id}", method = RequestMethod.GET)
    public String applyForProjectById(@PathVariable int id,Authentication authentication,Model model) {
    	User user = userService.findByUsername(authentication.getName());
    	Topic project= topicService.findOne(id);
    	model.addAttribute("project",project);
    	 model.addAttribute("user",user);
        return "chat";
    }
}
