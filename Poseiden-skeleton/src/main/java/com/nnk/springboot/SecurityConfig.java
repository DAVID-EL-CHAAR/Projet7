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

	 

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	  /* @Bean
	    public PasswordConstraintValidator passwordConstraintValidator() {
	        return new PasswordConstraintValidator();
	    }*/
}
