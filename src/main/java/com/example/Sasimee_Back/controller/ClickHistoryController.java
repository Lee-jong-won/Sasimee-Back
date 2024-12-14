package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.service.ClickHistoryService;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clickHistory")
@RequiredArgsConstructor
@Tag(name = "클릭 기록 관리", description="클릭 기록 관리를 위한 api들")
public class ClickHistoryController {
    private final ClickHistoryService clickHistoryService;
    private final PostService postService;

    @PostMapping("/save/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "클릭 기록 저장 성공"),
            @ApiResponse(responseCode = "400", description = "클릭 기록 저장 실패")
    })
    public ResponseEntity<Void> saveClickHistory(@PathVariable Long postId, @AuthenticationPrincipal SasimeePrincipal sasimeePrincipal) {

            clickHistoryService.saveClickHistory(sasimeePrincipal.getUseremail(), postId);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "클릭 기록 기반 게시글들 추천 성공"),
            @ApiResponse(responseCode = "400", description = "클릭 기록 기반 게시글들 추천 실패")
    })
    public ResponseEntity<Object> recommendByHistory(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal) {
            try{
                List<String> topTags = clickHistoryService.recommender(sasimeePrincipal.getUseremail());
                PostDTO.getAllPostResponse posts = postService.getPostByTag(topTags.get(0));
                return ResponseEntity.ok(posts);
            }catch (Exception e){
                return ResponseEntity.status(400).body("추천 게시글을 가져오지 못했습니다." + e.getMessage());
            }
    }
}
