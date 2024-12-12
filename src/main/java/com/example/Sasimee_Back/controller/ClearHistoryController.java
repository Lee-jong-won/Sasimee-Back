package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.service.ClearHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clearHistory")
@RequiredArgsConstructor
public class ClearHistoryController {
    private final ClearHistoryService clearHistoryService;

    @PostMapping("/save/{postId}")
    public ResponseEntity<Void> saveClearHistory(@AuthenticationPrincipal User user, @PathVariable Long postId) {
        clearHistoryService.saveClearHistory(user.getId(), postId);
        return ResponseEntity.ok().build();
    }
}
