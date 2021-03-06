
package com.dizeratie.forum.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dizeratie.forum.controller.form.NewUserForm;
import com.dizeratie.forum.controller.form.UserEditForm;
import com.dizeratie.forum.controller.model.UserProfile;
import com.dizeratie.forum.entity.User;
import com.dizeratie.forum.entity.UserInfo;
import com.dizeratie.forum.exception.UserNotFoundException;
import com.dizeratie.forum.service.UserService;
import com.dizeratie.forum.service.model.UserProfileService;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping(value = "/user/{username}")
    public String findUserByUsernameAndViewProfilePage(@PathVariable String username,
        Model model) {
        UserProfile userProfile;
        try {
            userProfile = userProfileService.findOne(username);
        } catch (NullPointerException e) {
            throw new UserNotFoundException();
        }
        model.addAttribute("userProfile", userProfile);
        return "user";
    }

    @RequestMapping(value = "/user/id/{id}")
    public String findUserByIdAndViewProfilePage(@PathVariable int id,
        Model model) {
        return "redirect:/user/" + userService.findOne(id).getUsername();
    }

    @RequestMapping(value = "/users")
    public String listOfAllUser(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String regiristrationPage(Model model) {
        model.addAttribute("newUser", new NewUserForm());
        return "new_user_form";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String processAndSaveNewUser(@Valid @ModelAttribute("newUser") NewUserForm newUser,
        BindingResult result,
        RedirectAttributes model) {

        if (result.hasErrors()) {
            return "new_user_form";
        }

        User user = new User();
        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        user.setPassword(newUser.getPassword());

        userService.create(user);

        model.addFlashAttribute("message", "user.successfully.added");
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username or password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = "/logout")
    public String logOutAndRedirectToLoginPage(Authentication authentication,
        HttpServletRequest request,
        HttpServletResponse response) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout=true";
    }


    @RequestMapping(value = "/myprofile")
    public String myProfile(Authentication authentication,
        Model model) {
        String username = authentication.getName();
        UserProfile userProfile;
        try {
            userProfile = userProfileService.findOne(username);
        } catch (NullPointerException e) {
            throw new UserNotFoundException();
        }
        model.addAttribute("userProfile", userProfile);
        return "user";
    }


    @RequestMapping(value = "/myprofile/delete", method = RequestMethod.GET)
    public String deleteProfile(Authentication authentication) {
        return authentication.getName() == null ? "redirect:/" : "user_confirm_delete";
    }

    @RequestMapping(value = "/myprofile/delete", method = RequestMethod.POST)
    public String processAndDeleteProfileAndLogout(@ModelAttribute String password,
        Authentication authentication,
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes model) {
        if (authentication.getName() == null) {
            return "redirect:/";
        }
        User user = userService.findByUsername(authentication.getName());
        userService.remove(user, password);
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        model.addFlashAttribute("message", "user.account.deleted");
        return "redirect:/";
    }


    @RequestMapping(value = "/myprofile/edit", method = RequestMethod.GET)
    public String editMode(Authentication authentication,
        Model model) {
        UserProfile userProfile;
        String username = authentication.getName();
        if (username == null) {
            return "redirect:/";
        }
        try {
            userProfile = userProfileService.findOne(username);
        } catch (NullPointerException e) {
            throw new UserNotFoundException();
        }

        model.addAttribute("userProfile", userProfile);
        model.addAttribute("userEditForm", new UserEditForm());
        return "user_edit_form";
    }

    @RequestMapping(value = "/myprofile/edit/picture", method = RequestMethod.POST)
    public String processAndSaveProfilePicture(@RequestPart MultipartFile profilePicture,
        HttpServletRequest request,
        Authentication authentication,
        RedirectAttributes redirectModel) {
        if (authentication.getName() == null) {
            return "redirect:/login";
        }
        if (profilePicture.isEmpty()) {
            return "redirect:/myprofile";
        }
        User user = userService.findByUsername(authentication.getName());
        try {
            String path =
                request.getSession().getServletContext().getRealPath("/resources/public/img/pp/");
            profilePicture.transferTo(new File(path + user.getId() + ".jpg"));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        userService.save(user);
        redirectModel.addFlashAttribute("message", "user.picture.successfully.saved");
        return "redirect:/myprofile";
    }

    
    @RequestMapping(value = "/myprofile/edit", method = RequestMethod.POST)
    public String processAndSaveProfileDetails(
        HttpServletRequest request,
        Authentication authentication,
        RedirectAttributes redirectModel) {
        if (authentication.getName() == null) {
            return "redirect:/login";
        }
        
        User user = userService.findByUsername(authentication.getName());
        UserInfo userinfo = new UserInfo();
       
        user.getInfo().setName(request.getParameter("name"));
        user.getInfo().setLastName(request.getParameter("lastName"));
        user.getInfo().setCity(request.getParameter("city"));
        SimpleDateFormat smdtf= new SimpleDateFormat("yyyy-MM-dd");
        try {
        	System.out.println("birthday"+request.getParameter("birthday"));
			user.getInfo().setBirthday(smdtf.parse(request.getParameter("birthday")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        user.getInfo().setFooter(request.getParameter("footer"));
        user.getInfo().setAboutMe(request.getParameter("biography"));
        userService.save(user);
        return "redirect:/myprofile";
    }
    
}
