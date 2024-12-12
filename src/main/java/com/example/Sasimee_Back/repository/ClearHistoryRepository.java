package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.ClearHistory;
import com.example.Sasimee_Back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClearHistoryRepository extends JpaRepository<ClearHistory, Long> {
    List<ClearHistory> findByUser(User user);
}
