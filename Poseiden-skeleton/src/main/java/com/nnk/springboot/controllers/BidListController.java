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
    
	@Autowired
	private BidListService bidListService;
	
	

	/**
	 * Gère la requête pour la liste des offres (bids).
	 * Cette méthode récupère la liste des offres en utilisant un service dédié, détermine si l'utilisateur
	 * courant a un rôle d'administrateur, et ajoute ces informations au modèle pour la vue.
	 * 
	 * @param model Le modèle de l'interface utilisateur utilisé pour ajouter des attributs qui seront rendus dans la vue.
	 * @param request L'objet HttpServletRequest représentant les informations de la requête HTTP.
	 * @return Le nom de la vue à rendre, dans ce cas, la liste des offres.
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

	/**
	 * Affiche le formulaire pour ajouter une nouvelle offre.
	 * Cette méthode renvoie simplement le nom de la vue qui contient le formulaire pour ajouter une offre.
	 * 
	 * @param bid L'objet BidList qui sera utilisé dans le formulaire pour soumettre une nouvelle offre.
	 *            Il est généralement initialisé automatiquement par Spring lors de l'affichage du formulaire.
	 * @return Le nom de la vue à rendre, dans ce cas, le formulaire d'ajout d'une offre.
	 */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }
  
    
    /**
     * Traite la soumission du formulaire d'ajout ou de mise à jour d'une offre.
     * Cette méthode valide l'objet BidList reçu et, si la validation échoue, renvoie la vue du formulaire avec les erreurs.
     * Si la validation réussit, l'offre est enregistrée et l'utilisateur est redirigé vers la liste des offres.
     * 
     * @param bid L'objet BidList à valider et à enregistrer.
     * @param result Le BindingResult qui contient les résultats de la validation et les erreurs éventuelles.
     * @param model Le modèle de l'interface utilisateur pour ajouter des attributs pour la vue.
     * @return Le nom de la vue à rendre ou une redirection vers la liste des offres.
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
    /**
     * Affiche le formulaire de mise à jour pour une offre spécifique.
     * Cette méthode recherche une offre par son identifiant. Si elle est trouvée, l'offre est ajoutée au modèle et la vue de mise à jour est retournée.
     * Dans le cas contraire, un message d'erreur est ajouté au modèle ou une redirection est effectuée.
     * 
     * @param id L'identifiant de l'offre à mettre à jour.
     * @param model Le modèle de l'interface utilisateur pour ajouter des attributs pour la vue.
     * @return Le nom de la vue à rendre ou une redirection si l'offre n'est pas trouvée.
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // je  Récupérer le BidList par id
        Optional<BidList> bidListOptional = bidListService.findById(id);

        if (bidListOptional.isPresent()) {
            // on ajoute bidList au modèle si présent
            model.addAttribute("bidList", bidListOptional.get());
        } else {
            
            model.addAttribute("errorMessage", "BidList not found");
            return "redirect:/bidList/list"; // 
        }

        return "bidList/update";
    }

    
  
    
    /**
     * Traite la requête de mise à jour d'une offre spécifique.
     * Cette méthode valide l'objet BidList modifié et, si la validation échoue, renvoie à la vue de mise à jour avec les erreurs.
     * Si la validation réussit, l'offre est mise à jour dans la base de données et l'utilisateur est redirigé vers la liste des offres.
     *
     * @param id L'identifiant de l'offre à mettre à jour.
     * @param bidList L'objet BidList modifié à valider et à enregistrer.
     * @param result Le BindingResult qui contient les résultats de la validation et les erreurs éventuelles.
     * @param model Le modèle de l'interface utilisateur pour ajouter des attributs pour la vue.
     * @return Le nom de la vue à rendre ou une redirection vers la liste des offres.
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

    
  
    
    /**
     * Traite la requête de suppression d'une offre spécifique.
     * Cette méthode appelle le service pour supprimer l'offre par son identifiant.
     * Après la suppression, elle redirige l'utilisateur vers la liste des offres.
     *
     * @param id L'identifiant de l'offre à supprimer.
     * @param model Le modèle de l'interface utilisateur pour ajouter des attributs pour la vue (non utilisé dans cette méthode).
     * @return Une redirection vers la liste des offres après la suppression de l'offre.
     */
    
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // j'ai Appeler le service pour supprimer le BidList par son ID
        bidListService.deleteById(id);

        // un return Rediriger vers la liste des BidList après la suppression
        return "redirect:/bidList/list";
    }

}
