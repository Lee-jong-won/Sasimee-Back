package com.example.Sasimee_Back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Sasimee_Back.entity.Recommend;


public interface RecommendRepository extends JpaRepository<Recommend, Long>{
    List<Recommend> findByPostId(long post_id);
}