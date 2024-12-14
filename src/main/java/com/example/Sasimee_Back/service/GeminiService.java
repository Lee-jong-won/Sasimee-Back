package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.GeminiDTO.GeminiRequest;
import com.example.Sasimee_Back.dto.GeminiDTO.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final RestTemplate restTemplate;

    public String AIRequest(String question){
        String geminiURL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + "AIzaSyDmrx3L9ciM9t3kUfgSJ7SLOesfo75wWGc";
        String requestText = "다음 설문으로 사용할 질문들이 문제가 있는지 검증해줘\n" + question + "각 질문에 대해 오/탈자는 없는지, 이해하기 쉬운 언어로 구성되어있는지, 이중/중복 질문이 없는지, 응답태도에 영향을 미칠만한 오류가 있는지 확인하고 3줄 안팎으로 요약해. 강조 표시는 사용하지 말고, 측정 척도에 대해서는 평가하지 마.";

        GeminiRequest request = new GeminiRequest();
        request.createGeminiRequest(requestText);

        try{
            GeminiResponse response = restTemplate.postForObject(geminiURL, request, GeminiResponse.class);
            return response.getCandidates().get(0).getContent().getParts().get(0).getText();
        }catch (Exception e){
            throw new RuntimeException("질문 검증 단계에서 오류 발생", e);
        }
    }
}
