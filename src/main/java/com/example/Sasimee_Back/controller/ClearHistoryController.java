package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.service.ClearHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "수행 기록", description = "사용자가 완료한 포스트들을 관리하는 API")
@RestController
@RequestMapping("/clearHistory")
@RequiredArgsConstructor
public class ClearHistoryController {
    private final ClearHistoryService clearHistoryService;

    @Operation(summary = "수행 기록 저장", description = "유저가 완수한 포스트들을 저장한다.")
    @PostMapping("/save/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수행 기록 저장 성공"),
            @ApiResponse(responseCode = "400", description = "수행 기록 저장 실패")
    })
    public ResponseEntity<Void> saveClearHistory(@RequestParam Long userId, @PathVariable Long postId) {
        clearHistoryService.saveClearHistory(userId, postId);
        return ResponseEntity.ok().build();
    }
}
