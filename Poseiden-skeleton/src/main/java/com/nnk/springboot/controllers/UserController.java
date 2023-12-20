package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserDto;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.service.UserMapper;
import com.nnk.springboot.service.UserService;
import org.springframework.validation.annotation.Validated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/register")
    public String showRegistrationForm( Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
    	logger.info("Méthode handleFormSubmit appelée");
    	if (result.hasErrors()) {
        	logger.error("Erreurs de validation détectées :");
            result.getAllErrors().forEach(error -> logger.error(error.toString()));
        	
            return "register";
        }
        try {
            userService.registerNewUserAccount(user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

   /*
    @RequestMapping("admin/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }
    */
    
    @RequestMapping("admin/user/list")
    public String adminUserList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Récupère le nom d'utilisateur

        User user = userService.findByUsername(username); // Utilisez votre service pour obtenir les détails de l'utilisateur

        if (!"ADMIN".equals(user.getRole())) {
            throw new AccessDeniedException("Accès refusé : Vous n'êtes pas autorisé à accéder à cette page.");
        }

        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(/*User bid*/ Model model) {
    	model.addAttribute("user", new UserDto());
        return "user/add";
    }
/*
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            model.addAttribute("users", userRepository.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }*/
    
    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        User OptionalUser = userService.getUserByUsername(userDto.getUsername());
        if (OptionalUser != null) {
            result.rejectValue("username", null, "This username is already used");
        }
        if (!result.hasErrors()) {
            User user = UserMapper.convertUserDtoToUser(userDto);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            model.addAttribute("users", UserMapper.convertUserListToUserDtoList(userService.getUsers()));
            return "redirect:/user/list";
        }
        model.addAttribute("user", userDto);
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }
}
