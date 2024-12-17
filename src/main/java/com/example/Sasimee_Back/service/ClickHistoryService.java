package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.entity.*;
import com.example.Sasimee_Back.repository.ClickHistoryRepository;
import com.example.Sasimee_Back.repository.PostRepository;
import com.example.Sasimee_Back.repository.UserRepository;
import com.example.Sasimee_Back.repository.UserTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClickHistoryService {
    private final ClickHistoryRepository clickHistoryRepository;
    private final UserTagRepository userTagRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveClickHistory(String userEmail, Long postId){
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("글을 찾을 수 없습니다."));

        List<ClickHistory> clickHistoryList = clickHistoryRepository.findByUser(user);

        if(clickHistoryList.size() >= 10){
            ClickHistory oldHistory = clickHistoryList.stream()
                    .min(Comparator.comparing(ClickHistory::getTimestamp))
                    .orElseThrow(() -> new RuntimeException("검색 기록을 찾을 수 없습니다."));

            clickHistoryRepository.delete(oldHistory);
        }

        ClickHistory clickHistory = new ClickHistory();
        clickHistory.setUser(user);
        clickHistory.setPost(post);
        clickHistory.setTimestamp(new Date().toString());

        clickHistoryRepository.save(clickHistory);
    }

    public List<String> recommender(String userEmail){
        List<ClickHistory> clickHistoryList = clickHistoryRepository.findByUser(userRepository.findByEmail(userEmail).orElseThrow(RuntimeException::new));

        Map<String, Integer> tagFrequencies = new HashMap<>();

        for(ClickHistory clickHistory : clickHistoryList){
            Post post = clickHistory.getPost();
            List<String> tags = post.getTags().stream()
                    .map(Tag::getName)
                    .toList();

            for(String tag : tags){
                tagFrequencies.put(tag, tagFrequencies.getOrDefault(tag, 0) + 1);
            }
        }

        List<String> recommendTags = tagFrequencies.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        if(recommendTags.size() < 10){
            List<String> userTags = getUserTags(userEmail);
            if(!userTags.isEmpty()){
                for(String tag : userTags){
                    tagFrequencies.put(tag, tagFrequencies.getOrDefault(tag, 0) + 1);
                }
            }
        }

        return tagFrequencies.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    private List<String> getUserTags(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        List<UserTag> userTags = userTagRepository.findByUser(user);

        return userTags.stream()
                .map(UserTag::getName)
                .collect(Collectors.toList());
    }
}
