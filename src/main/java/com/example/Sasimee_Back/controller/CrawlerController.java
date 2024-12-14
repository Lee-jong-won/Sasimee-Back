package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.service.CrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "크롤링 및 검수", description = "구글폼을 크롤링하고 Gemini를 통해 검수하는 API")
@RestController
@RequiredArgsConstructor
public class CrawlerController {
    private final CrawlerService crawlerService;

    @Operation(summary = "구글폼 검수", description = "구글 폼 링크를 받고 질문들에 대한 AI 검수를 반환한다.")
    @GetMapping("/AIresponse")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구글폼 검수 결과 반환 성공"),
            @ApiResponse(responseCode = "400", description = "구글폼 검수 결과 반환 실패")
    })
    
    public String verifyQuestions(@RequestParam String formUrl) throws IOException {
        return crawlerService.verifyQuestions(formUrl);
    }
}
