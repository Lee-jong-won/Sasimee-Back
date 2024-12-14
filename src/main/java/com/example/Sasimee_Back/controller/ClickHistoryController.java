package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.service.ClickHistoryService;
import com.example.Sasimee_Back.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clickHistory")
@RequiredArgsConstructor
public class ClickHistoryController {
    private final ClickHistoryService clickHistoryService;
    private final PostService postService;

    @PostMapping("/save/{userId}/{postId}")
    public ResponseEntity<Void> saveClickHistory(@PathVariable Long postId, @PathVariable Long userId) {

            clickHistoryService.saveClickHistory(userId, postId);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<Object> recommendByHistory(@PathVariable Long userId) {
            try{
                List<String> topTags = clickHistoryService.recommender(userId);
                PostDTO.getAllPostResponse posts = postService.getPostByTag(topTags.get(0));
                return ResponseEntity.ok(posts);
            }catch (Exception e){
                return ResponseEntity.status(400).body("추천 게시글을 가져오지 못했습니다." + e.getMessage());
            }
    }
}
