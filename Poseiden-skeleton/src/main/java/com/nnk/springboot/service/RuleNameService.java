package com.nnk.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

import java.util.List;

@Service
public class RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    public void save(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }
    
    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id).orElse(null);
    }
    
    public void updateRuleName(Integer id, RuleName updatedRuleName) {
        RuleName existingRuleName = ruleNameRepository.findById(id).orElse(null);
        if (existingRuleName != null) {
            // Mettre à jour les propriétés de existingRuleName avec celles de updatedRuleName
            existingRuleName.setName(updatedRuleName.getName());
            existingRuleName.setDescription(updatedRuleName.getDescription());
            existingRuleName.setJson(updatedRuleName.getJson());
            existingRuleName.setTemplate(updatedRuleName.getTemplate());
            existingRuleName.setSqlStr(updatedRuleName.getSqlStr());
            existingRuleName.setSqlPart(updatedRuleName.getSqlPart());

            // Enregistrer l'objet mis à jour dans la base de données
            ruleNameRepository.save(existingRuleName);
        }
        
        
    }
    
    public void deleteById(Integer id) {
        ruleNameRepository.deleteById(id);
    }

}
