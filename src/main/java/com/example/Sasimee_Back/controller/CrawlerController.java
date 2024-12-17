package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.service.CrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "구글폼 분석", description="구글폼 분석을 위한 api")
public class CrawlerController {
    private final CrawlerService crawlerService;

    @Operation(summary = "구글폼 링크 검수", description = "Gemini를 기반으로 한 AI검수")
    @GetMapping("/verify")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구글폼 분석 성공"),
            @ApiResponse(responseCode = "400", description = "구글폼 분석 실패")
    })
    public String verifyQuestions(@RequestParam String formUrl) throws IOException {
        return crawlerService.verifyQuestions(formUrl);
    }
}
