package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.TagCategory;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.entity.UserTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    Optional<UserTag> findByNameAndCategory(String name, TagCategory category);
    @Query("SELECT ut FROM UserTag ut JOIN ut.users u WHERE u = :user")
    List<UserTag> findByUser(@Param("user") User user);
}
