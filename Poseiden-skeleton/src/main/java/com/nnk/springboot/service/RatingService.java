package com.nnk.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }
    
    public Rating findById(Integer id) {
        return ratingRepository.findById(id).orElse(null);
    }
    
    public Rating updateRating(Integer id, Rating updatedRating) {
        Optional<Rating> existingRatingOptional = ratingRepository.findById(id);
        if (existingRatingOptional.isPresent()) {
            Rating existingRating = existingRatingOptional.get();

            // Mettre à jour les propriétés de existingRating avec celles de updatedRating
            existingRating.setMoodysRating(updatedRating.getMoodysRating());
            existingRating.setSandPRating(updatedRating.getSandPRating());
            existingRating.setFitchRating(updatedRating.getFitchRating());
            existingRating.setOrderNumber(updatedRating.getOrderNumber());

            // Enregistrer l'objet mis à jour dans la base de données
            return ratingRepository.save(existingRating);
        }
        return null;
    }
    
    public void deleteById(Integer id) {
        ratingRepository.deleteById(id);
    }


}
