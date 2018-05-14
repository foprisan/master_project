/**
 * Created by Dawid Stankiewicz on 3 Jul 2016
 */
package com.github.dawidstankiewicz.forum.controller;

import com.github.dawidstankiewicz.forum.service.PostService;
import com.github.dawidstankiewicz.forum.service.SectionService;
import com.github.dawidstankiewicz.forum.service.SkillService;
import com.github.dawidstankiewicz.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class HomeController {
    
    @Autowired
    private SectionService sectionService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private SkillService skillService;
    
    @RequestMapping(value = { "/",
                              "/home" })
    public String home(Model model) {
        model.addAttribute("sections", sectionService.findAll());
        model.addAttribute("topics", topicService.findRecent());
        model.addAttribute("posts", postService.findRecent());
        return "home";
    }

}
