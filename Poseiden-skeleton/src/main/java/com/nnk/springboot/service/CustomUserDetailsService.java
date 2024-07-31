package com.nnk.springboot.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Charge les détails d'un utilisateur par son nom d'utilisateur.
     * Cette méthode est une implémentation requise par l'interface UserDetailsService de Spring Security.
     * Elle est utilisée pour récupérer les détails d'un utilisateur à partir de la base de données lors de l'authentification.
     * Si aucun utilisateur n'est trouvé avec le nom d'utilisateur fourni, une exception UsernameNotFoundException est lancée.
     * Cette méthode est également marquée comme transactionnelle en lecture seule, indiquant qu'elle ne modifie pas les données de la base de données.
     *
     * @param username Le nom d'utilisateur à rechercher dans la base de données.
     * @return UserDetails L'objet UserDetails contenant les informations de l'utilisateur trouvé.
     * @throws UsernameNotFoundException Si aucun utilisateur n'est trouvé avec le nom d'utilisateur spécifié.
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Convertir le rôle de l'utilisateur en une liste d'autorités
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), true, true, true, true, authorities);
    }


   

}

































