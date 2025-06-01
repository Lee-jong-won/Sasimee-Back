package com.example.Sasimee_Back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Sasimee_Back.entity.Recommendation;


public interface RecommendationRepository extends JpaRepository<Recommendation, Long>{
    List<Recommendation> findById(long postId);

    long countByPostId(long postId);

    Recommendation findTopByPostIdOrderByIdAsc(long postId);
}