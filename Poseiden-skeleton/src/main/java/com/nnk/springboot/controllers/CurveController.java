package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import jakarta.validation.Valid;
import com.nnk.springboot.service.CurvePointService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CurveController {
    
	@Autowired
	private CurvePointService curvePointService;

  
	
	 @RequestMapping("/curvePoint/list")
	    public String home(Model model, HttpServletRequest request) {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    boolean isAdmin = auth.getAuthorities().stream()
		                          .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		    
		    
	        
	        List<CurvePoint> allCurvePoints = curvePointService.findAll();

	        model.addAttribute("curvePoints", allCurvePoints);
	        model.addAttribute("httpServletRequest", request); 
	        model.addAttribute("isAdmin", isAdmin);

	        return "curvePoint/list";
	    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }
    

   
    
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            FieldError termError = result.getFieldError("term");
            if (termError != null && "typeMismatch".equals(termError.getCode())) {
                model.addAttribute("termError", "La valeur saisie pour le terme doit être un nombre.");
            }
            return "curvePoint/add";
        }

        curvePointService.saveCurvePoint(curvePoint);
        return "redirect:/curvePoint/list";
    }




    
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.findById(id);
        if (curvePoint == null) {
            // Gérer le cas où le CurvePoint n'est pas trouvé
            return "redirect:/curvePoint/list";
        }
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            FieldError termError = result.getFieldError("term");
            if (termError != null && "typeMismatch".equals(termError.getCode())) {
                model.addAttribute("termError", "La valeur saisie pour le terme doit être un nombre.");
            }
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        }

        curvePointService.updateCurvePoint(id, curvePoint);
        return "redirect:/curvePoint/list";
    }


   
    
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }

}
