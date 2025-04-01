package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.argumentResolver.JwtAuthentication;
import com.example.Sasimee_Back.authentication.User;
import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.entity.PostType;
import com.example.Sasimee_Back.service.ClickHistoryService;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/offers")
@RestController
@RequiredArgsConstructor
@Tag(name = "추천 시스템", description = "추천 시스템 관련 API 입니다.")
public class RecommendationController {

    private final ClickHistoryService clickHistoryService;
    private final PostService postService;

    @Operation(summary = "설문형 게시글 추천", description = "게시글 클릭 기반으로 추천을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "클릭 기반 설문형 게시글 추천 실패"),
            @ApiResponse(responseCode = "200", description = "클릭 기반 설문형 게시글 추천 성공")
    })

    @User
    @GetMapping("/survey")
    public ResponseEntity<Object> surveyRecommendation(@JwtAuthentication String email){
        PostType postType = PostType.S;
        try{
            List<String> topTags = clickHistoryService.recommender(email);
            PostDTO.getAllPostResponse posts = postService.getPostByTag(topTags.get(0), postType);
            return ResponseEntity.ok(posts);
        }catch (Exception e){
            return ResponseEntity.status(400).body("설문형 추천 게시글을 가져올 수 없었습니다." + e.getMessage());
        }
    }

    @Operation(summary = "수행형 게시글 추천", description = "게시글 클릭 기반으로 추천을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "클릭 기반 수행형 게시글 추천 실패"),
            @ApiResponse(responseCode = "200", description = "클릭 기반 수행형 게시글 추천 성공")
    })

    @User
    @GetMapping("/task")
    public ResponseEntity<Object> taskRecommendation(@JwtAuthentication String email){
        PostType postType = PostType.T;
        try{
            List<String> topTags = clickHistoryService.recommender(email);
            PostDTO.getAllPostResponse posts = postService.getPostByTag(topTags.get(0), postType);
            return ResponseEntity.ok(posts);
        }catch (Exception e){
            return ResponseEntity.status(400).body("수행형 추천 게시글을 가져올 수 없었습니다." + e.getMessage());
        }
    }
}
