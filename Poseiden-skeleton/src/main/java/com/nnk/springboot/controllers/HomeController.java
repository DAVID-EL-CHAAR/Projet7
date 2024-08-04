package com.nnk.springboot.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import com.nnk.springboot.domain.User;

@Controller
public class HomeController
{
	
	@Autowired
    private UserService userService;
	
	
	
	@RequestMapping("/home")
	public String home(Model model) {
	   
	    return "home";
	}

	@RequestMapping("/homeW")
	public String homeW(Model model,Principal principal,HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    boolean isAdmin = auth.getAuthorities().stream()
	                          .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
	    
	    model.addAttribute("isAdmin", isAdmin);
	
	String email = principal.getName();
    
    // Trouve l'utilisateur par son email
    User user = userService.findByUsername(email);
    
    
    
    
        model.addAttribute("user", user);
        model.addAttribute("httpServletRequest", request); 
        model.addAttribute("isAdmin", isAdmin);
        return "homeW";
	}
	
	
	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		
		return "redirect:/bidList/list";
	}


}
