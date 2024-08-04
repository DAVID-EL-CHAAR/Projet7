package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;

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
public class TradeController {
   
	
	@Autowired
    private TradeService tradeService;

	
	

    @RequestMapping("/trade/list")
    public String home(Model model, HttpServletRequest request) {
    	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    boolean isAdmin = auth.getAuthorities().stream()
		                          .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        // JE Récupérer tous les trades depuis le service
        List<Trade> allTrades = tradeService.findAll();

        // Ajouter les trades et la requête HTTP au modèle pour Thymeleaf
        model.addAttribute("trades", allTrades);
        model.addAttribute("httpServletRequest", request); 
        model.addAttribute("isAdmin", isAdmin);
        return "trade/list";
    }

   
    
    @GetMapping("/trade/add")
    public String addTradeForm(Model model) {
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }

        //  Enregistrer le trade dans la base de données
        tradeService.save(trade);

        return "redirect:/trade/list";
    }

    
    
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.findById(id);
        if (trade == null) {
            // JE Gérer le cas où le Trade n'est pas trouvé
            return "redirect:/trade/list";
        }
        model.addAttribute("trade", trade);
        return "trade/update";
    }
    
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trade", trade);
            return "trade/update";
        }

        tradeService.updateTrade(id, trade);
        return "redirect:/trade/list";
    }



    
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteById(id);
        return "redirect:/trade/list";
    }

}
