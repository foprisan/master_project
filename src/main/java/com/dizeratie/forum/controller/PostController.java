/**
 * Created by Dawid Stankiewicz on 23.07.2016
 */
package com.dizeratie.forum.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dizeratie.forum.controller.form.PostEditForm;
import com.dizeratie.forum.entity.Post;
import com.dizeratie.forum.service.PostService;


@Controller
@RequestMapping(value = "/post")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable int id,
                         Authentication authentication,
                         RedirectAttributes model ) {
        Post post = postService.findOne(id);
        if (post == null || authentication == null || authentication.getName() == null
                || !authentication.getName().equals(post.getUser().getUsername())) {
            return "redirect:/";
        }
        
        postService.delete(post);
        
        model.addFlashAttribute("message", "post.successfully.deleted");
        return "redirect:/topic/" + post.getTopic().getId();
    }
    
    

    @RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String editPost(@PathVariable int id, Authentication authentication,Model model){
    	Post post = postService.findOne(id);
        if (post == null || authentication == null || authentication.getName() == null
                || !authentication.getName().equals(post.getUser().getUsername())) {
            return "redirect:/";
        }
        
        model.addAttribute("post", post);
        model.addAttribute("postEditForm", new PostEditForm());
        
        
        
        return "post_edit_form";
    
	}

    @RequestMapping(value="/edit/{id}",method=RequestMethod.POST)
	public String edit(HttpServletRequest request,@PathVariable int id, Authentication authentication,RedirectAttributes model){
    	Post post = postService.findOne(id);
        if (post == null || authentication == null || authentication.getName() == null
                || !authentication.getName().equals(post.getUser().getUsername())) {
            return "redirect:/";
        }
        
       System.out.println("POST PST");
        post.setContent(request.getParameter("content"));
        postService.save(post);
        model.addFlashAttribute("message", "post.successfully.edited");
        return "redirect:/topic/" + post.getTopic().getId();
    
	} 
}
