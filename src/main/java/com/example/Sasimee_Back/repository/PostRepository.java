package com.example.Sasimee_Back.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Sasimee_Back.entity.Post;
import com.example.Sasimee_Back.entity.PostType;
import com.example.Sasimee_Back.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByType(PostType postType, Pageable pageable);
    List<Post> findByUser(User user);
    List<Post> findByTagsNameAndType(String tagName, PostType postType);
}
