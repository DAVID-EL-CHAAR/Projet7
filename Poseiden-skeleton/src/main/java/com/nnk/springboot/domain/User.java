package com.nnk.springboot.domain;
/*
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
*/
import java.util.HashSet;
//import javax.validation.constraints.Pattern;
import java.util.Set;

//import com.nnk.springboot.validation.ValidPassword;
//import javax.validation.constraints.Pattern;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    //@NotBlank(message = "Username is mandatory")
    private String username;
    
    
    
   // @NotBlank(message = "password is mandatory")
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$",
   // message = "password must contain at least 1 uppercase, 1 digit, 1 special character and 8 characters")
    //@com.nnk.springboot.ValidPassword
    private String password;
    //@NotBlank(message = "FullName is mandatory")
    private String fullname;

    //@NotBlank(message = "Role is mandatory")
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
