package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByEmail(String email);
    @Query("select u from User u where u.email = :email")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findFirstForUpdateByEmail(@Param("email") String email);
}
