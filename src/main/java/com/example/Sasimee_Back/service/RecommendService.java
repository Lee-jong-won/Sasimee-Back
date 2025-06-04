package com.example.Sasimee_Back.service;

import org.springframework.stereotype.Service;

import com.example.Sasimee_Back.entity.Recommend;
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
            Recommend oldest = recommendationRepository.findTopByPostIdOrderByIdAsc(postId);
            recommendationRepository.delete(oldest);
        }

        Recommend newRecommendation = Recommend.builder()
                .postId(postId)
                .similarPostId(recommendationId)
                .similarityScore(similarity)
            .build();
        
        recommendationRepository.save(newRecommendation);
    }
}
