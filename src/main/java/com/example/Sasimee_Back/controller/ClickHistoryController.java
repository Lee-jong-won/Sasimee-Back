package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.service.ClickHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clickHistory")
@RequiredArgsConstructor
public class ClickHistoryController {
    private final ClickHistoryService clickHistoryService;

    @PostMapping("/save/{postId}")
    public ResponseEntity<Void> saveClickHistory(@AuthenticationPrincipal User user, @PathVariable Long postId) {
        clickHistoryService.saveClickHistory(user.getId(), postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    public ResponseEntity<Object> recommendByHistory(@AuthenticationPrincipal User user) {
        Long userId = user.getId();

        try{
            List<String> topTags = clickHistoryService.recommender(userId);

            return ResponseEntity.ok(topTags);
        }catch (Exception e){
            return ResponseEntity.status(400).body("추천 태그를 가져오지 못했습니다." + e.getMessage());
        }
    }
}
