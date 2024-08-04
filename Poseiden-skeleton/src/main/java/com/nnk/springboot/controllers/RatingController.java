package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import jakarta.validation.Valid;

@Controller
public class RatingController {
   
	

    @Autowired
    private RatingService ratingService;

 
    @RequestMapping("/rating/list")
    public String home(Model model, HttpServletRequest request) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    boolean isAdmin = auth.getAuthorities().stream()
	                          .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        List<Rating> allRatings = ratingService.findAll();

        model.addAttribute("ratings", allRatings);
        model.addAttribute("httpServletRequest", request);
        model.addAttribute("isAdmin", isAdmin);

        return "rating/list";
    }


  
    
    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }

        // Enregistrer le rating dans la base de données
        ratingService.save(rating);

        return "redirect:/rating/list";
    }


   
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.findById(id);
        if (rating == null) {
            
            return "redirect:/rating/list";
        }
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rating", rating);
            return "rating/update";
        }

        ratingService.updateRating(id, rating);
        return "redirect:/rating/list";
    }


    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteById(id);
        return "redirect:/rating/list";
    }

}
