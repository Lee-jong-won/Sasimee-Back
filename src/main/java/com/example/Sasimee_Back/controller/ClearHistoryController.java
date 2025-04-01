package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.argumentResolver.JwtAuthentication;
import com.example.Sasimee_Back.authentication.User;
import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.service.ClearHistoryService;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/completion")
@RequiredArgsConstructor
@Tag(name = "실험 참여 기록 관리", description = "실험 참여 기록을 관리하는 API들 입니다.")
public class ClearHistoryController {
    private final ClearHistoryService clearHistoryService;
    private final PostService postService;

    @Operation(summary = "수행한 실험 게시글 저장", description = "유저가 참여한 실험 기록을 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "실험 저장을 실패했습니다."),
            @ApiResponse(responseCode = "200", description = "실험 저장을 성공했습니다.")
    })

    @User
    @PostMapping("/{id}")
    public ResponseEntity<Void> saveClearHistory(@JwtAuthentication String email, @PathVariable Long id){
        clearHistoryService.saveClearHistory(email, id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수행한 실험 게시글 목록 조회", description = "유저가 참여한 실험 기록들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "실험 목록 조회를 실패하였습니다."),
            @ApiResponse(responseCode = "200", description = "실험 목록 조회에 성공하였습니다.")
    })

    @User
    @GetMapping
    public ResponseEntity<PostDTO.getAllPostResponse> getPostHistory(@JwtAuthentication String email){
        PostDTO.getAllPostResponse response = postService.getPostByHistory(email);
        return ResponseEntity.ok(response);
    }
}
