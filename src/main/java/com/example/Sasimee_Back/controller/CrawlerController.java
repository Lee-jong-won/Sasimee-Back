package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CrawlerController {
    private final CrawlerService crawlerService;

    @GetMapping("/AIresponse")
    public String verifyQuestions(@RequestParam String formUrl) throws IOException {
        return crawlerService.verifyQuestions(formUrl);
    }
}
