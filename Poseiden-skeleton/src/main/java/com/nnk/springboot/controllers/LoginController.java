package com.nnk.springboot.controllers;

import com.nnk.springboot.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    
    /**
     * Affiche la page de connexion.
     * Cette méthode gère la requête pour afficher la page de connexion de l'utilisateur.
     * Elle renvoie simplement le nom de la vue qui contient le formulaire de connexion.
     * Aucune logique d'authentification n'est implémentée ici car cette fonctionnalité est gérée
     * par Spring Security.
     *
     * @return Le nom de la vue à rendre pour la page de connexion.
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
    
    
