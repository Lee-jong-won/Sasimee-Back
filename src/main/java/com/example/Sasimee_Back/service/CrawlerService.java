package com.example.Sasimee_Back.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CrawlerService {

    public List<String> questions(String formUrl) throws IOException {
        List<String> questions = new ArrayList<>();

        Document doc = Jsoup.connect(formUrl).get();

        Element scriptTag = doc.selectFirst("script:containsData(FB_PUBLIC_LOAD_DATA_)");
        
        if (scriptTag != null) {
            String scriptContent = scriptTag.html();

            String jsonData = scriptContent.substring(
                scriptContent.indexOf("FB_PUBLIC_LOAD_DATA_ = ") + 23,
                scriptContent.indexOf(";", scriptContent.indexOf("FB_PUBLIC_LOAD_DATA_ = "))
            ).trim();

            if (jsonData.startsWith("A_ = ")) {
                jsonData = jsonData.substring(5).trim();
            }

            if (jsonData.startsWith("[")) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(jsonData);
                    
                    JsonNode questionsNode = rootNode.get(1).get(1);
                    if (questionsNode != null && questionsNode.isArray()) {
                        for (JsonNode questionNode : questionsNode) {
                            String questionTitle = questionNode.get(1).asText();
                            questions.add(questionTitle);
                        }
                    }
                } catch (Exception e) {
                    throw new IOException("Error parsing JSON data: " + e.getMessage(), e);
                }
            } else {
                throw new IllegalArgumentException("Unexpected data format: " + jsonData);
            }
        } else {
            throw new IllegalArgumentException("No script tag with FB_PUBLIC_LOAD_DATA_ found");
        }

        return questions;
    }
}
