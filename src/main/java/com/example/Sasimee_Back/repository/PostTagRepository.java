package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.PostTag;
import com.example.Sasimee_Back.entity.Tag;
import com.example.Sasimee_Back.entity.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    Optional<PostTag> findByNameAndCategory(String name, TagCategory category);
}
