package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.service.ClearHistoryService;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clearHistory")
@RequiredArgsConstructor
@Tag(name = "수행 기록 관리", description="수행 기록 관리를 위한 api들")
public class ClearHistoryController {
    private final ClearHistoryService clearHistoryService;
    private final PostService postService;

    @PostMapping("/save/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수행한 게시글 저장 성공"),
            @ApiResponse(responseCode = "400", description = "수행한 게시글 저장 실패")
    })
    public ResponseEntity<Void> saveClearHistory(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @PathVariable Long postId) {
        clearHistoryService.saveClearHistory(sasimeePrincipal.getUseremail(), postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수행한 게시글들 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "수행한 게시글들 전체 조회 실패")
    })
    public ResponseEntity<PostDTO.getAllPostResponse> getPostHistory(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal) {
        PostDTO.getAllPostResponse response = postService.getPostByHistory(sasimeePrincipal.getUseremail());
        return ResponseEntity.ok(response);
    }
}
