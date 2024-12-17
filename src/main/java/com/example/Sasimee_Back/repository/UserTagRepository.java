package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.TagCategory;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.entity.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    Optional<UserTag> findByNameAndCategory(String name, TagCategory category);
    List<UserTag> findByUser(User user);
}
