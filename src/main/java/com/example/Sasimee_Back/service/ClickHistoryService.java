package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.entity.ClickHistory;
import com.example.Sasimee_Back.entity.Post;
import com.example.Sasimee_Back.entity.Tag;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.repository.ClickHistoryRepository;
import com.example.Sasimee_Back.repository.PostRepository;
import com.example.Sasimee_Back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClickHistoryService {
    private final ClickHistoryRepository clickHistoryRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveClickHistory(Long userId, Long postId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
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

    @Transactional
    public List<String> recommender(Long userId){
        List<ClickHistory> clickHistoryList = clickHistoryRepository.findByUser(userRepository.findById(userId).orElseThrow(RuntimeException::new));

        Map<String, Integer> tagFrequencies = new HashMap<>();

        for(ClickHistory clickHistory : clickHistoryList){
            Post post = clickHistory.getPost();
            List<String> tags =post.getTags().stream()
                    .map(Tag::getName)
                    .toList();

            for(String tag : tags){
                tagFrequencies.put(tag, tagFrequencies.getOrDefault(tag, 0) + 1);
            }
        }

        return tagFrequencies.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }
}
