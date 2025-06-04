package com.example.Sasimee_Back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Sasimee_Back.entity.Recommend;


public interface RecommendationRepository extends JpaRepository<Recommend, Long>{
    List<Recommend> findById(long postId);

    long countByPostId(long postId);

    Recommend findTopByPostIdOrderByIdAsc(long postId);
}