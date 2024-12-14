package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.entity.ClearHistory;
import com.example.Sasimee_Back.entity.Post;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.repository.ClearHistoryRepository;
import com.example.Sasimee_Back.repository.PostRepository;
import com.example.Sasimee_Back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ClearHistoryService {
    private final ClearHistoryRepository clearHistoryRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveClearHistory(String useremail, Long postId){
        User user = userRepository.findByEmail(useremail).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("포스트를 찾을 수 없습니다."));

        ClearHistory clearHistory = new ClearHistory();

        clearHistory.setUser(user);
        clearHistory.setPost(post);
        clearHistory.setTimestamp(new Date().toString());

        clearHistoryRepository.save(clearHistory);
    }
}
