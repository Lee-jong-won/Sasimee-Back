package com.example.Sasimee_Back.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Sasimee_Back.entity.ClickHistory;
import com.example.Sasimee_Back.entity.Recommend;
import com.example.Sasimee_Back.repository.ClickHistoryRepository;
import com.example.Sasimee_Back.repository.RecommendRepository;
import com.example.Sasimee_Back.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private final ClickHistoryRepository clickHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Long> getRecommendById(long post_id){
        List<Recommend> recommends = recommendRepository.findByPostId(post_id);
        List<Long> similar_post_ids = new ArrayList<>();

        for(int i = 0; i < recommends.size(); i++){
            long similar_post_id = recommends.get(i).getSimilarPostId();
            similar_post_ids.add(similar_post_id);
        }

        return similar_post_ids;
    }

    @Transactional
    public List<Long> getRecommendByRecent(String email){
        List<ClickHistory> clickHistories = clickHistoryRepository.findByUser(userRepository.findByEmail(email).orElseThrow());
        List<Long> recommendList = new ArrayList<>();
        
        Long recent_clicked_post = clickHistories.get(0).getPost().getId();
        recommendList = getRecommendById(recent_clicked_post);

        return recommendList;
    }
}
