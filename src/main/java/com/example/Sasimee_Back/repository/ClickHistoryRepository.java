package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.ClickHistory;
import com.example.Sasimee_Back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClickHistoryRepository extends JpaRepository<ClickHistory, Long> {
    List<ClickHistory> findByUser(User user);
}
