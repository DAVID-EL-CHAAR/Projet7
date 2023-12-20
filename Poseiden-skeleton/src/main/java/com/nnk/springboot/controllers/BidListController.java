package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.BidListService;

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
import java.util.Optional;

import jakarta.validation.Valid;


@Controller
public class BidListController {
    // TODO: Inject Bid service
	@Autowired
	private BidListService bidListService;
	
	

    /*
    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        // TODO: call service find all bids to show to the view
        return "bidList/list";
    }
    */
	@RequestMapping("/bidList/list")
	public String home(Model model, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    boolean isAdmin = auth.getAuthorities().stream()
	                          .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
	    
	    
	    List<BidList> allBids = bidListService.findAllBids();
	    model.addAttribute("bidLists", allBids);
	    model.addAttribute("httpServletRequest", request); 
	    model.addAttribute("isAdmin", isAdmin);
	    return "bidList/list";
	}

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }
   /*
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return bid list
        return "bidList/add";
    }
    */

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        
        bidListService.save(bid);
        return "redirect:/bidList/list"; // Assurez-vous que cette route existe
    }

    
  /*  @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
        return "bidList/update";
    }
    */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // je  Récupérer le BidList par id
        Optional<BidList> bidListOptional = bidListService.findById(id);

        if (bidListOptional.isPresent()) {
            // on ajoute bidList au modèle si présent
            model.addAttribute("bidList", bidListOptional.get());
        } else {
            // je dois plus tard Gérer le cas où le BidList n'est pas trouvé
            // Par exemple, avec un message d'erreur ou rediriger
            model.addAttribute("errorMessage", "BidList not found");
            return "redirect:/bidList/list"; // 
        }

        return "bidList/update";
    }

    
    
/*
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and return list Bid
        return "redirect:/bidList/list";
    }
 */
    
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }

        bidListService.update(bidList); // Mettre à jour le bidList

        return "redirect:/bidList/list";
    }

    
   /* @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        return "redirect:/bidList/list";
    } */
    
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // j'ai Appeler le service pour supprimer le BidList par son ID
        bidListService.deleteById(id);

        // un return Rediriger vers la liste des BidList après la suppression
        return "redirect:/bidList/list";
    }

}
