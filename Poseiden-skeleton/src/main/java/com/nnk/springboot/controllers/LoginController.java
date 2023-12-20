package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@RequestMapping("app")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    /*
    @GetMapping("login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }
    */

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    

    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }
    @GetMapping("error")
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String errorMessage = "You are not authorized for the requested data.";

        mav.addObject("errorMsg", errorMessage);
        mav.addObject("httpServletRequest", request);
        mav.setViewName("403");

        return mav;
        
    }
}
    
    
