package com.nnk.springboot.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() {
        Rating rating = new Rating();
        ratingService.save(rating);
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    public void testFindAll() {
        when(ratingRepository.findAll()).thenReturn(Arrays.asList(new Rating(), new Rating()));
        assertEquals(2, ratingService.findAll().size());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        when(ratingRepository.findById(id)).thenReturn(Optional.of(new Rating()));
        assertNotNull(ratingService.findById(id));
    }

    @Test
    public void testUpdateRating() {
        Integer id = 1;
        Rating existingRating = new Rating();
        Rating updatedRating = new Rating();
        updatedRating.setMoodysRating("MoodysRating");
        updatedRating.setSandPRating("SandPRating");
        updatedRating.setFitchRating("FitchRating");
        updatedRating.setOrderNumber(1);

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(existingRating); // Simuler le retour de l'objet mis à jour

        Rating result = ratingService.updateRating(id, updatedRating);

        verify(ratingRepository, times(1)).save(existingRating);
        assertEquals("MoodysRating", existingRating.getMoodysRating());
        assertEquals("SandPRating", existingRating.getSandPRating());
        assertEquals("FitchRating", existingRating.getFitchRating());
        assertEquals(1, existingRating.getOrderNumber());
        assertNotNull(result); // Maintenant, result ne devrait pas être null
    }


    @Test
    public void testDeleteById() {
        Integer id = 1;
        ratingService.deleteById(id);
        verify(ratingRepository, times(1)).deleteById(id);
    }
}
