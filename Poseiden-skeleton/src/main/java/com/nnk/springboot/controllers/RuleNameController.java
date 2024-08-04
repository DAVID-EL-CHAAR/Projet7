package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;

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
public class RuleNameController {
    // TODO: Inject RuleName service
	
	@Autowired
    private RuleNameService ruleNameService;

    
	
	  @RequestMapping("/ruleName/list")
	    public String home(Model model, HttpServletRequest request) {
		  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    boolean isAdmin = auth.getAuthorities().stream()
		                          .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
	        
	        List<RuleName> allRuleNames = ruleNameService.findAll();

	        
	        model.addAttribute("ruleNames", allRuleNames);
	        model.addAttribute("httpServletRequest", request); 
	        model.addAttribute("isAdmin", isAdmin);

	        return "ruleName/list";
	    }

 
    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }

        //  Enregistrer le ruleName dans la base de données
        ruleNameService.save(ruleName);

        return "redirect:/ruleName/list";
    }


    
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = ruleNameService.findById(id);
        if (ruleName == null) {
           
            return "redirect:/ruleName/list";
        }
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        }

        ruleNameService.updateRuleName(id, ruleName);
        return "redirect:/ruleName/list";
    }


   
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }

}
