package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.argumentResolver.JwtAuthentication;
import com.example.Sasimee_Back.authentication.User;
import com.example.Sasimee_Back.service.ClickHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/click")
@RequiredArgsConstructor
@Tag(name = "클릭 기록 관리", description = "클릭 기록 관리를 위한 API들 입니다.")
public class ClickHistoryController {
    private final ClickHistoryService clickHistoryService;

    @Operation(summary = "유저 클릭 기록 저장", description = "현재 접속한 유저의 해당 게시글 ID 클릭 기록을 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "클릭 기록 저장 실패"),
            @ApiResponse(responseCode = "200", description = "클릭 기록 저장 성공")
    })
    @User
    @PostMapping("/{id}")
    public ResponseEntity<Void> saveClickHistory(@PathVariable long id, @JwtAuthentication String email) {
        clickHistoryService.saveClickHistory(email, id);
        return ResponseEntity.ok().build();
    }
}
