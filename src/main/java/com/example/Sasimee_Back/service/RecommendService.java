package com.example.Sasimee_Back.service;

import org.springframework.stereotype.Service;

import com.example.Sasimee_Back.entity.Recommendation;
import com.example.Sasimee_Back.repository.RecommendationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendationRepository recommendationRepository;

    @Transactional
    public void createRecommendation(long postId, long recommendationId, float similarity){
        long count = recommendationRepository.countByPostId(postId);

        if(count >= 3){
            Recommendation oldest = recommendationRepository.findTopByPostIdOrderByIdAsc(postId);
            recommendationRepository.delete(oldest);
        }

        Recommendation newRecommendation = Recommendation.builder()
            .postId(postId)
            .recommendId(recommendationId)
            .similarity(similarity)
            .build();
        
        recommendationRepository.save(newRecommendation);
    }
}
