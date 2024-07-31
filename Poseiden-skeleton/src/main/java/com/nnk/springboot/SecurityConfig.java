package com.nnk.springboot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



import com.nnk.springboot.service.CustomUserDetailsService;
//import com.nnk.springboot.validation.PasswordConstraintValidator;


@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	@Autowired
    private CustomUserDetailsService userDetailsService;

	
	/**
	 * Configure la chaîne de filtres de sécurité pour l'application.
	 * Cette méthode définit les règles de sécurité pour les différentes requêtes HTTP, configure le processus de connexion
	 * et de déconnexion, et associe le service de détails utilisateur pour l'authentification.
	 * Elle spécifie quels chemins sont accessibles sans authentification, les chemins restreints aux utilisateurs ayant le rôle ADMIN,
	 * et exige une authentification pour tous les autres chemins.
	 *
	 * @param http L'objet HttpSecurity pour configurer les composants de la sécurité web.
	 * @return La chaîne de filtres de sécurité configurée.
	 * @throws Exception Si une erreur survient durant la configuration.
	 */

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		
		
		http
		
	    .authorizeHttpRequests(authz -> authz
	        .requestMatchers("/register", "/login", "/home", "/bidList/list", "/bidList/add", "/bidList/validate").permitAll() // Autoriser sans authentification
	        .requestMatchers("/admin/**").hasRole("ADMIN") // Accès restreint aux administrateurs
	        .requestMatchers("/**").authenticated() // Tous les autres URL nécessitent une authentification
	    )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .loginProcessingUrl("/login")
	            .usernameParameter("username") 
	            .passwordParameter("password")
	            .defaultSuccessUrl("/homeW", true)
	            .failureUrl("/login?error=true")
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout=true")
	            .permitAll()
	        )
	        .userDetailsService(userDetailsService);

	    return http.build();
	}

	 
	/**
	 * Crée et retourne un encodeur de mot de passe pour l'application.
	 * Cette méthode instancie un BCryptPasswordEncoder qui est utilisé pour encoder et vérifier les mots de passe
	 * dans l'application. BCrypt est un algorithme de hachage robuste pour le hashage de mot de passe.
	 *
	 * @return Un encodeur de mot de passe BCryptPasswordEncoder.
	 */

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	  /* @Bean
	    public PasswordConstraintValidator passwordConstraintValidator() {
	        return new PasswordConstraintValidator();
	    }*/
}
