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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    
    /**
     * Affiche le formulaire d'inscription.
     * Cette méthode gère la requête pour afficher le formulaire d'inscription des utilisateurs.
     * Elle initialise un nouvel objet utilisateur vide et l'ajoute au modèle, qui est ensuite utilisé
     * pour lier les données du formulaire lors de l'envoi.
     *
     * @param model Le modèle de l'interface utilisateur pour ajouter des attributs pour la vue.
     * @return Le nom de la vue à rendre pour la page d'inscription.
     */
    @GetMapping("/register")
    public String showRegistrationForm( Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Traite le formulaire d'inscription de l'utilisateur.
     * Cette méthode gère la soumission du formulaire d'inscription. Elle valide l'objet utilisateur reçu
     * et, si la validation échoue, renvoie le formulaire avec les erreurs. Si la validation réussit, elle tente
     * d'enregistrer le nouveau compte utilisateur. En cas d'erreur lors de l'enregistrement (comme un nom d'utilisateur
     * déjà pris), un message d'erreur est ajouté au modèle.
     *
     * @param user L'objet utilisateur à enregistrer.
     * @param result Le BindingResult qui contient les résultats de la validation et les erreurs éventuelles.
     * @param model Le modèle de l'interface utilisateur pour ajouter des attributs pour la vue.
     * @return Le nom de la vue à rendre ou une redirection vers la page de connexion en cas de succès.
     */
    /*
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
    }*/
    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // Retour à la page d'inscription si erreurs de validation
        }

        // Vérifier si le nom d'utilisateur existe déjà
        if (userService.getUserByUsername(userDto.getUsername()) != null) {
            result.rejectValue("username", null, "This username is already used");
            return "register";
        }

        // Convertir UserDto en User
        User user = UserMapper.convertUserDtoToUser(userDto);

        // Encoder le mot de passe
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(userDto.getPassword()));

        user.setRole("USER");
        // Enregistrer l'utilisateur
        userService.saveUser(user);

        // Redirection ou autre logique
        return "redirect:/login"; // Rediriger vers une page de succès, par exemple
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
    public String adminUserList(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Récupère le nom d'utilisateur

        User user = userService.findByUsername(username); // Utilisez votre service pour obtenir les détails de l'utilisateur

        if (!"ADMIN".equals(user.getRole())) {
            throw new AccessDeniedException("Accès refusé : Vous n'êtes pas autorisé à accéder à cette page.");
        }

        model.addAttribute("httpServletRequest", request); 
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @GetMapping("admin/user/add")
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
    
    @PostMapping("admin/user/validate")
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
            return "redirect:/admin/user/list";
        }
        model.addAttribute("user", userDto);
        return "user/add";
    }

    @GetMapping("admin/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

   /* @PostMapping("admin/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        //userRepository.save(user);
        userService.saveUser(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/admin/user/list";
    }*/
    
    @PostMapping("/admin/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") UserDto userDto, 
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        // Vérifier si le nom d'utilisateur existe déjà et s'il est différent de l'utilisateur actuel
        User existingUser = userService.getUserByUsername(userDto.getUsername());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            result.rejectValue("username", null, "This username is already used");
            return "user/update";
        }

        // Convertir UserDto en User
        User userToUpdate = UserMapper.convertUserDtoToUser(userDto);
        userToUpdate.setId(id); // Conserver l'ID de l'utilisateur

        // Encoder le mot de passe
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userToUpdate.setPassword(encoder.encode(userDto.getPassword()));

        // Sauvegarder l'utilisateur mis à jour
        userService.saveUser(userToUpdate);
        model.addAttribute("users", UserMapper.convertUserListToUserDtoList(userService.getUsers()));
        return "redirect:/admin/user/list";
    }


    @GetMapping("admin/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/admin/user/list";
    }
}
