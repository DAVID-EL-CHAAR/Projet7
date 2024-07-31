package com.nnk.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;

import java.util.List;


import org.springframework.stereotype.Service;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Enregistre un nouveau compte utilisateur.
     * Cette méthode transactionnelle crée et enregistre un nouveau compte utilisateur dans la base de données.
     * Elle vérifie d'abord si un utilisateur avec le même nom d'utilisateur existe déjà. Si c'est le cas, elle lance
     * une exception. Sinon, elle crée un nouvel utilisateur, encode le mot de passe et définit le rôle par défaut.
     * Enfin, elle enregistre l'utilisateur dans la base de données.
     *
     * @param userDto L'objet utilisateur contenant les données du nouvel utilisateur à enregistrer.
     * @return L'objet User enregistré avec les détails persistés.
     * @throws RuntimeException Si un utilisateur avec le même nom d'utilisateur existe déjà.
     */

    @Transactional
    public User registerNewUserAccount(User userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new RuntimeException("il y a un compte avec ce username email " + userDto.getUsername());
        }
        User user = new User();
        user.setFullname(userDto.getFullname()); 
        user.setUsername(userDto.getUsername()); 
        //user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPassword(userDto.getPassword());
        // user.setEnabled(true); 
        user.setRole("USER");
        return userRepository.save(user);
    }
    
   



    public User findByUsername(String email) {
        return userRepository.findByUsername(email);
    }
    
    public void saveUser (User user){
        userRepository.save(user);
    }
    
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    
    public User getUserByUsername (String username) {
        return userRepository.findByUsername(username);
    }
    
}
