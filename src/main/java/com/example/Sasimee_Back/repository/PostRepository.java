package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTagsName(String tagName);
}
