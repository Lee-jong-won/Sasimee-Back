package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.service.ClickHistoryService;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "클릭 기록", description = "사용자의 클릭 기록을 관리하는 API들")
@RestController
@RequestMapping("/clickHistory")
@RequiredArgsConstructor
public class ClickHistoryController {
    private final ClickHistoryService clickHistoryService;
    private final PostService postService;

    @Operation(summary = "클릭 기록 저장", description = "해당 유저가 클릭한 게시글을 저장한다.")
    @PostMapping("/save/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "클릭 기록 저장 성공"),
            @ApiResponse(responseCode = "400", description = "클릭 기록 저장 실패")
    })
    public ResponseEntity<Void> saveClickHistory(@PathVariable Long postId, @RequestParam Long userId) {

            clickHistoryService.saveClickHistory(userId, postId);
            return ResponseEntity.ok().build();
    }

    @Operation(summary = "클릭 기록 기반 추천", description = "클릭 기록을 기반으로 태그를 검색하여 포스트들의 대략적인 정보를 가져온다.")
    @GetMapping("/recommend")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "클릭 기록 기반 추천 성공"),
            @ApiResponse(responseCode = "400", description = "클릭 기록 기반 추천 실패")
    })
    public ResponseEntity<Object> recommendByHistory(@RequestParam Long userId) {
            try{
                List<String> topTags = clickHistoryService.recommender(userId);
                PostDTO.getAllPostResponse posts = postService.getPostByTag(topTags.get(0));
                return ResponseEntity.ok(posts);
            }catch (Exception e){
                return ResponseEntity.status(400).body("추천 게시글을 가져오지 못했습니다." + e.getMessage());
            }
    }
}
