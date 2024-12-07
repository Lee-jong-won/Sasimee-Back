package com.example.Sasimee_Back.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CrawlerServiceTest {

    @Autowired
    private CrawlerService crawlerService;

    @Test
    public void testQuestions() throws Exception {
        String testUrl = "https://docs.google.com/forms/d/e/1FAIpQLSe2ZVRWS-v4yeM6Qqy6epcXHmEGnQbRDk4lq4f4eCt7TXD5AA/viewform";

        List<String> questions = crawlerService.questions(testUrl);

        assertNotNull(questions);
        assertFalse(questions.isEmpty());
        questions.forEach(System.out::println);
    }
}
