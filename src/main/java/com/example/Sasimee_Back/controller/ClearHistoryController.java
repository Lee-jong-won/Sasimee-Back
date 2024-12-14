package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.service.ClearHistoryService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clearHistory")
@RequiredArgsConstructor
@Tag(name = "수행 기록 관리", description="수행 기록 관리를 위한 api들")
public class ClearHistoryController {
    private final ClearHistoryService clearHistoryService;

    @PostMapping("/save/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "수행한 게시글 저장 성공"),
            @ApiResponse(responseCode = "200", description = "수행한 게시글 저장 실패")
    })
    public ResponseEntity<Void> saveClearHistory(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @PathVariable Long postId) {
        clearHistoryService.saveClearHistory(sasimeePrincipal.getUseremail(), postId);
        return ResponseEntity.ok().build();
    }
}
