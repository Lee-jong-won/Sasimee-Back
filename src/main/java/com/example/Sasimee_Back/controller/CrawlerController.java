package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.service.CrawlerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "구글폼 분석", description="구글폼 분석을 위한 api")
public class CrawlerController {
    private final CrawlerService crawlerService;
    
    @GetMapping("/AIresponse")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "구글폼 분석 성공"),
            @ApiResponse(responseCode = "200", description = "구글폼 분석 실패")
    })
    public String verifyQuestions(@RequestParam String formUrl) throws IOException {
        return crawlerService.verifyQuestions(formUrl);
    }
}
