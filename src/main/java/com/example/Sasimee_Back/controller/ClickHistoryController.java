package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.entity.Post;
import com.example.Sasimee_Back.entity.PostType;
import com.example.Sasimee_Back.service.ClickHistoryService;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
@Tag(name = "클릭 기록 관리", description="클릭 기록 관리를 위한 api들")
public class ClickHistoryController {
    private final ClickHistoryService clickHistoryService;
    private final PostService postService;

    @Operation(summary = "유저 클릭 기록 저장", description = "게시글 ID를 통한 유저 클릭 기록 저장")
    @PostMapping("/save/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "클릭 기록 저장 성공"),
            @ApiResponse(responseCode = "400", description = "클릭 기록 저장 실패")
    })
    public ResponseEntity<Void> saveClickHistory(@PathVariable Long postId, @AuthenticationPrincipal SasimeePrincipal sasimeePrincipal) {

            clickHistoryService.saveClickHistory(sasimeePrincipal.getUsername(), postId);
            return ResponseEntity.ok().build();
    }

    @Operation(summary = "설문형 게시글 추천", description = "게시글 클릭 기록 기반 게시글 추천")
    @GetMapping("/get/survey")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "클릭 기록 기반 게시글들 추천 성공"),
            @ApiResponse(responseCode = "400", description = "클릭 기록 기반 게시글들 추천 실패")
    })
    public ResponseEntity<Object> recommendSurvey(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @RequestParam(defaultValue = "3") int limit) {
        PostType postType = PostType.S;
            try{
                List<String> topTags = clickHistoryService.recommender(sasimeePrincipal.getUsername());

                if(topTags.isEmpty()){
                    return ResponseEntity.status(200).body("추천할 태그가 없습니다.");
                }

                List<PostDTO.getAllPostResponse.PostSummary> posts = new ArrayList<>();
                for (String topTag : topTags) {
                    PostDTO.getAllPostResponse tagPost = postService.getPostByTag(topTag, postType);
                    posts.addAll(tagPost.getPosts());
                }

                List<PostDTO.getAllPostResponse.PostSummary> limitedPosts = posts.stream()
                        .distinct()
                        .limit(limit)
                        .toList();

                if(limitedPosts.isEmpty()){
                    return ResponseEntity.status(200).body("추천할 게시글이 없습니다.");
                }

                PostDTO.getAllPostResponse response = PostDTO.getAllPostResponse.builder()
                        .posts(limitedPosts)
                        .build();

                return ResponseEntity.ok(response);
            }catch (Exception e){
                return ResponseEntity.status(400).body("추천 게시글을 가져오지 못했습니다." + e.getMessage());
            }
    }

    @Operation(summary = "수행형 게시글 추천", description = "게시글 클릭 기록 기반 게시글 추천")
    @GetMapping("/get/task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "클릭 기록 기반 게시글들 추천 성공"),
            @ApiResponse(responseCode = "400", description = "클릭 기록 기반 게시글들 추천 실패")
    })
    public ResponseEntity<Object> recommendTask(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @RequestParam(defaultValue = "3") int limit) {
        PostType postType = PostType.T;
        try{
            List<String> topTags = clickHistoryService.recommender(sasimeePrincipal.getUsername());

            if(topTags.isEmpty()){
                return ResponseEntity.status(200).body("추천할 태그가 없습니다.");
            }

            List<PostDTO.getAllPostResponse.PostSummary> posts = new ArrayList<>();
            for (String topTag : topTags) {
                PostDTO.getAllPostResponse tagPost = postService.getPostByTag(topTag, postType);
                posts.addAll(tagPost.getPosts());
            }

            List<PostDTO.getAllPostResponse.PostSummary> limitedPosts = posts.stream()
                    .distinct()
                    .limit(limit)
                    .toList();

            if(limitedPosts.isEmpty()){
                return ResponseEntity.status(200).body("추천할 게시글이 없습니다.");
            }

            PostDTO.getAllPostResponse response = PostDTO.getAllPostResponse.builder()
                    .posts(limitedPosts)
                    .build();

            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(400).body("추천 게시글을 가져오지 못했습니다." + e.getMessage());
        }
    }
}
